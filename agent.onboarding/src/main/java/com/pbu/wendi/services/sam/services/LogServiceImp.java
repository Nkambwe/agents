package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.SystemLog;
import com.pbu.wendi.repositories.sam.repos.LogRepository;
import com.pbu.wendi.utils.requests.sam.dto.LogRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class LogServiceImp implements LogService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final LogRepository logs;


    public LogServiceImp(AppLoggerService logger, ModelMapper mapper, LogRepository logs) {
        this.mapper = mapper;
        this.logs = logs;
        this.logger = logger;
    }

    @Override
    public CompletableFuture<List<LogRequest>> findAll() {
        logger.info("Retrieving all logs");
        List<LogRequest> records  = new ArrayList<>();
        try{
            List<SystemLog> myLogs = logs.findTop300();
            if(!myLogs.isEmpty()){
                for (SystemLog record : myLogs) {
                    records.add(this.mapper.map(record, LogRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<LogRequest>> findAll(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Retrieving all logs");
        List<LogRequest> records  = new ArrayList<>();
        try{
            List<SystemLog> myLogs = logs.findTop300(startDate, endDate);
            if(!myLogs.isEmpty()){
                for (SystemLog record : myLogs) {
                    records.add(this.mapper.map(record, LogRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }


    @Override
    public CompletableFuture<LogRequest> create(LogRequest log) {
        logger.info("Create new log record");
        try{
            //map record
            SystemLog record = this.mapper.map(log, SystemLog.class);

            //save record
            logs.save(record);

            //set log id
            log.setId(record.getId());
            return CompletableFuture.completedFuture(log);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
