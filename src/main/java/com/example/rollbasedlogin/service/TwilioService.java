package com.example.rollbasedlogin.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void sendSms(String toNumber, String message) {

        Twilio.init(accountSid, authToken);

        Message.creator(
                new com.twilio.type.PhoneNumber(toNumber),
                new com.twilio.type.PhoneNumber(fromNumber),
                message
        ).create();
    }
}
