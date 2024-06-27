package com.pbu.wendi.utils.common;

import com.pbu.wendi.utils.helpers.Generators;
import com.pbu.wendi.utils.helpers.Literals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
/**
 * Class Name :Secure
 * Created By : Nkambwe Mark
 * Description:Class handles application logging
 **/

@Service
public class AppLoggerService {
    private String logId;
    private final Logger logger = LoggerFactory.getLogger(AppLoggerService.class);
    private static final String LETTERS = Literals.LETTERS;
    private static final String NUMBERS = Literals.NUMBERS;

    /*Get log Id*/
    public String getLogId() {
        return logId;
    }

    /*Set log Id*/
    public void setLogId(String logId) {
        String id = null;
        if(logId != null){
            id = logId.replaceAll("/","_");
        }

        this.logId = id;
    }

    /*
     *Method logs a message provided as an argument
     * @message - Message to log
     */
    public void log(String message) {
        logger.info(message);
    }

    /*
     *Method logs a error message provided as an argument
     * @message - Error message to log
     */
    public void error(String message){
        String result;
        String date = Generators.currentDate();
        if(getLogId() != null){
            result = String.format("%s\t%s\t%s",date,getLogId(),message);
        } else{
            result = String.format("%s\t%s",date,message);
        }
        this.logger.error(result);
    }

    /*
     *Method logs message info provided as an argument
     * @message - Message info to log
     */
    public void info(String message){
        String result;
        String date = Generators.currentDate();
        if(getLogId()!=null){
            result = String.format("%s\t%s\t%s",date,getLogId(),message);
        }else {
            String r = transactionRef();
            setLogId(r);
            result = String.format("%s\tRNDM%s\t%s",date,r,message);
        }
        this.logger.info(result);
    }

    public void debug(String message){
        String result;
        String date = Generators.currentDate();
        if(getLogId()!=null){
            result = String.format("%s\t%s\t%s",date,getLogId(),message);
        }else {
            String r = transactionRef();
            setLogId(r);
            result = String.format("%s\tRNDM%s\t%s",date,r,message);
        }

        this.logger.debug(result);
    }

    /*
     * Method logs stacktrace
     * @message - Stacktrace message
     */
    public void stackTrace(String message){

        String result;
        String date = Generators.currentDate();
        if(getLogId() != null){
            result = String.format("%s\t%s\t%s",date,getLogId(),message);
        } else{
            result = String.format("%s\t%s",date,message);
        }

        this.logger.trace(result);
    }

    /*Method generate random transaction reference number*/
    public static String transactionRef() {
        String characters = String.format("%s%s",LETTERS,NUMBERS);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

}

