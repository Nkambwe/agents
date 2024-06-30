package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.Parish;
import com.pbu.wendi.repositories.agents.repos.ParishRepository;
import com.pbu.wendi.requests.agents.dto.ParishRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ParishServiceImp implements ParishService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final ParishRepository parishes;

    public ParishServiceImp(AppLoggerService logger, ModelMapper mapper, ParishRepository parishes) {
        this.logger = logger;
        this.mapper = mapper;
        this.parishes = parishes;
    }

    @Override
    public CompletableFuture<Boolean> parishExists(long id) {
        logger.info("Check if parish exists");
        try{
            return CompletableFuture.completedFuture(parishes.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'parishExists' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByName(String parishName) {
        logger.info("Check if parish exists");
        try{
            return CompletableFuture.completedFuture(parishes.existsByParishName(parishName));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByName' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String parishName, Long id) {
        logger.info(String.format("Retrieving parish with name %s and parish ID %s", parishName, id));
        try{
            return CompletableFuture.completedFuture(parishes.existsByParishNameAndIdNot(parishName, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNameAndNotId' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ParishRequest> findParishById(long id) {
        logger.info(String.format("Retrieving parish with ID %s", id));
        try {
            Parish record = parishes.findById(id);
            if (record != null) {
                ParishRequest request = this.mapper.map(record, ParishRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findParishById' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ParishRequest> findParishByName(String parishName) {
        logger.info(String.format("Retrieving parish with name %s", parishName));
        try{
            Parish record = parishes.findByParishName(parishName);
            if (record != null) {
                ParishRequest request = this.mapper.map(record, ParishRequest.class);
                return CompletableFuture.completedFuture(request);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findParishByName' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<ParishRequest>> getAllParishes() {
        logger.info("Retrieve all parishes");
        List<ParishRequest> records  = new ArrayList<>();
        try{
            //..get parishes records
            List<Parish> parishRecords = parishes.findAll();
            if(!parishRecords.isEmpty()){
                for (Parish record : parishRecords) {
                    records.add(this.mapper.map(record, ParishRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllParishes' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<ParishRequest>> getActiveParishes() {
        logger.info("Retrieve all active parishes");
        List<ParishRequest> records  = new ArrayList<>();
        try{
            //..get active parishes records
            List<Parish> parishRecords = parishes.findActiveParish();
            if(!parishRecords.isEmpty()){
                for (Parish record : parishRecords) {
                    records.add(this.mapper.map(record, ParishRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveParishes' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ParishRequest> create(ParishRequest parish) throws InterruptedException {
        logger.info("Adding new parish");
        try{
            Parish record = this.mapper.map(parish, Parish.class);
            parishes.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(parish);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(ParishRequest parish) {
        logger.info("update parish");
        try{
            Parish record = this.mapper.map(parish, Parish.class);
            record.setId(parish.getId());
            parishes.updateParish(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in ParishService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete parish");
        try{
            logger.info(String.format("Parish property 'is_deleted' value set to %s", deleted ? "true": "false"));
            parishes.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete parish");
        try{
            logger.info(String.format("Delete parish with id %s", id));
            parishes.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'ParishService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
