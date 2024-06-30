package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.BusinessAgent;
import com.pbu.wendi.model.agents.models.IndividualAgent;
import com.pbu.wendi.model.agents.models.Operator;
import com.pbu.wendi.repositories.agents.repos.OperatorRepository;
import com.pbu.wendi.requests.agents.dto.BusinessRequest;
import com.pbu.wendi.requests.agents.dto.IndividualRequest;
import com.pbu.wendi.requests.agents.dto.OperatorRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OperatorServiceImp implements OperatorService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final OperatorRepository operators;

    public OperatorServiceImp(AppLoggerService logger, ModelMapper mapper, OperatorRepository operators) {
        this.logger = logger;
        this.mapper = mapper;
        this.operators = operators;
    }

    @Override
    public boolean exists(long id) {
        logger.info(String.format("Checking whether operator with ID '%s' exists",id));
        try{
            return operators.existsById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public  boolean duplicatedName(String name, long agentId, long id) {
        logger.info(String.format("Checking whether operator name '%s' is assigned to another ID operator on the same agent '%s' ",name, agentId));
        try{
            boolean exists = operators.existsByOperatorNameAndBusinessIdAndIdNot(name, agentId, id);
            if(!exists){
                exists = operators.existsByOperatorNameAndPersonIdAndIdNot(name, agentId, id);
            }

            return exists;
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatedName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean duplicatedNIN(String nin) {
        logger.info(String.format("Checking whether operator INI '%s' is already in use' ",nin));
        try{
            return operators.existsByIdNin(nin);
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatedNIN'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean duplicatedNIN(String nin, long recordId) {
        logger.info(String.format("Checking whether operator INI '%s' is already in use' ",nin));
        try{
            return operators.existsByIdNinAndIdNot(nin, recordId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatedNIN'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> findByOperatorNameAndBusinessId(String operatorName, Long agentId) {
        logger.info("Check whether business operator exists");
        try{
            return CompletableFuture.completedFuture(operators.existsByOperatorNameAndBusiness_Id(operatorName, agentId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByOperatorNameAndBusinessId' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> findByOperatorNameAndPersonId(String operatorName, Long agentId) {
        logger.info("Check whether individual operator exists");
        try{
            return CompletableFuture.completedFuture(operators.existsByOperatorNameAndPerson_Id(operatorName, agentId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByOperatorNameAndPersonId' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<OperatorRequest>> findIndividualOperators(Long personId) {
        logger.info("Retrieve individual agent operators");
        List<OperatorRequest> records  = new ArrayList<>();
        try{
            //..get operator records
            List<Operator> o_records = operators.findByPerson_Id(personId);
            if(!o_records.isEmpty()){
                for (Operator record : o_records) {
                    records.add(this.mapper.map(record, OperatorRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findIndividualOperators' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<OperatorRequest>> findBusinessOperators(Long businessId) {
        logger.info("Retrieve business agent operators");
        List<OperatorRequest> records  = new ArrayList<>();
        try{
            //..get operator records
            List<Operator> o_records = operators.findByBusiness_Id(businessId);
            if(!o_records.isEmpty()){
                for (Operator record : o_records) {
                    records.add(this.mapper.map(record, OperatorRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBusinessOperators' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<OperatorRequest> create(OperatorRequest operator, IndividualRequest person, BusinessRequest business) throws InterruptedException{
        logger.info("Adding new agent operator");
        try{
            Operator record = this.mapper.map(operator, Operator.class);

            if(business != null){
                BusinessAgent bizRecord = mapper.map(business, BusinessAgent.class);
                record.setBusiness(bizRecord);
            }

            if(person != null){
                IndividualAgent personRecord = mapper.map(person, IndividualAgent.class);
                record.setPerson(personRecord);
            }

            operators.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(operator);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(OperatorRequest operator, IndividualRequest person, BusinessRequest business) {
        logger.info("update Operator record");
        try{
            Operator record = this.mapper.map(operator, Operator.class);
            record.setId(operator.getId());
            if(business != null){
                BusinessAgent bizRecord = mapper.map(business, BusinessAgent.class);
                record.setBusiness(bizRecord);
            }

            if(person != null){
                IndividualAgent personRecord = mapper.map(person, IndividualAgent.class);
                record.setPerson(personRecord);
            }
            operators.update(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in OperatorService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete operator");
        try{
            logger.info(String.format("Operator isDeleted value set to %s", deleted ? "true": "false"));
            operators.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete Operator record");
        try{
            logger.info(String.format("Delete Operator with id %s", id));
            operators.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'OperatorService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
