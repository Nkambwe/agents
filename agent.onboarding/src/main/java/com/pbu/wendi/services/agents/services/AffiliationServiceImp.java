package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.Affiliation;
import com.pbu.wendi.repositories.agents.repos.AffiliationRepository;
import com.pbu.wendi.utils.requests.agents.dto.AffiliationRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AffiliationServiceImp implements AffiliationService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final AffiliationRepository repository;

    public AffiliationServiceImp(AppLoggerService logger, ModelMapper mapper, AffiliationRepository repository) {
        this.logger = logger;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Boolean> affiliationExists(long id) {
        logger.info(String.format("Checking whether affiliation organization with id '%s' exists",id));
        try{
            return CompletableFuture.completedFuture(repository.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'affiliationExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkAffiliationDuplicateName(String name) {
        logger.info(String.format("Checking whether another affiliation organization with name '%s' exists",name));
        try{
            return repository.existsByOrgName(name);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkAffiliationDuplicateName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkAffiliationDuplicateNameWithDifferentIds(String name, long id) {
        logger.info(String.format("Checking whether another affiliation organization with name '%s' but not id '%s' exists",name, id));
        try{
            return repository.existsByOrgNameAndIdNot(name, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkAffiliationDuplicateNameWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkAffiliationDuplicateSortCode(String sort_code) {
        logger.info(String.format("Checking whether another affiliation organization with sort code '%s' exists",sort_code));
        try{
            return repository.existsBySortCode(sort_code);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkAffiliationDuplicateSortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkAffiliationDuplicateSortCodeWithDifferentIds(String sort_code, long id) {
        logger.info(String.format("Checking whether another affiliation organization with sort code '%s' but not id '%s' exists",sort_code, id));
        try{
            return repository.existsBySortCodeAndIdNot(sort_code, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkAffiliationDuplicateSortCodeWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<AffiliationRequest> findAffiliationById(long id) {
        logger.info(String.format("Retrieving affiliation with ID %s", id));
        try{

            Affiliation affiliation = repository.findById(id);
            if(affiliation != null){
                AffiliationRequest record = this.mapper.map(affiliation, AffiliationRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAffiliationById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<AffiliationRequest> findAffiliationBySortCode(String sort_code) {
        logger.info(String.format("Retrieving affiliation with sort code %s", sort_code));
        try{

            Affiliation affiliation = repository.findBySortCode(sort_code);
            if(affiliation != null){
                AffiliationRequest record = this.mapper.map(affiliation, AffiliationRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAffiliationBySortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<AffiliationRequest>> getAffiliations() {
        logger.info("Retrieving all affiliations");
        List<AffiliationRequest> records  = new ArrayList<>();
        try{
            List<Affiliation> affiliations = repository.findAll();
            if(!affiliations.isEmpty()){
                for (Affiliation record : affiliations) {
                    records.add(this.mapper.map(record, AffiliationRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAffiliations'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<AffiliationRequest>> getActiveAffiliations() {
        logger.info("Retrieving all active affiliations");
        List<AffiliationRequest> activeRecords  = new ArrayList<>();
        try{
            List<Affiliation> affiliations = repository.findActiveAffiliations();
            if(!affiliations.isEmpty()){
                for (Affiliation record : affiliations) {
                    activeRecords.add(this.mapper.map(record, AffiliationRequest.class));
                }
            }

            return CompletableFuture.completedFuture(activeRecords);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveAffiliations'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<AffiliationRequest> createAffiliation(AffiliationRequest affiliation) {
        logger.info("Create new affiliation organization");
        try{
            //map record
            Affiliation record = this.mapper.map(affiliation, Affiliation.class);

            //save record
            repository.save(record);

            //set identification type id
            affiliation.setId(record.getId());
            return CompletableFuture.completedFuture(affiliation);
        } catch(Exception ex){
            logger.info("An error occurred in method 'createAffiliation'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public void updateAffiliation(AffiliationRequest affiliation) {
        logger.info("update affiliation record");
        try{
            repository.updateAffiliation(this.mapper.map(affiliation, Affiliation.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateAffiliation'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDeleteAffiliation(long id, boolean is_deleted) {
        logger.info("Soft delete affiliation");
        try{
            logger.info(String.format("Affiliation property 'is_deleted' value is set to %s", is_deleted ? "true": "false"));
            repository.markAsDeleted(id, is_deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteAffiliation'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void deleteAffiliation(long id) {
        logger.info("Delete affiliation record");
        try{
            logger.info(String.format("Affiliation with id %s is being deleted", id));
            repository.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteAffiliation'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}

