package com.trakntell.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trakntell.web.Utils;
import com.trakntell.web.models.*;
import com.trakntell.web.repositories.MobileOrgVehicleRepository;
import com.trakntell.web.repositories.OtpRepository;
import com.trakntell.web.rest.TNTOTPClient;
import com.trakntell.web.rest.TVSMOTPClient;
import com.trakntell.web.rest.graphql.GraphQLApiClient;
import com.trakntell.web.rest.graphql.request.GraphQLRequest;
import com.trakntell.web.rest.graphql.response.BaseGraphqlResponse;
import com.trakntell.web.rest.graphql.response.UserDetailsResponse.*;
import com.trakntell.web.rest.models.response.AlexaDetailsResponse;
import com.trakntell.web.rest.models.response.ISO_CODES;
import com.trakntell.web.rest.models.response.TNTOTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OTPController {
    private static final Logger logger = LoggerFactory.getLogger(OTPController.class);

    private static final String ALEXA_NOT_ENABLED_LABEL = "Alexa services are not available for your subscription";

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    TNTOTPClient tntotpClient;

    @Autowired
    TVSMOTPClient tvsmotpClient;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    MobileOrgVehicleRepository mobileOrgVehRepo;

    private TokenResponse tvsmAccessToken;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private static GraphQLApiClient graphqlClient;

    @Autowired
    public void setgraphqlClient(GraphQLApiClient graphqlClient) {
        this.graphqlClient = graphqlClient;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/requestOTP")
    public ResponseEntity<?> requestOTP(@RequestParam("mobile") String mobile) {
        logger.debug("activeProfile = {}", activeProfile);

        if (mobile == null || mobile.length() <= 0) {
            return new ResponseEntity<String>("Invalid mobile number", HttpStatus.BAD_REQUEST);
        }

        Optional<AlexaDetailsResponse.Details> alexaSubDetails;

        if("9891748782".equals(mobile)) {
            AlexaDetailsResponse.Details details = new AlexaDetailsResponse.Details();
            details.setMobile(mobile);
            details.setAlexaEnabled(true);
            details.setSubscriptionEndDate("2023-10-27T00:00:00");

            alexaSubDetails = Optional.of(details);
        }
        else {
            // get alexa subscription for particular mobile no
            alexaSubDetails = fetchAlexaSubscriptionDetails(mobile);
        }

        if(alexaSubDetails.isPresent()) {
            logger.info("alexaSubDetails = {} ", alexaSubDetails.get());

            //boolean subscription_status = alexaSubDetails.get().isAlexaEnabled();

            if(!(alexaSubDetails.get().isAlexaEnabled())) {
                return new ResponseEntity<String>(ALEXA_NOT_ENABLED_LABEL, HttpStatus.BAD_REQUEST);
            }

        } else {
            logger.info("No Details Found for userid = {} ", mobile);
            return new ResponseEntity<String>(ALEXA_NOT_ENABLED_LABEL, HttpStatus.BAD_REQUEST);
        }

        try {
            //OTPResponse response = tvsmotpClient.getOTP(new OTPRequest(mobile));
            Map<String, String> requestData = new LinkedHashMap<>();
            requestData.put("action", "validate_mobile");
            requestData.put("mobile_number", mobile);
            String token = null;
            try {
                token = Utils.getTokenForGraphQLApis(graphqlClient);
            } catch(Exception e) {
                logger.error("Exception while generating tvsm token for P360 generate token api", e);
                return new ResponseEntity<>(StatusMessage.getTvs_server_side_error(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //Tnt api to validatae mobile no as well as get orgId and vehicleId detials
            String vin = getPrimaryUserVinDetailsByMobleNo(token, mobile);
            if(vin == null) {
                return new ResponseEntity<>(StatusMessage.getTvs_server_side_error(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            MobileOrgVehicle mobileOrgVehicle = new MobileOrgVehicle();
            mobileOrgVehicle.setMobileNum(mobile);
            mobileOrgVehicle.setOrgid("12345");
            mobileOrgVehicle.setVehicleid(vin);
            mobileOrgVehicle.setSubscription_status((alexaSubDetails.get().isAlexaEnabled() ? 1 : 0));
            mobileOrgVehicle.setSubscription_end_date(Utils.convertIntoLocalDateTime(alexaSubDetails.get().getSubscriptionEndDate()));
            mobileOrgVehicle.setModified(Utils.getCurrentTimeInDbFormat());
            //saves the record in table
            mobileOrgVehRepo.save(mobileOrgVehicle);

            //TODO:
            //Step-1: Generate 6 digit otp
            //Step-2: Request to TVSM servers for token generation
            //Step-3: Request to TVSM servers for sending otp (token, mobile, otp)

            if(tvsmAccessToken == null || StringUtils.isEmpty(tvsmAccessToken.getToken())) {
                try {
                    tvsmAccessToken = generateToken();
                }
                catch (Exception e) {
                    logger.error("Exception while generating tvsm token", e);
                    return new ResponseEntity<>(StatusMessage.getTvs_server_side_error(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            String otp = null;
            //default otp generated for default mobile number for testing
            if("6281686067".equals(mobile)) {
                otp = "123456";
            }
            else {
                otp = Utils.genOTP();
            }

            logger.info("OTP generated = {}", otp);
            saveOtp(otp, mobile);
            boolean result = false;

            //if("dev".equals(activeProfile)) {
            //    result = true;
            //}
            //else {
            //Sends otp to mobile number using already built iqube apis
            result = sendOTPSMS(mobile, otp, tvsmAccessToken.getToken());
            //}

            if(result) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(StatusMessage.getTvs_server_side_error(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Exception in otp", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public String getPrimaryUserVinDetailsByMobleNo(String token, String phoneNumber) {
//        String query = "query GetTvsmLatestDeviceData($phoneNumber: String!) {\n"
//                + "  getTvsmLatestDeviceData(vin: $vin) {\n"
//                + "    response {\n"
//                + "      charging_status\n"
//                + "      time_to_charge_completion\n"
//                + "      vin\n"
//                + "      userId\n"
//                + "      datetime_pkt\n"
//                + "    }\n"
//                + "    status\n"
//                + "    statusMessage\n"
//                + "  }\n"
//                + "}\n"
//                + "";
        String query = "query GetPrimaryUserDetailsByMobileNo($mobile_number: String!, $iso_code: ISO_CODES!) {" +
                "getUserDetailsByMobileNumber(mobile_number: $mobile_number, iso_code: $iso_code) {" +
                "details{\n" +
                "      user_details{\n" +
                "        associated_vins{\n" +
                "          vin\n" +
                "          type\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    status,\n" +
                "    statusMessage" +
                "}" +
                "}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("mobile_number", phoneNumber);
        variables.put("iso_code", ISO_CODES.IND);

        GraphQLRequest request = new GraphQLRequest(query, variables, "Bearer " + token);
        BaseGraphqlResponse response = null;
        String vin = null;
        Gson gson = new Gson();
        try {
            response = graphqlClient.request(request, "Bearer " + token);
            if(response != null) {
                Map<String, Object> responseData = response.getData();
                if(responseData == null ) {
                    throw new Exception("Invalid mobile mobile");
                }
//                LinkedHashMap<String,> getUserDetailsByMobileNumberMap = responseData
//                        .get("getUserDetailsByMobileNumber");
                UserDetailsResponse getUserDetailsByMobileNumber = gson.fromJson(gson.toJson(responseData.get("getUserDetailsByMobileNumber")), UserDetailsResponse.class);
//                (UserDetailsResponse)
                logger.debug("GetTvsmLatestDeviceData Api response: " + getUserDetailsByMobileNumber);
                if(getUserDetailsByMobileNumber.getStatus() != 200) {
                    throw new Exception(getUserDetailsByMobileNumber.getStatusMessage());
                }
                UserDetailsObject userDetailsObject = getUserDetailsByMobileNumber.getDetails();
                if(userDetailsObject.getUser_details() == null || userDetailsObject.getUser_details().length == 0) {
                    throw new Exception("User details Object is null or of size 0");
                }
                DetailsOfUser[] userDetailsArray = userDetailsObject.getUser_details();

                List<AssociatedVins> associatedVins = new ArrayList<>();

                for(DetailsOfUser detailsOfUser: userDetailsArray) {
                    if(detailsOfUser.getAssociated_vins() != null && detailsOfUser.getAssociated_vins().length > 0) {
                        AssociatedVins[] tempAssociatedVins = detailsOfUser.getAssociated_vins();
                        tempAssociatedVins = Arrays.asList(tempAssociatedVins).stream().filter(item -> item.getType() == UserType.PRIMARY).toArray(AssociatedVins[]::new);
//                        tempAssociatedVins = (List<AssociatedVins>) tempAssociatedVins.stream().filter(item -> item.getType() == UserType.PRIMARY);
                        for(AssociatedVins associatedVinsObj : tempAssociatedVins) {
                            associatedVins.add(associatedVinsObj);
                        }
                    }
                }

                if(associatedVins.size() > 0) {
                    vin = associatedVins.get(0).getVin();
                }
            } else {
                throw new Exception("response is null");
            }
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            return null;
        }
        return vin;
    }

    public TokenResponse generateToken() throws Exception {
        //token generation as we need this token to sent in each request for fetching iqube api response
        Map<String, String> requestToken = new LinkedHashMap<>();
        requestToken.put("grant_type", "password");
        requestToken.put("username", "getapitoken");
        requestToken.put("password", "Y4tgXk=W6$!m8st6");

        return tvsmotpClient.getToken(requestToken);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/resendOTP")
    public ResponseEntity<?> resendOTP(@RequestParam("mobile") String mobile) {
        if (mobile == null || mobile.length() <= 0) {
            return new ResponseEntity<String>("Invalid mobile number", HttpStatus.BAD_REQUEST);
        }
        //Get otp is stored or not if yes then check if max no of try is done or not
        OtpModel otp = otpRepository.findTop1ByMobileAndExpireGreaterThanEqualAndNumSentLessThanAndUsedOrderByCreatedDesc(mobile, Utils.getCurrentTimeInDbFormat(), 3, 0);
        if(otp == null) {
            return new ResponseEntity<>("You have exceeded maximum retries.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            //send otp to email using iqube apis
            boolean result = sendOTPSMS(mobile, otp.getOtp(), tvsmAccessToken.getToken());
            if(result) {
                otp.setNumSent(otp.getNumSent() + 1);
                otp.setModified(Utils.getCurrentTimeInDbFormat());
                otpRepository.save(otp);

                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                tvsmAccessToken = null;
                return new ResponseEntity<>("Some error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/verifyOTP")
    public ResponseEntity<?> verifyOTP(@RequestParam("mobile") String mobile, @RequestParam("otp") String otp) {
        logger.debug("verifyOTP, mobile = {}, otp = {}", mobile, otp);

        if (mobile == null || mobile.length() <= 0) {
            return new ResponseEntity<String>("Invalid mobile number", HttpStatus.BAD_REQUEST);
        }

        if (otp == null || otp.length() <= 0) {
            return new ResponseEntity<String>("Invalid mobile number", HttpStatus.BAD_REQUEST);
        }

        // Fetches stored otp from otp table
        OtpModel otpModel = otpRepository.findTop1ByMobileAndExpireGreaterThanEqualAndNumSentLessThanAndUsedOrderByCreatedDesc(mobile, Utils.getCurrentTimeInDbFormat(), 3, 0);
        if(otpModel != null) {
            String otpFromDb = otpModel.getOtp();

            if(otpFromDb.equals(otp)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("Invalid OTP", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<String>("Invalid Request", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean sendOTPSMS(String mobile, String otp, String token) {
        Map<String, String> otpParams = new LinkedHashMap<>();
        otpParams.put("mobileNumber", mobile);
        //otpParams.put("mobileNumber", "8440000768");
        otpParams.put("otp", otp);
        otpParams.put("templateId", "1607100000000193252");


        SendOtpResponse otpResponse = tvsmotpClient.sendOtp("Bearer " + token, otpParams);

        if (otpResponse.getStatus() == 200 && SendOtpResponse.SUCCESS.equals(otpResponse.getResult())) {
            return true;
        } else {
            return false;
        }


        //return true;
    }

    //saves otp in otp table
    private void saveOtp(String otp, String mobile) {
        OtpModel otpModel = new OtpModel();
        otpModel.setOtp(otp);
        otpModel.setMobile(mobile);
        otpModel.setNumSent(0);
        otpModel.setUsed(0);
        otpModel.setCreated(Utils.getCurrentTimeInDbFormat());
        otpModel.setModified(Utils.getCurrentTimeInDbFormat());
        otpModel.setExpire(Utils.getOTPExpireTime());

        otpRepository.save(otpModel);
    }

    public boolean isAlexaEnabled(String mobile) {
        tvsmAccessToken = null;
        if(tvsmAccessToken == null || StringUtils.isEmpty(tvsmAccessToken.getToken())) {
            try {
                tvsmAccessToken = generateToken();
            }
            catch (Exception e) {
                logger.error("Exception while generating tvsm token", e);
                return false;
            }
        }

        //Get alexa subscription detals
        AlexaDetailsResponse alexaDetailsResponse = tvsmotpClient.getAlexaDetails("Bearer " + tvsmAccessToken.getToken(), mobile);

        if (alexaDetailsResponse.getStatus() == 200 && SendOtpResponse.SUCCESS.equals(alexaDetailsResponse.getResult())) {
            ArrayList<AlexaDetailsResponse.Details> listDetails = alexaDetailsResponse.getData();
            if (listDetails != null && !listDetails.isEmpty()) {
                AlexaDetailsResponse.Details alexaDetails = listDetails.get(0);
                return alexaDetails.isAlexaEnabled();
            } else {
                //As discussed: In case of empty data, need to deny.
                return false;
            }
        } else {
            return false;
        }
    }

    public Optional<AlexaDetailsResponse.Details> fetchAlexaSubscriptionDetails(String mobile) {
        logger.info("Inside fetchAlexaSubscriptionDetails for userid = {} ",mobile);
        tvsmAccessToken = null;
        if(tvsmAccessToken == null || StringUtils.isEmpty(tvsmAccessToken.getToken())) {
            try {
                tvsmAccessToken = generateToken();
            }
            catch (Exception e) {
                logger.error("Exception while generating tvsm token", e);
                return Optional.empty();
            }
        }

        AlexaDetailsResponse alexaDetailsResponse = tvsmotpClient.getAlexaDetails("Bearer " + tvsmAccessToken.getToken(), mobile);

        if (alexaDetailsResponse.getStatus() == 200 && SendOtpResponse.SUCCESS.equals(alexaDetailsResponse.getResult())) {
            ArrayList<AlexaDetailsResponse.Details> listDetails = alexaDetailsResponse.getData();
            if (listDetails != null && !listDetails.isEmpty()) {
                AlexaDetailsResponse.Details alexaSubDetails = listDetails.get(0);
                logger.debug("alexaSubDetails: {}", alexaSubDetails);
                return Optional.of(alexaSubDetails);
            } else {
                logger.info("No Details Found for userid = {} ", mobile);
                return Optional.empty();
            }
        } else {
            logger.info("STATUS != 200 ");
            return Optional.empty();
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        logger.error("Exception ", ex);

        return new ResponseEntity<String>("Some error occurred. Please try again", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}