package com.trakntell.web.service;

import com.trakntell.web.Utils;
import com.trakntell.web.controllers.OTPController;
import com.trakntell.web.models.MobileOrgVehicle;
import com.trakntell.web.models.TNTOAuthAccessToken;
import com.trakntell.web.repositories.MobileOrgVehicleRepository;
import com.trakntell.web.repositories.OAuthAccessTokenRepository;
import com.trakntell.web.rest.models.response.AlexaDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

// fetches the list of vehicles wose susbsription is over from local database
//Fetches the latest subscription data and check whether item is enable or not from remote iqube apis for the list of vehicles whose subsription is over
//If yes then updates those details in our local database i.e mobileOrgVehicle table
@Configuration
public class CheckSubscriptionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CheckSubscriptionScheduler.class);

    @Autowired
    MobileOrgVehicleRepository mobileOrgVehicleRepository;

    @Autowired
    OTPController otpController;

    @Autowired
    DataSource dataSource;

    public static final ZoneId ZONE_IST 		= ZoneId.of("Asia/Calcutta");


    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)  // periodic task which runs in every 24 Hours
    @Transactional
    public void checkSubscription() {
        logger.info("Enter Inside checkSubscription()");
        try {
            logger.debug("Datasource = {}", dataSource.getConnection().getMetaData().getURL());
        }
        catch (Exception e) {
            logger.error("Exception in scheduler while printing datasource info", e);
        }

        String userid = "";
        int rc = 0;

//        Get the list of vehicles from our database whose subscription is over
        List<MobileOrgVehicle> expired_users_list = mobileOrgVehicleRepository.findBySubscription_end_dateLessThan(LocalDateTime.now(ZONE_IST));

        for(int idx = 0; idx < expired_users_list.size(); idx++) {

            userid = expired_users_list.get(idx).getMobileNum();

            logger.info("userid = {} ", userid);

            try {
//                Fetches latest subscription details from iqube apis for the local vehicles whose subscription is over
                Optional<AlexaDetailsResponse.Details> alexaSubDetails = otpController.fetchAlexaSubscriptionDetails(userid);

                if(alexaSubDetails.isPresent()) {
                    logger.info("alexaSubDetails = {} ", alexaSubDetails.get());

                    boolean subscription_status = alexaSubDetails.get().isAlexaEnabled();
                    String subscription_end_date = alexaSubDetails.get().getSubscriptionEndDate();
                    logger.info("userid = {}, subscription_status = {}, subscription_end_date = {} ", userid, subscription_status, subscription_end_date);

                    LocalDateTime db_date_time_sub = Utils.convertIntoLocalDateTime(subscription_end_date);
                    logger.info("subscription_end_date ##  db_date_time_sub= {} ", db_date_time_sub);

                    // Query Execution for update subscription_status and subscription_end_date to our DB
                    rc = mobileOrgVehicleRepository.copySubStatusAndDateToDB(userid, subscription_status ? 1 : 0, db_date_time_sub, Utils.getCurrentTimeInDbFormat());

                    logger.info("rc for user {} is {} ", userid, rc);
                }
                else {
                    logger.info("No Details Found for userid = {} ", userid);
                }
            } catch(Exception ex) {
                logger.error("Exception", ex);
            }

        }
    }
}
