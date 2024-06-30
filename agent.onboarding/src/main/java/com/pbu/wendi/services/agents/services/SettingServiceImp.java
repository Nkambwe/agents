package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.AgentSettings;
import com.pbu.wendi.repositories.agents.repos.SettingRepository;
import com.pbu.wendi.utils.requests.agents.dto.SettingsRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class SettingServiceImp implements SettingService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final SettingRepository settings;

    public SettingServiceImp(AppLoggerService logger, ModelMapper mapper, SettingRepository settings) {
        this.logger = logger;
        this.mapper = mapper;
        this.settings = settings;
    }

    @Override
    public CompletableFuture<Boolean> existsById(long id) {
        logger.info("Check whether setting exists");
        try{
            return CompletableFuture.completedFuture(settings.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsById' in 'SettingService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByParamName(String paramName) {
        logger.info("Check whether setting exists");
        try{
            return CompletableFuture.completedFuture(settings.existsByParamName(paramName));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByParamName' in 'SettingService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SettingsRequest> findByParamName(String paramName) {
        logger.info(String.format("Retrieving settings with name %s", paramName));
        try{
            Optional<AgentSettings> record = settings.findByParamName(paramName);
            if (record.isPresent()) {
                SettingsRequest request = this.mapper.map(record.get(), SettingsRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByParamName' in 'SettingService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<SettingsRequest>> findAllByParamNames(List<String> paramNames){
        logger.info("Retrieving settings where paramName is in list 'paramNames'");
        try{
            List<AgentSettings> records = settings.findAllWhereParamNameIn(paramNames);
            if (records.isEmpty()) {
                return CompletableFuture.completedFuture(new ArrayList<>());
            }

            List<SettingsRequest> settings = new ArrayList<>();
            for (AgentSettings setting : records){
                SettingsRequest request = this.mapper.map(setting, SettingsRequest.class);
                settings.add(request);
            }

            return CompletableFuture.completedFuture(settings);

        } catch(Exception ex){
            logger.info("An error occurred in method 'findAllByParamNames' in 'SettingService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SettingsRequest> create(SettingsRequest setting) throws InterruptedException {
        logger.info("Adding new settings");
        try{
            AgentSettings record = this.mapper.map(setting, AgentSettings.class);
            settings.save(record);
            setting.setId(record.getId());
            return CompletableFuture.completedFuture(setting);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'SettingService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void updateSettings(SettingsRequest setting) {
        logger.info("update agent partner");
        try{
            AgentSettings record = this.mapper.map(setting, AgentSettings.class);
            record.setId(setting.getId());
            settings.updateSettings(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateSettings' in SettingService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean is_deleted) {
        logger.info("Soft Delete Settings");
        try{
            logger.info(String.format("Settings isDeleted value set to %s", is_deleted ? "true": "false"));
            settings.softDelete(id, is_deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'SettingsService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void deleteById(long id) {
        logger.info("Delete Settings");
        try{
            logger.info(String.format("Delete Settings with id %s", id));
            settings.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'SettingService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
