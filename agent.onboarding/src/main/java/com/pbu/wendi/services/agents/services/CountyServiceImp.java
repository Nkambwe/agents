package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.County;
import com.pbu.wendi.repositories.agents.repos.CountyRepository;
import com.pbu.wendi.utils.requests.agents.dto.CountyRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CountyServiceImp implements CountyService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final CountyRepository counties;

    public CountyServiceImp(AppLoggerService logger, ModelMapper mapper, CountyRepository counties) {
        this.logger = logger;
        this.mapper = mapper;
        this.counties = counties;
    }

    @Override
    public CompletableFuture<Boolean> countyExists(long id) {
        logger.info("Check if county exists");
        try{
            return CompletableFuture.completedFuture(counties.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'countyExists' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByName(String countyName) {
        logger.info("Check if county exists");
        try{
            return CompletableFuture.completedFuture(counties.existsByCountyName(countyName));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByName' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String countyName, Long id) {
        logger.info(String.format("Retrieving County with name %s and agent Id %s", countyName, id));
        try{
            return CompletableFuture.completedFuture(counties.existsByCountyNameAndIdNot(countyName, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNameAndNotId' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<CountyRequest> findCountyById(long id) {
        logger.info(String.format("Retrieving County with ID %s", id));
        try{
            County record = counties.findById(id);
            if (record != null) {
                CountyRequest request = this.mapper.map(record, CountyRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findCountyById' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<CountyRequest> findCountyByName(String countyName) {
        logger.info(String.format("Retrieving County with name %s", countyName));
        try{
            County record = counties.findByCountyName(countyName);
            if (record != null) {
                CountyRequest request = this.mapper.map(record, CountyRequest.class);
                return CompletableFuture.completedFuture(request);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findCountyByName' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<CountyRequest>> getAllCounties() {
        logger.info("Retrieve all counties");
        List<CountyRequest> records  = new ArrayList<>();
        try{
            //..get counties records
            List<County> countyRecords = counties.findAll();
            if(!countyRecords.isEmpty()){
                for (County record : countyRecords) {
                    records.add(this.mapper.map(record, CountyRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllCounties' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<CountyRequest>> getActiveCounties() {
        logger.info("Retrieve all active counties");
        List<CountyRequest> records  = new ArrayList<>();
        try{
            //..get active counties records
            List<County> countyRecords = counties.findActiveCounties();
            if(!countyRecords.isEmpty()){
                for (County record : countyRecords) {
                    records.add(this.mapper.map(record, CountyRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveCounties' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<CountyRequest> create(CountyRequest county) throws InterruptedException {
        logger.info("Adding new county");
        try{
            County record = this.mapper.map(county, County.class);
            counties.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(county);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(CountyRequest county) {
        logger.info("update county");
        try{
            County record = this.mapper.map(county, County.class);
            record.setId(county.getId());
            counties.updateCounty(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in CountryService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete county");
        try{
            logger.info(String.format("County property 'is_deleted' value set to %s", deleted ? "true": "false"));
            counties.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete county");
        try{
            logger.info(String.format("Delete county with id %s", id));
            counties.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'CountryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
