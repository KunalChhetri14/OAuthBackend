package com.trakntell.web.config;

import com.trakntell.web.Utils;
import com.trakntell.web.models.OtpModel;
import com.trakntell.web.repositories.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.LinkedHashMap;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);

    @Autowired
    OtpRepository otpRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mobile = authentication.getName();
        String otp = authentication.getCredentials().toString();
        logger.debug(authentication.toString());
        logger.info("mobile = {}, otp = {}", mobile, otp);

        //boolean validateOTPResult = validateOTP(mobile, otp);
        //logger.info("validateOTPResult = {}", validateOTPResult);
        //if(validateOTPResult) {
        if(authentication.getDetails() instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> details = (LinkedHashMap<String, Object>) authentication.getDetails();

            logger.debug("details = {}", details);

            if ("getexttoken".equals(mobile) && "HDPGtGYoC9".equals(otp)) {
                return new UsernamePasswordAuthenticationToken(mobile, otp, Collections.emptyList());
            }
            else {
                logger.debug("Throwing exception");
                throw new BadCredentialsException("Invalid credentials");
            }
        }

        try {
            /*TNTUsernamePasswordAuthenticationToken token = new TNTUsernamePasswordAuthenticationToken(mobile, otp, Collections.emptyList());
            token.setOrgid(orgid);
            token.setVehicleid(vehicleid);*/
            return new UsernamePasswordAuthenticationToken(mobile, otp, Collections.emptyList());
        }
        catch (Exception e) {
            logger.error("Exception in verify", e);
            return null;
        }
        /*}
        else {
            throw new BadCredentialsException("Invalid OTP");
        }*/
    }

    //function to validate otp from otp table
    private boolean validateOTP(String mobile, String otp) {
        OtpModel otpModel = otpRepository.findTop1ByMobileAndExpireGreaterThanEqualAndNumSentLessThanAndUsedOrderByCreatedDesc(mobile, Utils.getCurrentTimeInDbFormat(), 3, 0);
        if(otpModel != null) {
            String otpFromDb = otpModel.getOtp();
            logger.debug("otpFromDb = {}, otpFromUser = {}", otpFromDb, otp);
            if(otpFromDb.equals(otp)) {
                otpModel.setUsed(1);
                otpRepository.save(otpModel);

                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        logger.debug("supports {}", authentication.getCanonicalName());
        return true;
    }
}