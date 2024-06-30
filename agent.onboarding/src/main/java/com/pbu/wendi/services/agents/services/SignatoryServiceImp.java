package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.BusinessAgent;
import com.pbu.wendi.model.agents.models.IndividualAgent;
import com.pbu.wendi.model.agents.models.Signatory;
import com.pbu.wendi.repositories.agents.repos.BusinessRepository;
import com.pbu.wendi.repositories.agents.repos.IndividualRepository;
import com.pbu.wendi.repositories.agents.repos.SignatoryRepository;
import com.pbu.wendi.utils.requests.agents.dto.SignatoryRequest;
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
public class SignatoryServiceImp implements SignatoryService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final SignatoryRepository signatories;
    private final IndividualRepository persons;
    private final BusinessRepository businesses;

    public SignatoryServiceImp(AppLoggerService logger, ModelMapper mapper,
                               SignatoryRepository signatories,
                               IndividualRepository persons, BusinessRepository businesses) {
        this.logger = logger;
        this.mapper = mapper;
        this.signatories = signatories;
        this.persons = persons;
        this.businesses = businesses;
    }

    @Override
    public boolean exists(long id) {
        logger.info(String.format("Checking whether signatory with ID '%s' exists",id));
        try{
            return signatories.existsById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean nameInUse(String name) {
        logger.info(String.format("Checking whether signatory with name '%s' exists",name));
        try{
            return signatories.existsByFullName(name);
        } catch(Exception ex){
            logger.info("An error occurred in method 'nameInUse'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean duplicatedSignatoryName(String name, long agentId){
        logger.info(String.format("Checking whether signatory name '%s' is assigned to another ID signatory on the same agent '%s' ",name, agentId));
        try{
            return signatories.existsByFullNameAndPersonIdOrBusinessId(name, agentId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatedName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean duplicatedName(String name, long id) {
        logger.info(String.format("Checking whether signatory name '%s' is assigned to another ID signatory on the same agent '%s' ",name, id));
        try{
            return signatories.existsByFullNameAndIdNot(name, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatedName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SignatoryRequest> findById(Long id) {
        logger.info(String.format("Retrieve signatory with ID '%s'", id));
        try{

            SignatoryRequest signatory = null;
            Optional<Signatory> record = signatories.findById(id);
            if (record.isPresent()) {
                signatory = this.mapper.map(record.get(), SignatoryRequest.class);
            }

            return CompletableFuture.completedFuture(signatory);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'SignatoryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<SignatoryRequest>> findByBusinessId(Long businessId) {
        logger.info("Retrieve business agent signatories");
        List<SignatoryRequest> records  = new ArrayList<>();
        try{

            List<Signatory> bRecords = signatories.findByBusinessId(businessId);
            if (!bRecords.isEmpty()) {
                for (Signatory record : bRecords) {
                    records.add(this.mapper.map(record, SignatoryRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByBusinessId' in 'SignatoryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<SignatoryRequest>> findByPersonId(Long personId) {
        logger.info("Retrieve individual agent signatories");
        List<SignatoryRequest> records  = new ArrayList<>();
        try{

            List<Signatory> iRecords = signatories.findByPersonId(personId);
            if (!iRecords.isEmpty()) {
                for (Signatory record : iRecords) {
                    records.add(this.mapper.map(record, SignatoryRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByPersonId' in 'SignatoryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete signatory");
        try{
            logger.info(String.format("Signatory is_deleted value set to %s", deleted ? "true": "false"));
            signatories.softDelete(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'SignatoryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public CompletableFuture<SignatoryRequest> createSignatory(SignatoryRequest signatory) {
        logger.info("Adding new signatory");
        try{
            IndividualAgent iAgent = null;
            Optional<IndividualAgent> person = persons.findById(signatory.getAgentId());
            if (person.isPresent()) {
                iAgent = person.get();
            }

            BusinessAgent bAgent = null;
            if(iAgent == null){
                Optional<BusinessAgent> business = businesses.findById(signatory.getAgentId());
                if (business.isPresent()) {
                    bAgent = business.get();
                }
            }

            if(iAgent == null && bAgent == null){
                logger.info("An agent id does not exist either for individual or business agent");
                throw new GeneralException("An agent id does not exist either for individual or business agent");
            }

            Signatory record = this.mapper.map(signatory, Signatory.class);
            if (iAgent != null) {
                record.setPerson(iAgent);
                signatories.updateSignatoryWithPerson(record);
            } else {
                record.setBusiness(bAgent);
                signatories.updateSignatoryWithBusiness(record);
            }

            signatories.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(signatory);
        }catch(Exception ex){
            logger.info("An error occurred in method 'createSignatory' in 'SignatoryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void updateSignatory(SignatoryRequest signatory) {
        logger.info("update signatory record");
        try{

            IndividualAgent iAgent = null;
            Optional<IndividualAgent> person = persons.findById(signatory.getAgentId());
            if (person.isPresent()) {
                iAgent = person.get();
            }

            BusinessAgent bAgent = null;
            if(iAgent == null){
                Optional<BusinessAgent> business = businesses.findById(signatory.getAgentId());
                if (business.isPresent()) {
                    bAgent = business.get();
                }
            }

            if(iAgent == null && bAgent == null){
                logger.info("An agent id does not exist either for individual or business agent");
                throw new GeneralException("An agent id does not exist either for individual or business agent");
            }

            Signatory record = this.mapper.map(signatory, Signatory.class);
            if (iAgent != null) {
                record.setPerson(iAgent);
                signatories.updateSignatoryWithPerson(record);
            } else {
                record.setBusiness(bAgent);
                signatories.updateSignatoryWithBusiness(record);
            }

        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in SignatoryService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Complete delete signatory record");
        try{
            signatories.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'SignatoryService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}

