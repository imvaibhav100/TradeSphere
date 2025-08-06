package com.vaibh.controller;

import com.vaibh.domain.PaymentMethod;
import com.vaibh.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class PaymentController {



    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<Void> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt) throws UserException {


        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


}
