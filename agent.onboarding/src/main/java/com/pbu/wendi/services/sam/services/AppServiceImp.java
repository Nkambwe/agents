package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.App;
import com.pbu.wendi.repositories.sam.repos.AppRepository;
import com.pbu.wendi.utils.requests.sam.dto.AppRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AppServiceImp implements AppService {
    private final ModelMapper mapper;
    private final AppRepository appRepo;
    private final AppLoggerService logger;

    public AppServiceImp(AppLoggerService logger, ModelMapper mapper, AppRepository appRepo) {
        this.mapper = mapper;
        this.appRepo = appRepo;
        this.logger = logger;
    }

    @Override
    public CompletableFuture<AppRequest> findById(long id) {
        logger.info(String.format("Retrieving affiliation with ID %s", id));
        try{

            App app = appRepo.findById(id);
            if(app != null){
                AppRequest record = this.mapper.map(app, AppRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'AppService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<AppRequest> findByName(String name) {
        logger.info(String.format("Retrieving affiliation with name %s", name));
        try{
            App app = appRepo.findByName(name);
            if(app != null){
                AppRequest record = this.mapper.map(app, AppRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByName' in 'AppService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<AppRequest>> findAll(long owner_id) {
        logger.info("Retrieving all affiliations");
        List<AppRequest> records  = new ArrayList<>();
        try{
            List<App> apps = appRepo.findAllByUsersId(owner_id);
            if(!apps.isEmpty()){
                for (App record : apps) {
                    records.add(this.mapper.map(record, AppRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll' in 'AppService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<AppRequest> create(AppRequest app) {
        try{
            App record = this.mapper.map(app, App.class);
            appRepo.save(record);
            app.setId(record.getId());
            return CompletableFuture.completedFuture(app);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'AppService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void updateApp(AppRequest app) {
        logger.info("update app record");
        try{
            App record = this.mapper.map(app, App.class);
            appRepo.update(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateApp' in 'AppService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean isDeleted) {
        logger.info("Soft delete app record");
        try{
            logger.info(String.format("app property 'is_deleted' value is set to %s", isDeleted ? "true": "false"));
            appRepo.markAsDeleted(id, isDeleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'AppService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}

