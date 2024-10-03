package com.trakntell.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping(value = "/health", method = RequestMethod.HEAD)
    public ResponseEntity<?> checkHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/skill/health", method = RequestMethod.GET)
    public ResponseEntity<?> checkHealthGet() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/skill/test", method= RequestMethod.GET)
    public String testApp() {
//        logger.info("Inside test of multi vehicles");
        return "Alexa oauth backend deployed with version 1.0.1 and works as expected";
    }
}
