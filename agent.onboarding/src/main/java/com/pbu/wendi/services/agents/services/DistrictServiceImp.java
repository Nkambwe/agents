package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.District;
import com.pbu.wendi.repositories.agents.repos.DistrictRepository;
import com.pbu.wendi.requests.agents.dto.DistrictRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DistrictServiceImp implements DistrictService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final DistrictRepository districts;

    public DistrictServiceImp(AppLoggerService logger, ModelMapper mapper, DistrictRepository districts) {
        this.logger = logger;
        this.mapper = mapper;
        this.districts = districts;
    }

    @Override
    public CompletableFuture<Boolean> districtExists(long id) {
        logger.info("Check if district exists");
        try{
            return CompletableFuture.completedFuture(districts.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'districtExists' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByName(String name) {
        logger.info("Check if district exists");
        try{
            return CompletableFuture.completedFuture(districts.existsByDistrictName(name));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByName' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String name, Long id) {
        logger.info(String.format("Retrieving district with name %s and agent Id %s", name, id));
        try{
            return CompletableFuture.completedFuture(districts.existsByDistrictNameAndIdNot(name, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNameAndNotId' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<DistrictRequest> findDistrictById(long id) {
        logger.info(String.format("Retrieving district with ID %s", id));
        try{

            District record = districts.findById(id);
            if (record != null) {
                DistrictRequest request = this.mapper.map(record, DistrictRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findDistrictById' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<DistrictRequest> findDistrictByName(String name) {
        logger.info(String.format("Retrieving district with name %s", name));
        try{
            District record = districts.findByDistrictName(name);
            if (record != null) {
                DistrictRequest request = this.mapper.map(record, DistrictRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findDistrictByName' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<DistrictRequest>> getAllDistricts() {
        logger.info("Retrieve all districts");
        List<DistrictRequest> records  = new ArrayList<>();
        try{
            //..get districts records
            List<District> districtRecords = districts.findAll();
            if(!districtRecords.isEmpty()){
                for (District record : districtRecords) {
                    records.add(this.mapper.map(record, DistrictRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllDistricts' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<DistrictRequest>> getActiveDistricts() {
        logger.info("Retrieve active districts");
        List<DistrictRequest> records  = new ArrayList<>();
        try{
            //..get district records
            List<District> districtRecords = districts.findActiveDistricts();
            if(!districtRecords.isEmpty()){
                for (District record : districtRecords) {
                    records.add(this.mapper.map(record, DistrictRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveDistricts' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<DistrictRequest> create(DistrictRequest district) throws InterruptedException {
        logger.info("Adding new district");
        try{
            District record = this.mapper.map(district, District.class);
            districts.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(district);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(DistrictRequest district) {
        logger.info("update district");
        try{
            District record = this.mapper.map(district, District.class);
            record.setId(district.getId());
            districts.updateDistrict(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in DistrictService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete district");
        try{
            logger.info(String.format("District is_deleted value set to %s", deleted ? "true": "false"));
            districts.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete district");
        try{
            logger.info(String.format("Delete district with id %s", id));
            districts.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'DistrictService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
