package com.trakntell.web.controllers;

import com.trakntell.web.Utils;
import com.trakntell.web.repositories.MobileOrgVehicleRepository;
import com.trakntell.web.rest.models.response.AlexaDetailsResponse;
import com.trakntell.web.rest.models.response.SubscriptionStatusResponse;
import com.trakntell.web.rest.models.response.SubscriptionStatusUpdateResponse;
import com.trakntell.web.rest.models.response.TNTOTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class SubscriptionStatusController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionStatusController.class);

    @Autowired
    private MobileOrgVehicleRepository mobileOrgVehicleRepository;

    /*
     * It is possible that iqube user has purchased the alexa skill - but not signed up in Alexa yet
     *     - Status success and code = USER_NOT_SIGNED_UP
     * Good case
     *      - Status success and code = SUBSCRIPTION_UPDATED
     * Exception
     *      - Status failure and
     *          code = MISSING_FIELD
     *          code = AUTH_FAILURE
     *          code = INVALID_DATE
     *          code = INTERNAL_SERVER_ERROR
     */

    @PostMapping(value = "/api/updateSubscriptionStatus")
    @Transactional
    public ResponseEntity<SubscriptionStatusResponse> updateSubscriptionStatus(@RequestBody AlexaDetailsResponse.Details subscriptionStatusRequestDetails) {
        logger.debug("Enter inside updateSubscriptionStatus");
        try {
            if (subscriptionStatusRequestDetails != null) {
                String mobile = subscriptionStatusRequestDetails.getMobile();

                if(StringUtils.isEmpty(mobile)) {
                    SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();

                    subscriptionStatusRes.setStatus("error");
                    subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_INVALID_MOBILE_NUMBER);
                    return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.BAD_REQUEST);
                }

                String subscription_end_date = subscriptionStatusRequestDetails.getSubscriptionEndDate();
                if(StringUtils.isEmpty(subscription_end_date)) {
                    SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
                    subscriptionStatusRes.setStatus("error");
                    subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_INVALID_DATE);
                    return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.BAD_REQUEST);
                }

                LocalDateTime subscriptionEndLocalDateTime = Utils.convertIntoLocalDateTime(subscription_end_date);

                if(subscriptionEndLocalDateTime == null) {
                    SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
                    subscriptionStatusRes.setStatus("error");
                    subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_INVALID_DATE);
                    return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.BAD_REQUEST);
                }

                int subscription_status = (subscriptionStatusRequestDetails.isAlexaEnabled()) ? 1 : 0;

                logger.info("userid = {}, subscription_status = {}, subscription_end_date = {} ", mobile, subscription_status, subscription_end_date);

                int rc = mobileOrgVehicleRepository.copySubStatusAndDateToDB(mobile, subscription_status, subscriptionEndLocalDateTime, Utils.getCurrentTimeInDbFormat());
                logger.info("mobile= {}, rc = {} ",mobile, rc);

                if(rc == 1) {
                    SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
                    subscriptionStatusRes.setStatus("success");
                    subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_SUBSCRIPTION_UPDATED);
                    return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.OK);
                }
                else if(rc == 0) {
                    SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
                    subscriptionStatusRes.setStatus("success");
                    subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_USER_NOT_SIGNED_UP);
                    return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.OK);
                }
                else {
                    SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
                    subscriptionStatusRes.setStatus("error");
                    subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_SERVER_ERROR);
                    return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else {
                SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
                subscriptionStatusRes.setStatus("error");
                subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_MISSING_FIELDS);
                return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception ex) {
            logger.error("Exception", ex);

            SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
            subscriptionStatusRes.setStatus("error");
            subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_SERVER_ERROR);
            return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        logger.error("Exception ", ex);

        SubscriptionStatusResponse subscriptionStatusRes = new SubscriptionStatusResponse();
        subscriptionStatusRes.setStatus("error");
        subscriptionStatusRes.setCode(SubscriptionStatusResponse.CODE_SERVER_ERROR);
        return new ResponseEntity<>(subscriptionStatusRes, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}