package com.trakntell.web.rest;

import com.trakntell.web.rest.models.response.TNTOTPResponse;
import com.trakntell.web.rest.models.response.TNTVerifyOTPResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

//@FeignClient(name = "tvsmotpclient", url = "https://tvs-iqube-stage-api.azurefd.net/api")
@FeignClient(name = "tntotpclient", url = "https://api.trakntell.com/tnt/servlet")
public interface TNTOTPClient {

    //@RequestMapping(method = RequestMethod.POST, value = "/UserLogin/Login", produces = "application/json")
    //OTPResponse getOTP(@RequestBody OTPRequest request);

    @PostMapping(value = "/tntServiceTVSMAlexaOTP", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TNTOTPResponse validateMobileNumber(@RequestBody Map<String, ?> form);

    @PostMapping(value = "/tntServiceTVSMAlexaOTP", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TNTVerifyOTPResponse validateOTP(@RequestBody Map<String, ?> form);


    //@RequestMapping(method = RequestMethod.POST, value = "/RegisterUser/VerifyOTP", produces = "application/json", consumes = "application/json")
    //VerifyOTPResponse validateOTP(@RequestBody VerifyOTPRequest request);
}
