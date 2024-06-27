package com.pbu.wendi.utils.helpers;

import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.common.Secure;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Generators {
    /*Generate the string equivalent of the current date*/
    public static String currentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    /*Convert date to string*/
    public static String dateToString(LocalDateTime dateTime) {
        // Define the desired date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the date and time as a string
        return dateTime.format(formatter);
    }
    public static String getHashedPassword(String password, AppLoggerService logger) {
        logger.info("Hashing password...");
        String hashedPassword = null;
        try {
            logger.info(String.format("Original Password is '%s'", password));
            hashedPassword = Secure.hashPassword(password, Literals.SALT.getBytes());
            logger.info(String.format("Hashed Password is '%s'", hashedPassword));
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("NoSuchAlgorithmException Occurred. Message '%s'", e.getMessage()));
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(e);
            logger.stackTrace(stackTrace);
        } catch (InvalidKeySpecException e) {
            RuntimeException ex = new RuntimeException(e);
            logger.error(String.format("InvalidKeySpecException Occurred. Message '%s'", ex.getMessage()));
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(e);
            logger.stackTrace(stackTrace);
        }

        return hashedPassword;
    }

    /*
     * Check if two passwords match
     * @param password - user password
     * @param hashedPassword - encrypted password
     * */
    public static boolean isPasswordMatch(String password, String hashedPassword, AppLoggerService logger){
        logger.info("Verifying password...");
        boolean isMatched = false;
        try {
            logger.info(String.format("Original Password :: '%s'", password));
            logger.info(String.format("Hashed Password is '%s'", hashedPassword));
            isMatched = Secure.verifyPassword(password, hashedPassword, Literals.SALT.getBytes());
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("NoSuchAlgorithmException Occurred. Message '%s'", e.getMessage()));
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(e);
            logger.stackTrace(stackTrace);
        } catch (InvalidKeySpecException e) {
            RuntimeException ex = new RuntimeException(e);
            logger.error(String.format("InvalidKeySpecException Occurred. Message '%s'", ex.getMessage()));
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(e);
            logger.stackTrace(stackTrace);
        }
        return isMatched;
    }

    public static String generatePinNumber() {
        SecureRandom secureRandom = new SecureRandom();

        // Generates a number between 0 and 999999
        int pin = secureRandom.nextInt(1000000);
        return String.format("%06d", pin);
    }

    public static String resizeCode(long code) {
        String codeStr = String.valueOf(code);
        if (codeStr.length() > 4) {
            // Truncate to the last 4 digits
            codeStr = codeStr.substring(codeStr.length() - 4);
        } else if (codeStr.length() < 4) {
            // Pad with leading zeros to make it 4 digits
            codeStr = String.format("%04d", code);
        }
        return codeStr;
    }

    /*Generate error message from BindingResult object*/
    public static String buildErrorMessage(BindingResult bindingResult){
        List<FieldError> fields = bindingResult.getFieldErrors();
        StringBuilder message = new StringBuilder("Validation failed for fields: ");

        for (FieldError field : fields) {
            message.append(field.getField())
                    .append(" - ")
                    .append(field.getDefaultMessage())
                    .append(", ");
        }

        // Remove the trailing comma and space
        message.setLength(message.length() - 2);

        return message.toString();
    }
}

