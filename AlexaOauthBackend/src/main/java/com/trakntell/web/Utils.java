package com.trakntell.web;

import com.trakntell.web.rest.graphql.request.GraphQLRequest;
import com.trakntell.web.rest.graphql.GraphQLApiClient;
import com.trakntell.web.rest.graphql.response.GenerateAuthTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static final DateTimeFormatter DB_DATETIMEFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final ZoneId ZONE_UTC = ZoneId.of("UTC");



    public static String md5(String str) {
		try { 
			  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(str.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        }
	}

    public static String getCurrentTimeInDbFormat() {
        ZonedDateTime now = ZonedDateTime.now(ZONE_UTC);
        return DB_DATETIMEFORMAT.format(now);
    }

    public static String getOTPExpireTime() {
        return DB_DATETIMEFORMAT.format(ZonedDateTime.now(ZONE_UTC).plusMinutes(10));
    }

    public static String genOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    /*public static String convertIntoDBDateTime(String date_time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(date_time, formatter);
        String db_date_time_sub = DB_DATETIMEFORMAT.format(date);
        return db_date_time_sub;
    }*/

    public static LocalDateTime convertIntoLocalDateTime(String date_time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

            LocalDateTime localDateTime = LocalDateTime.parse(date_time, formatter);
            return localDateTime;
        }
        catch (Exception e) {
            logger.error("Exception in parsing date, date_time = {}", date_time, e);
            return null;
        }
    }

    public static String getTokenForGraphQLApis(GraphQLApiClient graphqlClient) throws Exception{
        String query = "query GenerateAuthTokenAPI($username: String!, $password: String!) {" +
                "generateAuthTokenAPI(username: $username, password: $password){\n" +
                "                token\n" +
                "            } }";
//        query{
//            generateAuthTokenAPI($username:String, $password: String){
//                token
//            }
//        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "tvsmdemo");
        variables.put("password", "Tracking@123");
        try {
            GraphQLRequest request = new GraphQLRequest(query, variables);

            GenerateAuthTokenResponse response = graphqlClient.query(request);
            System.out.println(response);
            Map<String, Object> data = response.getData();
            Map<String, Object> authTokenObject = (HashMap)data.get("generateAuthTokenAPI");
            String token = (String) authTokenObject.get("token");
            return token;
        } catch(Exception e) {
            logger.error("Server side error");
            throw new Exception("Something went wrong. Please contact the tvs alexa skills help section");
        }
    }
}
