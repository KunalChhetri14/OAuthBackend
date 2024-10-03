package com.trakntell.web.rest;

import com.trakntell.web.models.SendOtpResponse;
import com.trakntell.web.models.TokenResponse;
import com.trakntell.web.rest.models.response.AlexaDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

//@FeignClient(name = "tvsmotpclient", url = "https://tvsiqubeapi.tvsmotor.com")

// Staging End Point
//@FeignClient(name = "tvsmotpclient", url = "https://tvsiqubestagapi.tvsmotor.com")

// Production End Point
@FeignClient(name = "tvsmotpclient", url = "https://tvsiqubestagapi.tvsmotor.com")
public interface TVSMOTPClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponse getToken(@RequestBody Map<String, ?> form);

    @PostMapping(value = "/api/sendotp", consumes = MediaType.APPLICATION_JSON_VALUE)
    SendOtpResponse sendOtp(@RequestHeader(value = "Authorization") String token, @RequestBody Map<String, ?> form);

    //@RequestMapping(method = RequestMethod.POST, value = "/RegisterUser/VerifyOTP", produces = "application/json", consumes = "application/json")
    //VerifyOTPResponse validateOTP(@RequestBody VerifyOTPRequest request);

    @PostMapping(value = "/ev-app-dev-api/api/vehicle/alxdetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    AlexaDetailsResponse getAlexaDetails(@RequestHeader(value = "Authorization") String token, @RequestBody String mobile);
}
