package com.trakntell.web.config;

import com.trakntell.web.Constants;
import com.trakntell.web.Utils;
import com.trakntell.web.controllers.OTPController;
import com.trakntell.web.models.MobileOrgVehicle;
import com.trakntell.web.models.TNTUsernamePasswordAuthenticationToken;
import com.trakntell.web.repositories.MobileOrgVehicleRepository;
import com.trakntell.web.rest.TVSMOTPClient;
import com.trakntell.web.rest.models.response.AlexaDetailsResponse;
import org.apache.catalina.Pipeline;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DataSource mainDataSource;

    @Autowired
    private MobileOrgVehicleRepository mobileOrgVehicleRepo;

    @Value("${access_token_validity}")
    private Integer accessTokenValidity;

    @Value("${refresh_token_validity}")
    private Integer refreshTokenValidity;

    @Autowired
    @Lazy
    OTPController otpcontroller;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);
        security.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("tnt-web-application")
                .authorizedGrantTypes("password", "refresh_token", "authorization_code")
                .autoApprove(true)
                /*
                 * Values can be read, write, test, delete
                 * then any controller method can be annotated with "@PreAuthorize("#oauth2.hasScope('read')")"
                 * to check the scope of client application
                 */
                .scopes("profile")
                .resourceIds("tntresourceid")
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity)
                .secret(passwordEncoder.encode("lUUSmr7I2LfGEu3D"))
                .redirectUris("https://alexa.amazon.co.jp/api/skill/link/MYEVBGF3O697I",  //alexa redirect urls contained inside alexa dev console account linking section
                        "https://pitangui.amazon.com/api/skill/link/MYEVBGF3O697I",
                        "https://layla.amazon.com/api/skill/link/MYEVBGF3O697I", "https://oauth.pstmn.io/v1/callback");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore);
        endpoints.authenticationManager(authenticationManager);

        endpoints.accessTokenConverter(new AccessTokenConverter() {
            @Override
            public Map<String, ?> convertAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

                logger.info("convertAccessToken, accessToken = {}, token = {}", oAuth2AccessToken.toString(), oAuth2Authentication);

                String mobile = null;
                if(oAuth2Authentication.getPrincipal() instanceof String) {
                    mobile = oAuth2Authentication.getPrincipal().toString();
                }
                else if(oAuth2Authentication.getPrincipal() instanceof User){
                    //get mobile number
                    mobile = ((User) oAuth2Authentication.getPrincipal()).getUsername();
                }

                Optional<MobileOrgVehicle> optionalMobileOrg =  mobileOrgVehicleRepo.findByMobileNum(mobile);
                if(optionalMobileOrg.isPresent()) {

                    MobileOrgVehicle mobileOrgVehicle = optionalMobileOrg.get();
                    int subscription_status = mobileOrgVehicle.getSubscription_status();

                    if(subscription_status == 0) {

                       try {
                           // Inline for checking Alexa Service is enabled OR Not
                           Optional<AlexaDetailsResponse.Details> alexaSubDetails = otpcontroller.fetchAlexaSubscriptionDetails(mobile);
                           logger.info("mobile = {}, alexaSubDetails = {} ", mobile, alexaSubDetails);

                           if (alexaSubDetails.isPresent()) {
                               subscription_status = (alexaSubDetails.get().isAlexaEnabled()) ? 1 : 0;

                               logger.info("mobile = {}, subscription_status = {} ", mobile, subscription_status);

                               if (subscription_status == 1) {
                                   // Update The Latest Subscription Status in DB
                                   int rc = mobileOrgVehicleRepo.updateSubscriptionStatus(mobile, subscription_status, Utils.getCurrentTimeInDbFormat());
                                   logger.info("mobile= {}, rc = {} ",mobile, rc);
                               }
                           }
                       } catch(Exception ex) {
                           logger.error("Exception", ex);
                        }
                    }

                    //contents returned when check_token api is being called
                    HashMap<String, Object> returnMap = new LinkedHashMap<>();
                    returnMap.put("active", !oAuth2AccessToken.isExpired());
                    returnMap.put("orgid", mobileOrgVehicle.getOrgid());
                    returnMap.put("vehicleid", mobileOrgVehicle.getVehicleid());
                    returnMap.put("subscription_status", subscription_status);
                    returnMap.put("exp", oAuth2AccessToken.getExpiresIn());
                    returnMap.put("scope", oAuth2AccessToken.getScope());
                    returnMap.put("user_name", mobile);


                    //for multi vehicles testing
                    List<Map<String, Object>> list = new ArrayList<>();
                    Map<String, Object> obj = new HashMap<>();
                    obj.put("vin", "AOI_T5266569913");
                    obj.put("vehicle_name", "iqube");
                    obj.put("user_name", "kunal");
                    obj.put("app_user_id", "NEW_AOI_T5266569913");
                    obj.put("isDefault", true);
                    obj.put("subscription_expired",1);
                    list.add(obj);
                    Map<String, Object> obj2 = new HashMap<>(); // Use obj2 instead of obj
                    obj2.put("vin", "83454636445545");
                    obj2.put("vehicle_name", "jupiter");
                    obj2.put("user_name", "rohit");
                    obj2.put("app_user_id", "USER_83454636445545");
                    obj2.put("isDefault", false);
                    obj2.put("subscription_expired", 1);
                    list.add(obj2);
                    returnMap.put("multiVehicle", list);

                    return returnMap;
                }
                else {
                    HashMap<String, Object> returnMap = new LinkedHashMap<>();
                    returnMap.put("active", false);
                    returnMap.put("user_name", oAuth2Authentication.getCredentials());

                    return returnMap;
                }
            }

            @Override
            public OAuth2AccessToken extractAccessToken(String s, Map<String, ?> map) {
                logger.info("extractAccessToken, map = {}", map.toString());
                return null;
            }

            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                logger.info("extractAuthentication, map = {}", map.toString());
                return null;
            }
        });
        endpoints.userDetailsService(userDetailsService);
        //        .authenticationManager(authenticationManager)
        //        .userDetailsService(userDetailsService);

        /*
        endpoints
                .tokenStore(tokenStore)
                .tokenEnhancer(new TokenEnhancer() {
                    @Override
                    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
                        logger.info("enhance, oAuth2AccessToken = "+ oAuth2AccessToken.getTokenType() +", token = "+oAuth2AccessToken.getValue());
                        if(oAuth2Authentication.getUserAuthentication() instanceof TNTUsernamePasswordAuthenticationToken) {
                            logger.info("yooo this is done");
                        }

                        logger.info("enhance, auth = "+oAuth2Authentication.getUserAuthentication().toString());
                        oAuth2AccessToken = new DefaultOAuth2AccessToken( ((TNTUsernamePasswordAuthenticationToken)oAuth2Authentication.getUserAuthentication()).getTvsmToken());
                        return oAuth2AccessToken;
                    }
                })
                .accessTokenConverter(new AccessTokenConverter() {
            @Override
            public Map<String, ?> convertAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
                logger.info("convertAccessToken, oAuth2AccessToken = "+ oAuth2AccessToken.getTokenType() +", token = "+oAuth2AccessToken.getValue());

                if(oAuth2Authentication.getUserAuthentication() instanceof TNTUsernamePasswordAuthenticationToken) {
                    logger.info("yooo this is done");
                }

                logger.info("convertAccessToken, auth = "+oAuth2Authentication.getUserAuthentication().toString());
                return null;
            }

            @Override
            public OAuth2AccessToken extractAccessToken(String s, Map<String, ?> map) {
                logger.info("extractAccessToken");
                return null;
            }

            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                logger.info("extractAuthentication");
                return null;
            }
        });*/
    }
    
    @Configuration
    public class TNTWebMvcConfigurer implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/login").setViewName("tvsmlogin");
        }
    }
}
