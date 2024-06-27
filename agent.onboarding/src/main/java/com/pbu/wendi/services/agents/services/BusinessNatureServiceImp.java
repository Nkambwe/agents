package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.BusinessNature;
import com.pbu.wendi.repositories.agents.repos.BusinessNatureRepository;
import com.pbu.wendi.requests.agents.dto.BusinessNatureRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BusinessNatureServiceImp implements BusinessNatureService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final BusinessNatureRepository businessNatures;

    public BusinessNatureServiceImp(AppLoggerService logger, ModelMapper mapper, BusinessNatureRepository businessNatures) {
        this.logger = logger;
        this.mapper = mapper;
        this.businessNatures = businessNatures;
    }

    @Override
    public CompletableFuture<Boolean> businessNatureExists(long id) {
        logger.info("Check if Business nature exists");
        try{
            return CompletableFuture.completedFuture(businessNatures.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'businessNatureExists' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNature(String nature) {
        logger.info("Check if Business nature exists");
        try{
            return CompletableFuture.completedFuture(businessNatures.existsByNature(nature));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNature' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNatureAndNotId(String nature, Long id) {
        logger.info(String.format("Retrieving Business nature with 'Nature' %s and BusinessNature ID %s", nature, id));
        try{
            return CompletableFuture.completedFuture(businessNatures.existsByNatureAndIdNot(nature, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNatureAndNotId' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BusinessNatureRequest> findBusinessNatureById(long id) {
        logger.info(String.format("Retrieving Business nature with ID %s", id));
        try {
            BusinessNature record = businessNatures.findById(id);
            if (record != null) {
                BusinessNatureRequest request = this.mapper.map(record, BusinessNatureRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBusinessNatureById' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BusinessNatureRequest> findBusinessNatureByNature(String nature) {
        logger.info(String.format("Retrieving Business nature with name %s", nature));
        try{
            BusinessNature record = businessNatures.findByNature(nature);
            if (record != null) {
                BusinessNatureRequest request = this.mapper.map(record, BusinessNatureRequest.class);
                return CompletableFuture.completedFuture(request);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBusinessNatureByNature' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<BusinessNatureRequest>> getAllBusinessNatures() {
        logger.info("Retrieve all Business nature");
        List<BusinessNatureRequest> records  = new ArrayList<>();
        try{
            //..get Business nature records
            List<BusinessNature> natureRecords = businessNatures.findAll();
            if(!natureRecords.isEmpty()){
                for (BusinessNature record : natureRecords) {
                    records.add(this.mapper.map(record, BusinessNatureRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllBusinessNatures' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<BusinessNatureRequest>> getActiveBusinessNatures() {
        logger.info("Retrieve all active Business nature");
        List<BusinessNatureRequest> records  = new ArrayList<>();
        try{
            //..get active Business nature records
            List<BusinessNature> natureRecords = businessNatures.findActiveBusinessNatures();
            if(!natureRecords.isEmpty()){
                for (BusinessNature record : natureRecords) {
                    records.add(this.mapper.map(record, BusinessNatureRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveBusinessNatures' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BusinessNatureRequest> create(BusinessNatureRequest biz_nature) throws InterruptedException {
        logger.info("Adding new Business nature");
        try{
            BusinessNature record = this.mapper.map(biz_nature, BusinessNature.class);
            businessNatures.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(biz_nature);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(BusinessNatureRequest biz_nature) {
        logger.info("update Business nature");
        try{
            BusinessNature record = this.mapper.map(biz_nature, BusinessNature.class);
            record.setId(biz_nature.getId());
            businessNatures.updateBusinessNatures(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in BusinessNatureService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete business nature");
        try{
            logger.info(String.format("Business nature property 'is_deleted' value set to %s", deleted ? "true": "false"));
            businessNatures.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete business nature");
        try{
            logger.info(String.format("Delete business nature with id %s", id));
            businessNatures.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'BusinessNatureService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
