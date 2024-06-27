package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.Outlet;
import com.pbu.wendi.repositories.agents.repos.OutletRepository;
import com.pbu.wendi.requests.agents.dto.OutletRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OutletServiceImp implements OutletService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final OutletRepository outlets;

    public OutletServiceImp(AppLoggerService logger, ModelMapper mapper, OutletRepository outlets) {
        this.logger = logger;
        this.mapper = mapper;
        this.outlets = outlets;
    }

    @Override
    public CompletableFuture<Boolean> outletExists(long id) {
        logger.info("Check if outlet exists");
        try{
            return CompletableFuture.completedFuture(outlets.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'outletExists' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByName(String name) {
        logger.info("Check if outlet exists");
        try{
            return CompletableFuture.completedFuture(outlets.existsByOutletName(name));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByName' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String name, Long id) {
        logger.info(String.format("Retrieving outlet with name %s and agent Id %s", name, id));
        try{
            return CompletableFuture.completedFuture(outlets.existsByOutletNameAndIdNot(name, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNameAndNotId' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<OutletRequest> findOutletsById(long id) {
        logger.info(String.format("Retrieving outlet with ID %s", id));
        try{

            Outlet record = outlets.findById(id);
            if (record != null) {
                OutletRequest request = this.mapper.map(record, OutletRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findOutletsById' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<OutletRequest> findOutletsByName(String name) {
        logger.info(String.format("Retrieving telecom with name %s", name));
        try{

            Outlet record = outlets.findByOutletName(name);
            if (record != null) {
                OutletRequest request = this.mapper.map(record, OutletRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findOutletsByName' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<OutletRequest>> getAllOutlets() {
        logger.info("Retrieve all telecoms");
        List<OutletRequest> records  = new ArrayList<>();
        try{
            //..get Outlet records
            List<Outlet>telecomRecords = outlets.findAll();
            if(!telecomRecords.isEmpty()){
                for (Outlet record : telecomRecords) {
                    records.add(this.mapper.map(record, OutletRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllOutlets' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<OutletRequest>> getActiveOutlets() {
        logger.info("Retrieve active telecoms");
        List<OutletRequest> records  = new ArrayList<>();
        try{
            //..get telecom records
            List<Outlet>telecomRecords = outlets.findActiveOutlets();
            if(!telecomRecords.isEmpty()){
                for (Outlet record : telecomRecords) {
                    records.add(this.mapper.map(record, OutletRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveOutlets' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<OutletRequest> create(OutletRequest outlet) throws InterruptedException {
        logger.info("Adding new telecom");
        try{
            Outlet record = this.mapper.map(outlet, Outlet.class);
            outlets.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(outlet);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(OutletRequest outlet) {
        logger.info("update telecom");
        try{
            Outlet record = this.mapper.map(outlet, Outlet.class);
            record.setId(outlet.getId());
            outlets.updateTelecom(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in OutletService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete telecom");
        try{
            logger.info(String.format("Telecom is_deleted value set to %s", deleted ? "true": "false"));
            outlets.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete outlet");
        try{
            logger.info(String.format("Delete outlet with id %s", id));
            outlets.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'OutletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
