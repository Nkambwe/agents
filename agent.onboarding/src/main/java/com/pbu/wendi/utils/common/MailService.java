package com.pbu.wendi.utils.common;

import org.springframework.stereotype.Service;

import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException ;



@Service
public class MailService {
    public static void sendMultipleEmail(String toAddress, List<String> ccAddresses, String body, String subject) {
        try {
            Properties props = new
                            Properties()
                    ;
            props.put(
                    "mail.smtp.host"
                    ,
                    "postbank-co-ug.mail.eo.outlook.com"
            )
            ;
            props.put(
                    "mail.smtp.port"
                    ,
                    "25"
            )
            ;
            props.put(
                    "mail.smtp.auth"
                    , true
            )
            ;
            props.put(
                    "mail.smtp.starttls.enable"
                    , true
            )
            ;
            Session session = Session.
                    getInstance
                            (props
                                    , new
                                            Authenticator() {
                                                protected
                                                PasswordAuthentication
                                                getPasswordAuthentication
                                                        () {
                                                    return new
                                                            PasswordAuthentication(
                                                            "ussdgateway@postbank.co.ug"
                                                            ,
                                                            ""
                                                    )
                                                            ;
                                                }
                                            })
                    ;

            Message message =
                    new
                            MimeMessage(session)
                    ;
            message.setFrom(
                    new
                            InternetAddress(
                            "ussdgateway@postbank.co.ug"
                            ,
                            "USSD Gateway Threat Detected"
                    ))
            ;
            message.setRecipient(Message.RecipientType.
                            TO
                    , new
                            InternetAddress(toAddress))
            ;

// Set CC recipients
            if
            (ccAddresses !=
                    null
                    && !ccAddresses.isEmpty()) {
                Address[] ccRecipients =
                        new
                                InternetAddress[ccAddresses.size()]
                        ;

                for
                (
                        int
                        i = 0
                        ;
                        i < ccAddresses.size()
                        ;
                        i++) {
                    ccRecipients[i] =
                            new
                                    InternetAddress(ccAddresses.get(i))
                    ;
                }
                message.setRecipients(Message.RecipientType.
                                CC
                        ,
                        ccRecipients)
                ;
            }
}
