package com.pbu.wendi.utils.common;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException ;



@Service
public class MailService {
    public void sendMultipleEmail(String toAddress, List<String> ccAddresses, String body, String subject, AppLoggerService logger) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "postbank-co-ug.mail.eo.outlook.com");
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", true);
            props.put("mail.smtp.starttls.enable", true);
            Session session = Session.getInstance(props, new Authenticator() {protected jakarta.mail.PasswordAuthentication
                   getPasswordAuthentication() {
                      return new jakarta.mail.PasswordAuthentication("agentonboarding@postbank.co.ug", "");
                   }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ussdgateway@postbank.co.ug", "USSD Gateway Threat Detected"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

            // Set CC recipients
            if(ccAddresses != null && !ccAddresses.isEmpty()) {
                Address[] ccRecipients = new InternetAddress[ccAddresses.size()];
                for (int i = 0; i < ccAddresses.size(); i++) {
                    ccRecipients[i] = new InternetAddress(ccAddresses.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, ccRecipients);
            }
            message.setSubject(subject);
            message.setContent(body, "text/html");
            Transport.send(message);
        } catch (MessagingException mex) {
            logger.info("MAIL SENDER:: Reset password mail not set. An error occurred during mail sending");
            logger.stackTrace(Arrays.toString(mex.getStackTrace()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
