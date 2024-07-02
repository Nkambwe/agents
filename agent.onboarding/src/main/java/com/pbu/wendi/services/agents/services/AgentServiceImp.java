package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.*;
import com.pbu.wendi.repositories.agents.repos.BusinessRepository;
import com.pbu.wendi.repositories.agents.repos.IndividualRepository;
import com.pbu.wendi.requests.agents.dto.*;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AgentServiceImp implements AgentService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final IndividualRepository individuals;
    private final BusinessRepository businesses;
    public AgentServiceImp(
            AppLoggerService logger,
            ModelMapper mapper,
            IndividualRepository individuals,
            BusinessRepository businesses){
        this.logger = logger;
        this.mapper = mapper;
        this.individuals = individuals;
        this.businesses = businesses;
    }

    //region Individual Agents
    @Override
    public CompletableFuture<Boolean> duplicateIndividualSortCode(String sortCode){
        logger.info("Checking whether sortCode exists");
        try{
            return CompletableFuture.completedFuture(individuals.existsBySortCode(sortCode));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicateIndividualSortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> duplicateIndividualPinNumber(String pin){
        logger.info("Checking whether PIN number exists");
        try{
            return CompletableFuture.completedFuture(individuals.existsByPinNumber(pin));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicateIndividualPinNumber'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> duplicatePersonalTin(String tin){
        logger.info("Checking whether PIN number exists");
        try{
            return CompletableFuture.completedFuture(individuals.existsByPersonalTin(tin));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatePersonalTin'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    @Transactional
    @Override
    public CompletableFuture<Boolean> individualExists(long id){
        logger.info("Check if individual exists");
        try{
            return CompletableFuture.completedFuture(individuals.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'individualExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<IndividualRequest> findIndividual(long id){
        logger.info(String.format("Retrieving individual agent with ID %s", id));
        try{

            IndividualAgent record = individuals.findByIdWithRelatedEntities(id);
            if(record != null){
                IndividualRequest request = mapIndividual(record);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findIndividual'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<IndividualRequest> findIndividualSortCode(String sortCode) {
        logger.info(String.format("Retrieving individual agent with sortCode %s", sortCode));
        try{

            IndividualAgent record = individuals.findBySortCode(sortCode);
            if(record != null){
                IndividualRequest request = mapIndividual(record);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findIndividualSortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<IndividualRequest>> getIndividuals(){
        List<IndividualRequest> records  = new ArrayList<>();
        try{
            //..get individual records
            List<IndividualAgent> personRecords = individuals.findIndividuals();
            if(!personRecords.isEmpty()){
                for (IndividualAgent record : personRecords) {
                    records.add(mapIndividual(record));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getIndividuals'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<IndividualRequest>> getIndividuals(boolean isDeleted){
        List<IndividualRequest> records  = new ArrayList<>();
        try{
            //..get individual records
            List<IndividualAgent> personRecords = individuals.findAllWithRelatedEntities(isDeleted);
            if(!personRecords.isEmpty()){
                for (IndividualAgent record : personRecords) {
                    records.add(mapIndividual(record));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getIndividuals'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<IndividualRequest> createIndividual(IndividualRequest person, AffiliationRequest org){
        logger.info("Adding new individual agent");
        try{
            IndividualAgent record = this.mapper.map(person, IndividualAgent.class);

            //..assign affiliation
            Affiliation aff = mapper.map(org, Affiliation.class);
            record.setAffiliation(aff);

            //..get operator details
            OperatorRequest operator = person.getOperator();
            if(operator != null){
                Operator ops = this.mapper.map(operator, Operator.class);
                if(record.getOperators() == null){
                    record.setOperators(new ArrayList<>());
                }
                record.getOperators().add(ops);
            }

            //..get signatories details
            List<SignatoryRequest> signatories = person.getSignatories();
            if(signatories != null && !signatories.isEmpty()){
                List<Signatory> signs = new ArrayList<>();
                for (SignatoryRequest sign: signatories) {
                    signs.add(this.mapper.map(sign, Signatory.class));
                }
                record.setSignatories(signs);
            }

            //..get kin details
            List<KinRequest> relatives  = person.getRelatives();
            if(relatives != null && !relatives.isEmpty()){
                List<Kin> kins = new ArrayList<>();
                for (KinRequest kin: relatives) {
                    kins.add(this.mapper.map(kin, Kin.class));
                }
                record.setNextOfKin(kins);
            }

            //..get partners
            List<PartnerRequest> partners = person.getPartners();
            if(partners != null && !partners.isEmpty()){
                List<Partner> buddies = new ArrayList<>();
                for (PartnerRequest partner: partners) {
                    buddies.add(this.mapper.map(partner, Partner.class));
                }
                record.setPartners(buddies);
            }

            individuals.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(person);
        }catch(Exception ex){
            logger.info("An error occurred in method 'createIndividual'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void createIndividualAgents(List<IndividualRequest> records, long userId) {
        logger.info(String.format("Import list of individual agents by user with ID '%s'", userId));
        try{
            List<IndividualAgent> agents = records.stream().map(request ->
                    mapper.map(request, IndividualAgent.class)).collect(Collectors.toList());
            individuals.saveAll(agents);
        } catch(Exception ex){
            logger.info("An error occurred in method 'createIndividualAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public void updateIndividual(IndividualRequest person, AffiliationRequest org){
        logger.info("update individual agent");
        try{
            IndividualAgent record = this.mapper.map(person, IndividualAgent.class);
            record.setId(person.getId());

            //..assign affiliation
            Affiliation aff = mapper.map(org, Affiliation.class);
            record.setAffiliation(aff);

            individuals.update(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateIndividual'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void softDeleteIndividual(long id, boolean deleted){
        logger.info("Delete individual agent");
        try{
            logger.info(String.format("Agent is_deleted value set to %s", deleted ? "true": "false"));
            individuals.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteIndividual'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void deleteIndividual(long id){
        logger.info("Delete Individual Agent");
        try{
            logger.info(String.format("Delete Individual Agent with id %s", id));
            individuals.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteIndividual'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    //endregion

    //region Business Agents
    @Transactional
    @Override
    public CompletableFuture<Boolean> businessExists(long id){
        logger.info("Checking whether business exists");
        try{
            return CompletableFuture.completedFuture(businesses.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'businessExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> duplicateSortCode(String sortCode){
        logger.info("Checking whether pin name exists");
        try{
            return CompletableFuture.completedFuture(businesses.existsBySortCode(sortCode));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicateSortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> duplicateBusinessPinNumber(String pin){
        logger.info("Checking whether pin name exists");
        try{
            return CompletableFuture.completedFuture(businesses.existsByPinNumber(pin));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicatePinNumber'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> duplicateBusinessTin(String tin){
        logger.info("Checking whether tin name exists");
        try{
            return CompletableFuture.completedFuture(businesses.existsByBusinessTin(tin));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicateBusinessTin'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<Boolean> duplicateBusinessNameWithDifferentIds(String name, long id){
        logger.info("Checking whether business name exists");
        try{
            return CompletableFuture.completedFuture(businesses.existsByBusinessNameAndIdNot(name, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'duplicateBusinessNameWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<BusinessRequest>  findBusinessById(long id){
        logger.info(String.format("Retrieving business agent with ID %s", id));
        try{
            BusinessAgent record = businesses.findByIdWithRelatedEntities(id);
            if(record != null){
                BusinessRequest request = mapBusiness(record);
                return CompletableFuture.completedFuture(request);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBusinessById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BusinessRequest> findBusinessSortCode(String sortCode) {
        logger.info(String.format("Retrieving business agent with SortCode %s", sortCode));
        try{
            BusinessAgent record = businesses.findBySortCode(sortCode);
            if(record != null){
                BusinessRequest request = mapBusiness(record);
                return CompletableFuture.completedFuture(request);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBusinessSortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<BusinessRequest>  findBusinessByName(String name){
        logger.info(String.format("Retrieving business agent with business name %s", name));
        try{

            BusinessAgent record = businesses.findByBusinessName(name);
            if(record != null){
                BusinessRequest request = mapBusiness(record);
                return CompletableFuture.completedFuture(request);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBusinessByName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<BusinessRequest>> getBusinesses(){
        List<BusinessRequest> records  = new ArrayList<>();
        try{
            logger.info("Retrieve a list all of business agents");
            //..get business records
            List<BusinessAgent> businessRecords = businesses.findAllWithRelatedEntities();
            if(!businessRecords.isEmpty()){
                for (BusinessAgent record : businessRecords) {
                    records.add(mapBusiness(record));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getBusinesses'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<BusinessRequest>> getBusinesses(boolean isDeleted){
        List<BusinessRequest> records  = new ArrayList<>();
        try{
            logger.info("Retrieve a list undeleted business agents");
            //..get business records
            List<BusinessAgent> businessRecords = businesses.findAllWithRelatedEntities(isDeleted);
            if(!businessRecords.isEmpty()){
                for (BusinessAgent record : businessRecords) {
                    records.add(mapBusiness(record));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getBusinesses'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<BusinessRequest> createBusiness(BusinessRequest business, AffiliationRequest org) {
        logger.info("Create new business agent");
        try{
            BusinessAgent record = this.mapper.map(business, BusinessAgent.class);

            Affiliation aff = mapper.map(org, Affiliation.class);
            record.setAffiliation(aff);

            //..get operator details
            OperatorRequest operator = business.getOperator();
            if(operator != null){
                Operator ops = this.mapper.map(operator, Operator.class);
                if(record.getOperators() == null){
                    record.setOperators(new ArrayList<>());
                }
                record.getOperators().add(ops);
            }

            //..get signatories details
            List<SignatoryRequest> signatories = business.getSignatories();
            if(signatories != null && !signatories.isEmpty()){
                List<Signatory> signs = new ArrayList<>();
                for (SignatoryRequest sign: signatories) {
                    signs.add(this.mapper.map(sign, Signatory.class));
                }
                record.setSignatories(signs);
            }

            businesses.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(business);
        }catch(Exception ex){
            logger.info("An error occurred in method 'createBusiness'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void createBusinessAgents(List<BusinessRequest> records, long userId) {
        logger.info(String.format("Import list of business agents by user with ID '%s'", userId));
        try{
            List<BusinessAgent> agents = records.stream().map(request ->
                    mapper.map(request, BusinessAgent.class)).collect(Collectors.toList());
            businesses.saveAll(agents);
        } catch (Exception ex){
            logger.info("An error occurred in method 'createBusinessAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void updateBusiness(BusinessRequest business, AffiliationRequest org){
        logger.info("update business agent");
        try{
            BusinessAgent record = this.mapper.map(business, BusinessAgent.class);
            record.setId(business.getId());

            //attach affiliated organization
            Affiliation aff = mapper.map(org, Affiliation.class);
            record.setAffiliation(aff);
            businesses.update(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateBusiness'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void softDeleteBusiness(long id, boolean deleted){
        logger.info("Delete Business agent");
        try{
            logger.info(String.format("Agent is_deleted value set to %s", deleted ? "true": "false"));
            businesses.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteBusiness'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void deleteBusiness(long id){
        logger.info("Delete Business Agent");
        try{
            logger.info(String.format("Delete Business Agent with id %s", id));
            businesses.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteBusiness'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    //endregion

    //region general

    @Transactional
    @Override
    public CompletableFuture<Boolean> isDuplicateCode(String sortCode) {
        boolean exists = individuals.existsBySortCode(sortCode) || businesses.existsBySortCode(sortCode);
        return CompletableFuture.completedFuture(exists);
    }
    @Transactional
    @Override
    public CompletableFuture<Boolean> isDuplicatePin(String pin){
        boolean exists = individuals.existsByPinNumber(pin) || businesses.existsByPinNumber(pin);
        return CompletableFuture.completedFuture(exists);
    }
    @Transactional
    @Override
    public CompletableFuture<Boolean> isDuplicateTin(String tin){
        boolean exists = individuals.existsByPersonalTin(tin) || businesses.existsByBusinessTin(tin);
        return CompletableFuture.completedFuture(exists);
    }
    @Transactional
    @Override
    public CompletableFuture<List<AgentRequest>> getAgents(){
        List<AgentRequest> records  = new ArrayList<>();
        try{
            //..get individual records
            List<IndividualAgent> personRecords = individuals.findAllWithRelatedEntities();
            if(!personRecords.isEmpty()){
                for (IndividualAgent record : personRecords) {
                    records.add(mapIndividualRequest(record));
                }
            }

            //..get business records
            List<BusinessAgent> bizRecords = businesses.findAllWithRelatedEntities();
            if(!bizRecords.isEmpty()){
                for (BusinessAgent record : bizRecords) {
                    records.add(mapBusinessRequest(record));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }

    }

    @Transactional
    @Override
    public CompletableFuture<List<AgentRequest>> getAgents(boolean isDeleted){
        List<AgentRequest> records  = new ArrayList<>();
        try{
            //..get individual records
            List<IndividualAgent> personRecords = individuals.findAllWithRelatedEntities(isDeleted);
            if(!personRecords.isEmpty()){
                for (IndividualAgent record : personRecords) {
                    records.add(mapIndividualRequest(record));
                }
            }

            //..get business records
            List<BusinessAgent> bizRecords = businesses.findAllWithRelatedEntities(isDeleted);
            if(!bizRecords.isEmpty()){
                for (BusinessAgent record : bizRecords) {
                    records.add(mapBusinessRequest(record));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    @Transactional
    @Override
    public CompletableFuture<List<AgentRequest>> getAgents(int type){
        List<AgentRequest> records  = new ArrayList<>();
        try{

            switch (type) {
                case 1: {
                    //..get individual records
                    List<IndividualAgent> personRecords = individuals.findAllWithRelatedEntities();
                    if (!personRecords.isEmpty()) {
                        for (IndividualAgent record : personRecords) {
                            records.add(mapIndividualRequest(record));
                        }
                    }
                }
                break;
                case 2 : {
                    //..get business records
                    List<BusinessAgent> bizRecords = businesses.findAllWithRelatedEntities();
                    if (!bizRecords.isEmpty()) {
                        for (BusinessAgent record : bizRecords) {
                            records.add(mapBusinessRequest(record));
                        }
                    }
                }
                break;
                default : {
                    logger.info("An error occurred in method 'getAgents'");
                    logger.error("No agent type selected");
                    throw new GeneralException("No agent type selected");
                }
            }


        } catch(Exception ex){
            logger.info("An error occurred in method 'getAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }

        return CompletableFuture.completedFuture(records);
    }
    @Transactional
    @Override
    public CompletableFuture<List<AgentRequest>> getAgents(int type, boolean isDeleted){
        List<AgentRequest> records  = new ArrayList<>();
        try{

            switch (type) {
                case 1: {
                    logger.info("Retrieving a list of individual agents");
                    //..get individual records
                    List<IndividualAgent> personRecords = individuals.findAllWithRelatedEntities(isDeleted);
                    if (!personRecords.isEmpty()) {
                        for (IndividualAgent record : personRecords) {
                            records.add(mapIndividualRequest(record));
                        }
                    }
                }
                break;
                case 2: {
                    logger.info("Retrieving a list of business agents");
                    //..get business records
                    List<BusinessAgent> bizRecords = businesses.findAllWithRelatedEntities(isDeleted);
                    if (!bizRecords.isEmpty()) {
                        for (BusinessAgent record : bizRecords) {
                            records.add(mapBusinessRequest(record));
                        }
                    }
                }
                break;
                default: {
                    logger.info("An error occurred in method 'getAgents'");
                    logger.error("No agent type selected");
                    throw new GeneralException("No agent type selected");
                }
            }

        } catch(Exception ex){
            logger.info("An error occurred in method 'getAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }

        return CompletableFuture.completedFuture(records);
    }

    @Transactional
    @Override
    public CompletableFuture<List<AgentRequest>> getAffiliationAgents(long affiliationId){
        List<AgentRequest> records  = new ArrayList<>();
        try{
            //..get individual records
            List<IndividualAgent> personRecords = individuals.findByAffiliationId(affiliationId);
            if(!personRecords.isEmpty()){
                for (IndividualAgent record : personRecords) {
                    records.add(mapIndividualRequest(record));
                }
            }

            //..get business records
            List<BusinessAgent> bizRecords = businesses.findByAffiliationId(affiliationId);
            if(!bizRecords.isEmpty()){
                for (BusinessAgent record : bizRecords) {
                    records.add(mapBusinessRequest(record));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAgents'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    //endregion

    //region utility methods
    private AgentRequest mapIndividualRequest(IndividualAgent record) {
        AgentRequest request = this.mapper.map(record, AgentRequest.class);
        return individualIncludes(request, record);
    }

    private IndividualRequest mapIndividual(IndividualAgent record) {
        IndividualRequest request = this.mapper.map(record, IndividualRequest.class);
        return individualIncludes(request, record);
    }

    private IndividualRequest individualIncludes(IndividualRequest request, IndividualAgent record) {
        //..include affiliation to request
        if(record.getAffiliation() != null) {
            this.mapper.map(record.getAffiliation(), request);
        }

        //..include approval to request
        //if(record.getOperators() != null && !record.getApprovals().isEmpty()) {
        //    for (Operator operator : record.getOperators()){
        //        this.mapper.map(operator, request);
        //    }
        //}

        //..include operator to request
        if(record.getOperators() != null && !record.getOperators().isEmpty()) {
            for(Operator op : record.getOperators()){
                this.mapper.map(op, request);
            }

        }

        //..include next kins
        if(record.getNextOfKin() != null && !record.getNextOfKin().isEmpty()){
            List<KinRequest> kins = new ArrayList<>();
            for(Kin kin: record.getNextOfKin()){
                kins.add(this.mapper.map(kin, KinRequest.class));
            }

            request.setRelatives(kins);
        }

        //..include signatories
        if(record.getSignatories() != null && !record.getSignatories().isEmpty()){
            List<SignatoryRequest> signatories = new ArrayList<>();
            for(Signatory signatory: record.getSignatories()){
                signatories.add(this.mapper.map(signatory, SignatoryRequest.class));
            }

            request.setSignatories(signatories);
        }

        return request;
    }

    private AgentRequest individualIncludes(AgentRequest request, IndividualAgent record) {
        //..include affiliation to request
        if(record.getAffiliation() != null) {
            this.mapper.map(record.getAffiliation(), request);
        }

        //..include approval to request
        //if(record.getApprovals() != null && !record.getApprovals().isEmpty()) {
        //    for (Approval approval : record.getApprovals()){
        //        this.mapper.map(approval, request);
        //    }
        //}

        //..include operator to request
        if(record.getOperators() != null && !record.getOperators().isEmpty()) {
            for(Operator op : record.getOperators()){
                this.mapper.map(op, request);
            }
        }
        return request;
    }

    private BusinessRequest mapBusiness(BusinessAgent record){
        BusinessRequest request = this.mapper.map(record, BusinessRequest.class);
        return businessIncludes(request, record);
    }
    private AgentRequest mapBusinessRequest(BusinessAgent record) {
        AgentRequest request = this.mapper.map(record, AgentRequest.class);
        return businessIncludes(request, record);
    }

    private AgentRequest businessIncludes(AgentRequest request, BusinessAgent record) {
        if(record.getAffiliation() != null) {
            this.mapper.map(record.getAffiliation(), request);
        }

        //..include approval to request
        //if(record.getApprovals() != null && !record.getApprovals().isEmpty()) {
        //    for (Approval approval : record.getApprovals()){
        //        this.mapper.map(approval, request);
        //    }
       // }

        //..include operator to request
        if(record.getOperators() != null && !record.getOperators().isEmpty()) {
            for(Operator op : record.getOperators()){
                this.mapper.map(op, request);
            }
        }

        return request;
    }

    private BusinessRequest businessIncludes(BusinessRequest request, BusinessAgent record) {
        //..include affiliation to request
        if(record.getAffiliation() != null) {
            this.mapper.map(record.getAffiliation(), request);
        }

        //..include approval to request
        //if(record.getApprovals() != null && !record.getApprovals().isEmpty()) {
        //    for (Approval approval : record.getApprovals()){
        //        this.mapper.map(approval, request);
        //    }
        //}

        //..include operator to request
        if(record.getOperators() != null && !record.getOperators().isEmpty()) {
            for(Operator op : record.getOperators()){
                this.mapper.map(op, request);
            }
        }

        //..include signatories
        if(record.getSignatories() != null && !record.getSignatories().isEmpty()){
            List<SignatoryRequest> signatories = new ArrayList<>();
            for(Signatory signatory: record.getSignatories()){
                signatories.add(this.mapper.map(signatory, SignatoryRequest.class));
            }

            request.setSignatories(signatories);
        }

        return request;
    }
    //endregion
}
