package com.pbu.wendi.controllers;

import com.pbu.wendi.requests.agents.dto.*;
import com.pbu.wendi.services.agents.services.*;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.enums.AgentType;
import com.pbu.wendi.utils.exceptions.*;
import com.pbu.wendi.utils.helpers.AppConstants;
import com.pbu.wendi.utils.helpers.ApplicationSettings;
import com.pbu.wendi.utils.helpers.Generators;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("agents")
public class AgentController {
    private final ModelMapper mapper;
    private final AppLoggerService logger;
    private final ApplicationExceptionHandler exceptionHandler;
    private final AgentService agents;
    private final AffiliationService affiliations;
    private final ApprovalService approvals;
    private final KinService kins;
    private final PartnerService partners;
    private final OperatorService operators;
    private final SignatoryService signatories;
    private final SettingService settings;
    private final BankService banks;
    private final OutletService outlets;
    private final DistrictService districts;

    public AgentController(
            ModelMapper mapper,
            AppLoggerService logger,
            ApplicationExceptionHandler exceptionHandler,
            AffiliationService affiliations,
            AgentService agents,
            ApprovalService approvals,
            KinService kins,
            PartnerService partners,
            OperatorService operators,
            SignatoryService signatories,
            SettingService settings,
            BankService banks,
            OutletService outlets,
            DistrictService districts) {
        this.logger = logger;
        this.mapper = mapper;
        this.exceptionHandler = exceptionHandler;
        this.agents = agents;
        this.affiliations = affiliations;
        this.approvals = approvals;
        this.kins = kins;
        this.partners = partners;
        this.operators = operators;
        this.signatories = signatories;
        this.settings = settings;
        this.banks = banks;
        this.outlets = outlets;
        this.districts = districts;
    }

    //region all agents
    @GetMapping("/getAgentById/{agentId}/{agentType}/{userId}")
    public ResponseEntity<?> getAgentById(@PathVariable("agentId") long agentId,
                                          @PathVariable("agentType")int agentType,
                                          @PathVariable("userId") long userId,
                                          HttpServletRequest request){
        logger.info(String.format("Retrieve Agent with AgentId '%s' by user with id %s" ,agentId, userId));
        AgentRequest record;
        try {

            IndividualRequest person = null;
            BusinessRequest business = null;
            if(AgentType.convertToEnum(agentType) == AgentType.INDIVIDUAL){
                CompletableFuture<IndividualRequest> personRec = agents.findIndividual(agentId)   ;
                person = personRec.join();
            } else {
                CompletableFuture<BusinessRequest> businessRec = agents.findBusinessById(agentId)   ;
                business = businessRec.join();
            }

            if(person == null && business == null){
                logger.info(String.format("Agent with AgentId '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",agentId),
                        request);
            }

            record = person != null ?
                    mapper.map(person, AgentRequest.class) :
                    mapper.map(business, AgentRequest.class);

        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAgentBySortCode/{sortCode}/{agentType}/{userId}")
    public ResponseEntity<?> getAgentBySortCode(@PathVariable("sortCode") String sortCode,
                                                @PathVariable("agentType")int agentType,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Retrieve Agent with sortCode '%s' by user with id %s" ,sortCode, userId));
        AgentRequest record;
        try {

            IndividualRequest person = null;
            BusinessRequest business = null;
            if(AgentType.convertToEnum(agentType) == AgentType.INDIVIDUAL){
                CompletableFuture<IndividualRequest> personRec = agents.findIndividualSortCode(sortCode)   ;
                person = personRec.join();
            } else {
                CompletableFuture<BusinessRequest> businessRec = agents.findBusinessSortCode(sortCode)   ;
                business = businessRec.join();
            }

            if(person == null && business == null){
                logger.info(String.format("Agent with SortCode '%s' not found. Returned a 404 code - Resource not found", sortCode));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","SortCode",sortCode),
                        request);
            }

            record = person != null ?
                    mapper.map(person, AgentRequest.class) :
                    mapper.map(business, AgentRequest.class);

        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/saveIndividualAgents/{userId}")
    public ResponseEntity<?> saveIndividualAgents(@RequestBody @Valid List<IndividualRequest> records,
                                                  @PathVariable("userId") long userId,
                                                  HttpServletRequest request){
        try {
            logger.info(String.format("Importing individual agent records by user with id %s" ,userId));
            agents.createIndividualAgents(records, userId);
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>("Imported successfully", HttpStatus.OK);
    }

    @PostMapping("/saveBusinessAgents/{userId}")
    public ResponseEntity<?> saveBusinessAgents(@RequestBody @Valid List<BusinessRequest> records,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){

        try {
            logger.info(String.format("Importing business agent records by user with id %s" ,userId));
            agents.createBusinessAgents(records, userId);
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }
        return new ResponseEntity<>("Imported successfully", HttpStatus.OK);
    }

    //endregion

    //region business agents
    @GetMapping("/ getBusinessWithId/{agentId}/{userId}")
    public ResponseEntity<?> getBusinessWithId(@PathVariable("agentId") long agentId,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Retrieve Individual Agent with ID '%s' by user with id %s",agentId,userId));
        BusinessRequest record;
        try {
            CompletableFuture<BusinessRequest> futureRecord = agents.findBusinessById(agentId);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Agent with ID '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",agentId),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/ getBusinessWithSortCode/{sortCode}/{userId}")
    public ResponseEntity<?> getBusinessWithSortCode(@PathVariable("sortCode") String sortCode,
                                                     @PathVariable("userId") long userId,
                                                     HttpServletRequest request){
        logger.info(String.format("Retrieve Business Agent with SortCode '%s' by user with id %s",sortCode,userId));
        BusinessRequest record;
        try {
            CompletableFuture<BusinessRequest> futureRecord = agents.findBusinessSortCode(sortCode);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Agent with SortCode '%s' not found. Returned a 404 code - Resource not found", sortCode));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","SortCode",sortCode),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/ getBusinessWithName/{businessName}/{userId}")
    public ResponseEntity<?> getBusinessWithName(@PathVariable("businessName") String businessName,
                                                 @PathVariable("userId") long userId,
                                                 HttpServletRequest request){
        logger.info(String.format("Retrieve Business Agent with SortCode '%s' by user with id %s",businessName,userId));
        BusinessRequest record;
        try {
            CompletableFuture<BusinessRequest> futureRecord = agents.findBusinessByName(businessName);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Agent with BusinessName '%s' not found. Returned a 404 code - Resource not found", businessName));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","BusinessName",businessName),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getBusinessAgents/{userId}")
    public ResponseEntity<?>getBusinessAgents(@PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve business agents by user with id %s",userId));

        List<BusinessRequest> records;
        try {
            CompletableFuture<List<BusinessRequest>> agentRecords = agents.getBusinesses();
            records = agentRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }

        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getDeletedBusinessAgents/{userId}")
    public ResponseEntity<?>getDeletedBusinessAgents(@PathVariable("userId") long userId,
                                                     HttpServletRequest request){
        logger.info(String.format("Retrieve business agents by user with id %s",userId));

        List<BusinessRequest> records;
        try {
            CompletableFuture<List<BusinessRequest>> agentRecords = agents.getBusinesses(true);
            records = agentRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }

        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @PostMapping("/onBoardBusinessAgent/{userId}")
    public ResponseEntity<?> onBoardBusinessAgent(@RequestBody BusinessRequest business,
                                                  @PathVariable("userId") long userId,
                                                  BindingResult bindingResult,
                                                  HttpServletRequest request) {
        logger.info(String.format("Create new Business agent by user with id %s", userId));
        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        //generate PIN
        business.setPinNumber(Generators.generatePinNumber());

        //...get last sort code
        String parameterName = AppConstants.SORT_CODE;
        CompletableFuture<SettingsRequest> settingsRecord = settings.findByParamName(parameterName);
        SettingsRequest config = settingsRecord.join();
        String sortCode;
        try {
            sortCode = resizeCode(config, parameterName, settings);
            business.setSortCode(sortCode);
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        }

        BusinessRequest record;
        try {

            //check for duplicate name
            String bizName = business.getBusinessName();
            CompletableFuture<BusinessRequest>  bizRecord = agents.findBusinessByName(bizName);
            BusinessRequest bizFound = bizRecord.join();
            if(bizFound != null){
                logger.info(String.format("Resource Conflict! Another Business with name '%s' exists", bizName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Agent", "BusinessName", bizName),
                        request);
            }

            //...check for duplicate TIN number
            String tin = business.getBusinessTin();
            CompletableFuture<Boolean> existsRecord = agents.duplicateBusinessTin(tin);
            boolean exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Business with TIN '%s' exists", tin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Agent", "TIN", tin),
                        request);
            }

            //...check for duplicate SortCode number
            existsRecord = agents.duplicateSortCode(sortCode);
            exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Business with SortCode '%s' exists", sortCode));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Agent", "SortCode", sortCode),
                        request);
            }

            //check for duplicate PIN_number
            String pin = business.getPinNumber();
            logger.info(String.format("Checking whether Business PIN '%s' is not in use.", pin));
            existsRecord = this.agents.duplicateBusinessPinNumber(pin);
            exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Business with PIN '%s' exists", pin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Individual", "PIN", pin),
                        request);
            }

            //...check if top level organization exists
            long orgId = business.getAffiliationId();
            CompletableFuture<AffiliationRequest> orgRecord = affiliations.findAffiliationById(orgId);
            AffiliationRequest org = orgRecord.join();
            if(org == null){
                logger.info(String.format("Organization with ID '%s' not found. Returned a 404 code - Resource not found", orgId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","OrganizationId",orgId),
                        request);
            }

            //..return create record
            CompletableFuture<BusinessRequest> biz = this.agents.createBusiness(business, org);
            record = biz.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving Business agent"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/modifyBusinessAgent/{userId}")
    public ResponseEntity<?> modifyBusinessAgent(@RequestBody BusinessRequest business,
                                                 @PathVariable("userId") long userId,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request) {
        logger.info(String.format("Modify Business agent with ID by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        BusinessRequest record;
        try {

            //check for record if in Db
            long recordId = business.getId();
            CompletableFuture<Boolean> futureRecord = agents.businessExists(recordId);
            boolean exists = futureRecord.join();
            if(!exists){
                logger.info(String.format("Agent with ID '%s' not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",recordId),
                        request);
            }

            String bizName = business.getBusinessName();
            CompletableFuture<Boolean> existsRecord = agents.duplicateBusinessNameWithDifferentIds(bizName, recordId);
            exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Business with name '%s' exists", bizName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Agent", "BusinessName", bizName),
                        request);
            }

            //...check if top level organization exists
            long orgId = business.getAffiliationId();
            CompletableFuture<AffiliationRequest> orgRecord = affiliations.findAffiliationById(orgId);
            AffiliationRequest org = orgRecord.join();
            if(org == null){
                logger.info(String.format("Organization with ID '%s' not found. Returned a 404 code - Resource not found", orgId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","OrganizationId",orgId),
                        request);
            }

            //..return update record
            this.agents.updateBusiness(business, org);

            //..return record
            CompletableFuture<BusinessRequest> record1 = agents.findBusinessById(recordId);
            record = record1.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeleteBusiness/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteBusiness(@PathVariable Long recordId,
                                                @PathVariable Boolean isDeleted,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Mark Agent with ID '%s' as deleted by user with id %s",recordId,userId));
        try {
            CompletableFuture<Boolean> existsRecord = agents.businessExists(recordId);
            boolean exists = existsRecord.join();
            if(!exists){
                logger.info(String.format("Agent with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",recordId),
                        request);
            }

            agents.softDeleteBusiness(recordId, isDeleted);
            return new ResponseEntity<>("Agent record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }
    @PutMapping("/deleteBusiness/{recordId}/{userId}")
    public ResponseEntity<?> deleteBusiness(@PathVariable Long recordId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Delete Agent with ID '%s' by user with id %s",recordId, userId));

        try {
            CompletableFuture<Boolean> existsRecord = agents.businessExists(recordId);
            boolean exists = existsRecord.join();
            if(!exists){
                logger.info(String.format("Agent with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",recordId),
                        request);
            }

            agents.deleteBusiness(recordId);
            return new ResponseEntity<>("Agent record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }
    //endregion

    //region individual agents
    @GetMapping("/ getIndividualWithId/{agentId}/{userId}")
    public ResponseEntity<?> getIndividualWithId(@PathVariable("agentId") long agentId,
                                                 @PathVariable("userId") long userId,
                                                 HttpServletRequest request){
        logger.info(String.format("Retrieve Individual Agent with ID '%s' by user with id %s",agentId,userId));
        IndividualRequest record;
        try {
            CompletableFuture<IndividualRequest> futureRecord = agents.findIndividual(agentId);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Agent with ID '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",agentId),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/ getIndividualWithSortCode/{sortCode}/{userId}")
    public ResponseEntity<?> getIndividualWithSortCode(@PathVariable("sortCode") String sortCode,
                                                       @PathVariable("userId") long userId,
                                                       HttpServletRequest request){
        logger.info(String.format("Retrieve Individual Agent with SortCode '%s' by user with id %s",sortCode, userId));
        IndividualRequest record;
        try {
            CompletableFuture<IndividualRequest> futureRecord = agents.findIndividualSortCode(sortCode);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Agent with SortCode '%s' not found. Returned a 404 code - Resource not found", sortCode));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","SortCode",sortCode),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getIndividualAgents/{userId}")
    public ResponseEntity<?>getIndividualAgents(@PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Retrieve individual agents by user with id %s",userId));

        List<IndividualRequest> records;
        try {
            CompletableFuture<List<IndividualRequest>> agentRecords = agents.getIndividuals();
            records = agentRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }

        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @GetMapping("/getDeletedIndividualAgents/{userId}")
    public ResponseEntity<?>getDeletedIndividualAgents(@PathVariable("userId") long userId,
                                                       HttpServletRequest request){
        logger.info(String.format("Retrieve individual agents by user with id %s",userId));

        List<IndividualRequest> records;
        try {
            CompletableFuture<List<IndividualRequest>> agentRecords = agents.getIndividuals(true);
            records = agentRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }

        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @PostMapping("/onBoardIndividualAgent/{userId}")
    public ResponseEntity<?> onBoardIndividualAgent(@RequestBody IndividualRequest individual,
                                                    @PathVariable("userId") long userId,
                                                    BindingResult bindingResult,
                                                    HttpServletRequest request) {
        logger.info(String.format("Create new Individual agent by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        //generate PIN
        individual.setPinNumber(Generators.generatePinNumber());

        //...get last sort code
        String parameterName = AppConstants.SORT_CODE;
        CompletableFuture<SettingsRequest> settingsRecord = settings.findByParamName(parameterName);
        SettingsRequest config = settingsRecord.join();
        String sortCode;
        try {
            sortCode = resizeCode(config, parameterName, settings);
            individual.setSortCode(sortCode);
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        }

        IndividualRequest record;
        try {

            //...check for duplicate TIN number
            String tin = individual.getTin();
            CompletableFuture<Boolean> existsRecord = agents.duplicatePersonalTin(tin);
            boolean exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Individual with TIN '%s' exists", tin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Agent", "TIN", tin),
                        request);
            }

            //...check for duplicate SortCode number
            sortCode = individual.getSortCode();
            existsRecord = agents.duplicateIndividualSortCode(sortCode);
            exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Individual with SortCode '%s' exists", tin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Agent", "SortCode", sortCode),
                        request);
            }

            //check for duplicate PIN_number
            String pin = individual.getPinNumber();
            logger.info(String.format("Checking whether Individual PIN '%s' is not in use.", pin));
            existsRecord = this.agents.duplicateIndividualPinNumber(pin);
            exists = existsRecord.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Individual with PIN '%s' exists", pin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Individual", "PIN", pin),
                        request);
            }

            //...check if top level organization exists
            long orgId = individual.getAffiliationId();
            CompletableFuture<AffiliationRequest> orgRecord = affiliations.findAffiliationById(orgId);
            AffiliationRequest org = orgRecord.join();
            if(org == null){
                logger.info(String.format("Organization with ID '%s' not found. Returned a 404 code - Resource not found", orgId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","OrganizationId",orgId),
                        request);
            }

            //..return create record
            CompletableFuture<IndividualRequest> person = this.agents.createIndividual(individual, org);
            record = person.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving signatory"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/modifyIndividualAgent/{userId}")
    public ResponseEntity<?> modifyIndividualAgent(@RequestBody IndividualRequest individual,
                                                   @PathVariable("userId") long userId,
                                                   BindingResult bindingResult,
                                                   HttpServletRequest request) {
        logger.info(String.format("Update Individual agent by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        IndividualRequest record;
        try {

            //check for record if in Db
            long recordId = individual.getId();
            CompletableFuture<Boolean> futureRecord = agents.individualExists(recordId);
            boolean exists = futureRecord.join();
            if(!exists){
                logger.info(String.format("Agent with ID '%s' not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",recordId),
                        request);
            }

            //...check if top level organization exists
            long orgId = individual.getAffiliationId();
            CompletableFuture<AffiliationRequest> orgRecord = affiliations.findAffiliationById(orgId);
            AffiliationRequest org = orgRecord.join();
            if(org == null){
                logger.info(String.format("Organization with ID '%s' not found. Returned a 404 code - Resource not found", orgId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","OrganizationId",orgId),
                        request);
            }

            //..return create record
            this.agents.updateIndividual(individual, org);

            //..return record
            CompletableFuture<IndividualRequest> record1 = agents.findIndividual(recordId);
            record = record1.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving signatory"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeleteIndividual/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteIndividual(@PathVariable Long recordId,
                                                  @PathVariable Boolean isDeleted,
                                                  @PathVariable("userId") long userId,
                                                  HttpServletRequest request){
        logger.info(String.format("Mark Agent with ID '%s' as deleted by user with id %s",recordId,userId));
        try {
            CompletableFuture<Boolean> existsRecord = agents.individualExists(recordId);
            boolean exists = existsRecord.join();
            if(!exists){
                logger.info(String.format("Agent with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",recordId),
                        request);
            }

            agents.softDeleteIndividual(recordId, isDeleted);
            return new ResponseEntity<>("Agent record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }
    @PutMapping("/deleteIndividual/{recordId}/{userId}")
    public ResponseEntity<?> deleteIndividual(@PathVariable Long recordId,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Delete Agent with ID '%s' by user with id %s",recordId, userId));

        try {
            CompletableFuture<Boolean> existsRecord = agents.individualExists(recordId);
            boolean exists = existsRecord.join();
            if(!exists){
                logger.info(String.format("Agent with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",recordId),
                        request);
            }

            agents.deleteIndividual(recordId);
            return new ResponseEntity<>("Agent record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }
    //endregion

    //region Next Of Kins
    @GetMapping("/getKinWithName/{name}/{agentId}/{userId}")
    public ResponseEntity<?> getKinWithName(@PathVariable("name") String name,
                                            @PathVariable("agentId") long agentId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Retrieve NextOfKin with name '%s' by user with id %s" ,name, userId));
        KinRequest record;
        try {
            CompletableFuture<KinRequest> futureRecord = kins.findKinByName(name, agentId);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("NextOfKin with name '%s' not found. Returned a 404 code - Resource not found", name));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("NextOfKin","Name",name),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAgentKins/{agentId}/{userId}")
    public ResponseEntity<?>getAgentKins(@PathVariable("agentId") long agentId,
                                         @PathVariable("userId") long userId,
                                         HttpServletRequest request){
        logger.info(String.format("Retrieve NextOfKin for agent with ID '%s' by user with id %s", agentId, userId));

        List<KinRequest> records;
        try {
            CompletableFuture<List<KinRequest>> operatorRecords = kins.getKins(agentId);
            records = operatorRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/createKin/{userId}")
    public ResponseEntity<?> createKin(@RequestBody @Valid KinRequest kin,
                                       @PathVariable("userId") long userId,
                                       BindingResult bindingResult,
                                       HttpServletRequest request){
        logger.info(String.format("Create new NextOfKin by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        KinRequest record;
        try {

            long agentId = kin.getAgentId();
            CompletableFuture<IndividualRequest> agentRecord = agents.findIndividual(agentId);
            IndividualRequest agent = agentRecord.join();
            if(agent == null){
                logger.info(String.format("Agent record with ID '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId", agentId),
                        request);
            }

            //check whether kin name is not in use
            String name = kin.getFullName();
            logger.info(String.format("Checking whether NextOfKin assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.kins.existsByFullNameAndAgentId(name, agentId);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another NextOfKin with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("NextOfKin", "Name", name),
                        request);
            }

            //..return create record
            CompletableFuture<KinRequest> ops = this.kins.create(kin, agent);
            record = ops.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving NextOfKin"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PutMapping("/updateKin/{userId}")
    public ResponseEntity<?> updateKin(@RequestBody @Valid KinRequest kin,
                                       @PathVariable("userId") long userId,
                                       BindingResult bindingResult,
                                       HttpServletRequest request){
        logger.info(String.format("Modification of NextOfKin by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        KinRequest record;
        try {

            //check for Kin record
            long recordId = kin.getId();
            CompletableFuture<Boolean> kinExists = kins.kinExists(recordId);
            boolean exists = kinExists.join();
            if(!exists){
                logger.info(String.format("NextOfKin with ID '%s' not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("NextOfKin","KinId", recordId),
                        request);
            }

            //check for agent record
            long agentId = kin.getAgentId();
            CompletableFuture<IndividualRequest> agentRecord = agents.findIndividual(agentId);
            IndividualRequest agent = agentRecord.join();
            if(agent == null){
                logger.info(String.format("Agent record with ID '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId", agentId),
                        request);
            }

            //check whether kin name is not in use
            String name = kin.getFullName();
            logger.info(String.format("Checking whether NextOfKin assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.kins.existsByFullNameAndAgentIdNotId(name, agentId, recordId);
            exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another NextOfKin with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("NextOfKin", "Name", name),
                        request);
            }

            //..update record
            this.kins.update(kin, agent);
            record = kin;
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/softDeleteKin/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteKin(@PathVariable Long recordId,
                                           @PathVariable Boolean isDeleted,
                                           @PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Mark NextOfKin '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<KinRequest> futureRecord = kins.findKinById(recordId);
            KinRequest record = futureRecord.join();
            if(record == null){
                logger.info(String.format("NextOfKin with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("NextOfKin","KinId",recordId),
                        request);
            }

            kins.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("NextOfKin record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PutMapping("/deleteKin/{recordId}/{userId}")
    public ResponseEntity<?> deleteKin(@PathVariable Long recordId,
                                       @PathVariable("userId") long userId,
                                       HttpServletRequest request){
        logger.info(String.format("Delete Next Of Kin with ID '%s' by user with id %s",recordId, userId));

        try {
            CompletableFuture<KinRequest> kin = this.kins.findKinById(recordId);
            KinRequest record = kin.join();
            if(record == null){
                logger.info(String.format("Next Of Kin with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId ,userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("NextOfKin","KinId", recordId),
                        request);
            }

            kins.delete(recordId);
            return new ResponseEntity<>("NextOfKin record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region Partners
    @GetMapping("/ getAPartnerWithName/{name}/{agentId}/{userId}")
    public ResponseEntity<?> getAPartnerWithName(@PathVariable("name") String name,
                                                 @PathVariable("agentId") long agentId,
                                                 @PathVariable("userId") long userId,
                                                 HttpServletRequest request){
        logger.info(String.format("Retrieve partner with name '%s' by user with id %s" ,name, userId));
        PartnerRequest record;
        try {
            CompletableFuture<PartnerRequest> futureRecord = partners.findPartnerByFullName(name, agentId);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Partner with name '%s' not found. Returned a 404 code - Resource not found", name));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Partner","Name",name),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/ getPartnerWithId/{partnerId}/{userId}")
    public ResponseEntity<?> getPartnerWithId(@PathVariable("partnerId") long partnerId,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve partner with PartnerId '%s' by user with id %s" ,partnerId, userId));
        PartnerRequest record;
        try {
            CompletableFuture<PartnerRequest> futureRecord = partners.findPartnerById(partnerId);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Partner with PartnerId '%s' not found. Returned a 404 code - Resource not found", partnerId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Partner","PartnerId",partnerId),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAgentPartners/{agentId}/{userId}")
    public ResponseEntity<?>getAgentPartners(@PathVariable("agentId") long agentId,
                                             @PathVariable("userId") long userId,
                                             HttpServletRequest request){
        logger.info(String.format("Retrieve Partner for agent with ID '%s' by user with id %s", agentId, userId));

        List<PartnerRequest> records;
        try {
            CompletableFuture<List<PartnerRequest>> operatorRecords = partners.getPartners(agentId);
            records = operatorRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @PutMapping("/createPartner/{userId}")
    public ResponseEntity<?> createPartner(@RequestBody @Valid PartnerRequest partner,
                                           @PathVariable("userId") long userId,
                                           BindingResult bindingResult,
                                           HttpServletRequest request){
        logger.info(String.format("Create new Partner by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        PartnerRequest record;
        try {

            long agentId = partner.getAgentId();
            CompletableFuture<IndividualRequest> agentRecord = agents.findIndividual(agentId);
            IndividualRequest agent = agentRecord.join();
            if(agent == null){
                logger.info(String.format("Agent record with ID '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId", agentId),
                        request);
            }

            //check whether Partner name is not in use
            String name = partner.getFullName();
            logger.info(String.format("Checking whether Partner assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.partners.existsByFullNameAndAgentId(name, agentId);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Partner with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Partner", "Name", name),
                        request);
            }

            //..return create record
            CompletableFuture<PartnerRequest> partRec = this.partners.create(partner, agent);
            record = partRec.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PutMapping("/updatePartner/{userId}")
    public ResponseEntity<?> updatePartner(@RequestBody @Valid PartnerRequest partner,
                                           @PathVariable("userId") long userId,
                                           BindingResult bindingResult,
                                           HttpServletRequest request){
        logger.info(String.format("Modification of Partner by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        PartnerRequest record;
        try {

            //check for partner record
            long recordId = partner.getId();
            CompletableFuture<Boolean> partExists = partners.partnerExists(recordId);
            boolean exists = partExists.join();
            if(!exists){
                logger.info(String.format("Partner with ID '%s' not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Partner","PartnerId", recordId),
                        request);
            }

            //check for agent record
            long agentId = partner.getAgentId();
            CompletableFuture<IndividualRequest> agentRecord = agents.findIndividual(agentId);
            IndividualRequest agent = agentRecord.join();
            if(agent == null){
                logger.info(String.format("Agent record with ID '%s' not found. Returned a 404 code - Resource not found", agentId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId", agentId),
                        request);
            }

            //check whether partner name is not in use
            String name = partner.getFullName();
            logger.info(String.format("Checking whether Partner assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.partners.existsByIdAndFullNameAndAgentId(recordId, name, agentId);
            exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Partner with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Partner", "Name", name),
                        request);
            }

            //..update record
            this.partners.update(partner, agent);
            record = partner;
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeletePartner/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeletePartner(@PathVariable Long recordId,
                                               @PathVariable Boolean isDeleted,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Mark Partner '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<PartnerRequest> futureRecord = partners.findPartnerById(recordId);
            PartnerRequest record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Partner with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Partner","PartnerId",recordId),
                        request);
            }

            partners.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("Partner record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PutMapping("/deletePartner/{recordId}/{userId}")
    public ResponseEntity<?> deletePartner(@PathVariable Long recordId,
                                           @PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Delete Partner with ID '%s' by user with id %s",recordId, userId));

        try {
            CompletableFuture<PartnerRequest> kin = (this).partners.findPartnerById(recordId);
            PartnerRequest record = kin.join();
            if(record == null){
                logger.info(String.format("Partner with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId ,userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Partner","PartnerId", recordId),
                        request);
            }

            partners.delete(recordId);
            return new ResponseEntity<>("Partner record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region Operators

    @GetMapping("/getAgentOperators/{agentId}/{userId}")
    public ResponseEntity<?>getAgentOperators(@PathVariable("agentId") long agentId,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve operators for agent with ID '%s' by user with id %s", agentId, userId));

        List<OperatorRequest> records;
        try {
            CompletableFuture<List<OperatorRequest>> operatorRecords = operators.findIndividualOperators(agentId);
            records = operatorRecords.get();

            //...check business id
            if(records == null || records.isEmpty()){
                operatorRecords = operators.findBusinessOperators(agentId);
                records = operatorRecords.get();
            }

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @PutMapping("/createOperator/{userId}")
    public ResponseEntity<?> createOperator(@RequestBody @Valid OperatorRequest operator,
                                            @PathVariable("userId") long userId,
                                            BindingResult bindingResult,
                                            HttpServletRequest request){
        logger.info(String.format("Create new operator by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        OperatorRequest record;
        try {

            long agentId = operator.getAgentId();

            //check whether operator name is not in use
            String name = operator.getOperatorName();
            logger.info(String.format("Checking whether operator assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.operators.findByOperatorNameAndBusinessId(name, agentId);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another operator with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Operator", "Name", name),
                        request);
            }

            //check individual agents
            recordExists = this.operators.findByOperatorNameAndPersonId(name, agentId);
            exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another operator with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Operator", "Name", name),
                        request);
            }

            //check whether Operator NIN in use
            String nin = operator.getIdNin();
            logger.info(String.format("Checking whether operator NIN '%s' is not in use.", nin));
            exists = this.operators.duplicatedNIN(name);
            if(exists){
                logger.info(String.format("Resource Conflict! Another operator is using this NIN '%s' exists", nin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Operator", "NIN", name),
                        request);
            }

            //get agent records
            CompletableFuture<IndividualRequest> personRecord = agents.findIndividual(agentId);
            IndividualRequest person = personRecord.join();

            BusinessRequest business = null;
            if(person == null){
                CompletableFuture<BusinessRequest> businessRecord = agents.findBusinessById(agentId);
                business = businessRecord.join();
            }

            if(person == null && business == null){
                logger.info(String.format("Agent with id %s retrieval by user with ID %s failed. Returned a 404 code - Resource not found", agentId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",agentId),
                        request);
            }

            //..return create record
            CompletableFuture<OperatorRequest> ops = this.operators.create(operator, person, business);
            record = ops.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving operator"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PutMapping("/updateOperator/{userId}")
    public ResponseEntity<?> updateOperator(@RequestBody @Valid OperatorRequest operator,
                                            @PathVariable("userId") long userId,
                                            BindingResult bindingResult,
                                            HttpServletRequest request){
        long recordId =  operator.getId();
        logger.info(String.format("Update operator with ID '%s' by user with id %s", recordId, userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        OperatorRequest record;
        try {

            long agentId = operator.getAgentId();

            //check whether operator name is not in use
            String name = operator.getOperatorName();
            logger.info(String.format("Checking whether operator assigned name '%s' is not in use.", name));
            boolean exists = this.operators.duplicatedName(name, agentId, recordId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another operator with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Operator", "Name", name),
                        request);
            }

            //check whether Operator NIN in use
            String nin = operator.getIdNin();
            logger.info(String.format("Checking whether operator NIN '%s' is not in use.", nin));
            exists = this.operators.duplicatedNIN(name, recordId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another operator is using this NIN '%s' exists", nin));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Operator", "NIN", name),
                        request);
            }

            //get agent records
            CompletableFuture<IndividualRequest> personRecord = agents.findIndividual(agentId);
            IndividualRequest person = personRecord.join();

            BusinessRequest business = null;
            if(person == null){
                CompletableFuture<BusinessRequest> businessRecord = agents.findBusinessById(agentId);
                business = businessRecord.join();
            }

            if(person == null && business == null){
                logger.info(String.format("Agent with id %s retrieval by user with ID %s failed. Returned a 404 code - Resource not found", agentId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Agent","AgentId",agentId),
                        request);
            }

            //..return create record
            this.operators.update(operator, person, business);
            record = operator;
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeleteOperator/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteOperator(@PathVariable Long recordId,
                                                @PathVariable Boolean isDeleted,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Mark Operator '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<SignatoryRequest> futureRecord = signatories.findById(recordId);
            SignatoryRequest record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Operator with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Operator","OperatorId",recordId),
                        request);
            }

            operators.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("Operator record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PutMapping("/deleteOperator/{recordId}/{userId}")
    public ResponseEntity<?> deleteOperator(@PathVariable Long recordId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Delete Operator with ID '%s' by user with id %s",recordId, userId));

        try {
            CompletableFuture<SignatoryRequest> app = this.signatories.findById(recordId);
            SignatoryRequest record = app.join();
            if(record == null){
                logger.info(String.format("Signatory with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Operator","OperatorId",recordId),
                        request);
            }

            signatories.delete(recordId);
            return new ResponseEntity<>("Operator record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region Signatories
    @GetMapping("/getAgentSignatories/{agentId}/{agentType}/{userId}")
    public ResponseEntity<?>getAgentSignatories(@PathVariable("agentId") long agentId,
                                                @PathVariable("agentType") int agentType,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Retrieve signatories for agent with ID '%s' by user with id %s", agentId, userId));

        List<SignatoryRequest> records;
        try {
            AgentType type = AgentType.convertToEnum(agentType);
            CompletableFuture<List<SignatoryRequest>> futureRecord = type == AgentType.BUSINESS ?
                    signatories.findByBusinessId(agentId) :
                    signatories.findByPersonId(agentId) ;
            records = futureRecord.get();
            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/createSignatory/{userId}")
    public ResponseEntity<?> createSignatory(@RequestBody @Valid SignatoryRequest signatory,
                                             @PathVariable("userId") long userId,
                                             BindingResult bindingResult,
                                             HttpServletRequest request){
        logger.info(String.format("Create new signatory by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        SignatoryRequest record;
        try {

            //check whether signatory name is not in use
            String name = signatory.getFullName();
            long agentId = signatory.getAgentId();
            logger.info(String.format("Checking whether signatory assigned name '%s' is not in use.", name));
            boolean exists = this.signatories.duplicatedSignatoryName(name, agentId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another Signatory with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Signatory", "Name", name),
                        request);
            }

            //..return create record
            CompletableFuture<SignatoryRequest> sign = this.signatories.createSignatory(signatory);
            record = sign.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving signatory"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PutMapping("/updateSignatory//{userId}")
    public ResponseEntity<?> updateSignatory(@RequestBody @Valid SignatoryRequest signatory,
                                             @PathVariable("userId") long userId,
                                             BindingResult bindingResult,
                                             HttpServletRequest request){
        long recordId =  signatory.getId();
        logger.info(String.format("Modify signatory with ID '%s' by user with id %s", recordId, userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        SignatoryRequest record;
        try {

            CompletableFuture<SignatoryRequest> sigRecord = this.signatories.findById(recordId);
            record = sigRecord.join();
            if(record == null){
                logger.info(String.format("Signatory with id %s not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Signatory","SignatoryId", recordId),
                        request);
            }

            //check whether signatory name is not in use
            String name = signatory.getFullName();
            logger.info(String.format("Checking whether signatory assigned name '%s' is not in use.", name));
            boolean exists = this.signatories.duplicatedName(name, recordId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another Signatory with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Signatory", "Name", name),
                        request);
            }

            //...update signatory
            signatories.updateSignatory(signatory);

            //..return updated record
            sigRecord = this.signatories.findById(recordId);
            record = sigRecord.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving organization"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeleteSignatory/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteSignatory(@PathVariable Long recordId,
                                                 @PathVariable Boolean isDeleted,
                                                 @PathVariable("userId") long userId,
                                                 HttpServletRequest request){
        logger.info(String.format("Mark Signatory '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<SignatoryRequest> futureRecord = signatories.findById(recordId);
            SignatoryRequest record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Signatory with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Signatory","SignatoryId",recordId),
                        request);
            }

            signatories.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("Signatory record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PutMapping("/deleteSignatory/{recordId}/{userId}")
    public ResponseEntity<?> deleteSignatory(@PathVariable Long recordId,
                                             @PathVariable("userId") long userId,
                                             HttpServletRequest request){
        logger.info(String.format("Delete Signatory with ID '%s' by user with id %s",recordId, userId));

        try {

            CompletableFuture<SignatoryRequest> app = this.signatories.findById(recordId);
            SignatoryRequest record = app.join();
            if(record == null){
                logger.info(String.format("Signatory with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Signatory","SignatoryId",recordId),
                        request);
            }

            signatories.delete(recordId);
            return new ResponseEntity<>("Signatory record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region Approvals

    @GetMapping("/getApprovalWithId/{recordId}/{userId}")
    public ResponseEntity<?>getApprovalWithId(@PathVariable("recordId") long recordId,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve approval with id %s by user with id %s", recordId, userId));

        ApprovalRequest record;
        try{

            //Retrieve record
            CompletableFuture<ApprovalRequest> app = this.approvals.findApprovalById(recordId);
            record = app.join();
            if(record == null){
                logger.info(String.format("Approval with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Approval","ApprovalId",recordId),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg), request);
        }

        //return branch record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getApprovals/{agentId}/{agentType}/{userId}")
    public ResponseEntity<?>getApprovals(@PathVariable("userId") long userId,
                                         @PathVariable("agentId") long agentId,
                                         @PathVariable("agentType") int agentType,
                                         HttpServletRequest request){
        logger.info(String.format("Retrieve all approvals for user with id %s", userId));

        List<ApprovalRequest> records;
        try {
            AgentType type = AgentType.convertToEnum(agentType);
            CompletableFuture<List<ApprovalRequest>> futureRecord = approvals.getApprovals(agentId, type);
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @PostMapping("/approveAgent/{agentType}/{userId}")
    public ResponseEntity<?> approveAgent(@RequestBody @Valid ApprovalRequest approval,
                                          @PathVariable int agentType,
                                          @PathVariable("userId") long userId,
                                          BindingResult bindingResult,
                                          HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        long agentId = approval.getAgentId();
        logger.info(String.format("Approve agent with ID '%s' by user with id %s", agentId, userId));
        ApprovalRequest record;
        try {

            //check whether approval exists is not in use
            logger.info(String.format("Checking whether agent has approval record is not in use... by user with ID %s", userId));

            AgentType type = AgentType.convertToEnum(agentType);
            CompletableFuture<Boolean> recordFound = type == AgentType.BUSINESS ?
                    this.approvals.existsByBusinessId(agentId) :
                    this.approvals.existsByPersonId(agentId);

            boolean exists = recordFound.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Agent has an approval record with ID '%s'. Consider updating record instead.", agentId));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Approval", "AgentId", agentId),
                        request);
            }

            CompletableFuture<ApprovalRequest> futureRecord = approvals.create(approval, type);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/updateApproval/{userId}")
    public ResponseEntity<?> updateApproval(@RequestBody @Valid ApprovalRequest approval,
                                            @PathVariable("userId") long userId,
                                            BindingResult bindingResult,
                                            HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Modify approval by user with id %s", userId));
        ApprovalRequest record;
        long approvalId = approval.getId();
        try {

            CompletableFuture<ApprovalRequest> approvalRecord = this.approvals.findApprovalById(approvalId);
            record = approvalRecord.join();
            if(record == null){
                logger.info(String.format("Approval with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", approvalId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",approvalId),
                        request);
            }
            approvals.update(approval);

            //..return updated record
            approvalRecord = this.approvals.findApprovalById(approvalId);
            record = approvalRecord.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeleteApproval/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteApproval(@PathVariable Long recordId,
                                                @PathVariable Boolean isDeleted,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Mark approval '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<ApprovalRequest> app = this.approvals.findApprovalById(recordId);
            ApprovalRequest record = app.join();
            if(record == null){
                logger.info(String.format("Approval with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Approval","ApprovalId",recordId),
                        request);
            }

            approvals.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("Approval record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }
    @GetMapping("/deleteApproval/{recordId}/{userId}")
    public ResponseEntity<?> deleteApproval(@PathVariable Long recordId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Delete approval with ID '%s' by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<ApprovalRequest> app = this.approvals.findApprovalById(recordId);
            ApprovalRequest record = app.join();
            if(record == null){
                logger.info(String.format("Approval with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Approval","ApprovalId",recordId),
                        request);
            }

            approvals.delete(recordId);
            return new ResponseEntity<>("Approval record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region affiliations
    @GetMapping("/getAffiliationById/{id}/{userId}")
    public ResponseEntity<?> getAffiliationById(@PathVariable("id") long id,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){

        logger.info(String.format("Retrieve Organization with ID '%s' by user with id %s" ,id, userId));
        AffiliationRequest record;
        try {
            CompletableFuture<AffiliationRequest> futureRecord = affiliations.findAffiliationById(id);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Organization with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","ID",id),
                        request);
            }

            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(record == null){
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Affiliation", "Id", String.format("%s", id)),
                    request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/ getAffiliationBySortCode/{sortCode}")
    public ResponseEntity<?> getAffiliationBySortCode(@PathVariable("sortCode") String sortCode, HttpServletRequest request){

        AffiliationRequest record;
        try {
            CompletableFuture<AffiliationRequest> futureRecord = affiliations.findAffiliationBySortCode(sortCode);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Organization with sort code %s not found. Returned a 404 code - Resource not found", sortCode));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","sortCode",sortCode),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAllAffiliations")
    public ResponseEntity<?>  getAllAffiliations(HttpServletRequest request) {
        List<AffiliationRequest> records;
        try {
            CompletableFuture<List<AffiliationRequest>> futureRecord = affiliations.getAffiliations();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(records == null || records.isEmpty()){
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getActiveAffiliations")
    public ResponseEntity<?> getActiveAffiliations(HttpServletRequest request) {
        List<AffiliationRequest> records;
        try {
            CompletableFuture<List<AffiliationRequest>> futureRecord = affiliations.getActiveAffiliations();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(records == null || records.isEmpty()){
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createAffiliation")
    public ResponseEntity<?> createAffiliation(@RequestBody @Valid AffiliationRequest affiliation,
                                               BindingResult bindingResult,
                                               HttpServletRequest request) {
        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        AffiliationRequest record;
        try {
            //check whether branch name is not in use
            logger.info("Checking whether organization assigned name is not in use...");
            String orgName = affiliation.getOrgName();
            boolean exists = this.affiliations.checkAffiliationDuplicateName(orgName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another organization with name '%s' exists", orgName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("organization", "Name", orgName),
                        request);
            }

            //check whether organization sort code is not in use
            logger.info("Checking whether organization assigned sort code is not in use...");
            String sortCode = affiliation.getSortCode();
            exists = this.affiliations.checkAffiliationDuplicateSortCode(sortCode);
            if(exists){
                logger.info(String.format("Resource Conflict! Another organization with SolId '%s' exists", sortCode));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("organization", "SortCode", sortCode),
                        request);
            }

            CompletableFuture<AffiliationRequest> futureRecord = affiliations.createAffiliation(affiliation);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving organization"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PutMapping("/updateAffiliation/{userId}")
    public ResponseEntity<?> updateAffiliation(@RequestBody @Valid AffiliationRequest affiliation,
                                               @PathVariable("userId") long userId,
                                               BindingResult bindingResult,
                                               HttpServletRequest request){

        long recordId =  affiliation.getId();
        logger.info(String.format("Modify Organization with ID '%s' by user with id %s", recordId, userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        AffiliationRequest record;
        try {

            CompletableFuture<AffiliationRequest> org = this.affiliations.findAffiliationById(affiliation.getId());
            record = org.join();
            if(record == null){
                logger.info(String.format("Organization with id %s not found. Returned a 404 code - Resource not found", affiliation.getId()));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","ID",affiliation.getId()),
                        request);
            }

            //check whether organization name is not in use
            logger.info(String.format("Checking whether organization assigned name '%s' is not in use.", affiliation.getOrgName()));
            String orgName = affiliation.getOrgName();
            boolean exists = this.affiliations.checkAffiliationDuplicateNameWithDifferentIds(orgName, affiliation.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another organization with name '%s' exists", orgName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Organization", "Name", orgName),
                        request);
            }

            //check whether organization SortCode is not in use
            logger.info("Checking whether organization assigned SortCode is not in use...");
            String sortCode = affiliation.getSortCode();
            exists = this.affiliations.checkAffiliationDuplicateSortCodeWithDifferentIds(sortCode, affiliation.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another organization with sortCode '%s' exists", sortCode));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Organization", "SortCode", sortCode),
                        request);
            }

            affiliations.updateAffiliation(affiliation);

            //..return updated record
            org = this.affiliations.findAffiliationById(affiliation.getId());
            record = org.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving organization"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @DeleteMapping("/softDeleteAffiliation/{id}/{isDeleted}")
    public ResponseEntity<?> softDeleteAffiliation(@PathVariable Long id,
                                                   @PathVariable boolean isDeleted,
                                                   HttpServletRequest request) {
        logger.info(String.format("Mark organization '%s' as deleted" ,id));

        try {

            CompletableFuture<AffiliationRequest> org = this.affiliations.findAffiliationById(id);
            AffiliationRequest record = org.join();
            if(record == null){
                logger.info(String.format("Organization with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","ID",id),
                        request);
            }

            affiliations.softDeleteAffiliation(id, isDeleted);
            return new ResponseEntity<>("Organization record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    @DeleteMapping("/deleteAffiliation/{id}")
    public ResponseEntity<?> deleteAffiliation(@PathVariable Long id,
                                               HttpServletRequest request) {
        logger.info(String.format("Delete organization with ID '%s'" ,id));

        try {

            CompletableFuture<AffiliationRequest> org = this.affiliations.findAffiliationById(id);
            AffiliationRequest record = org.join();
            if(record == null){
                logger.info(String.format("Organization with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Organization","ID",id),
                        request);
            }

            affiliations.deleteAffiliation(id);
            return new ResponseEntity<>("Organization record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region banks
    @GetMapping("/getBankById/{id}/{userId}")
    public ResponseEntity<?> getBankById(@PathVariable("id") long id,
                                         @PathVariable("userId") long userId,
                                         HttpServletRequest request){

        logger.info(String.format("Retrieve Bank ID '%s' by user with id %s" ,id, userId));
        BankRequest record;
        try {
            CompletableFuture<BankRequest> futureRecord = banks.findBankById(id);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Bank with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Bank","ID",id),
                        request);
            }

            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(record == null){
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Bank", "Id", String.format("%s", id)),
                    request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getBankBySortCode/{sortCode}")
    public ResponseEntity<?> getBankBySortCode(@PathVariable("sortCode") String sortCode, HttpServletRequest request){

        BankRequest record;
        try {
            CompletableFuture<BankRequest> futureRecord = banks.findBankBySortCode(sortCode);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Bank with sort code %s not found. Returned a 404 code - Resource not found", sortCode));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Bank","sortCode",sortCode),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getBanks")
    public ResponseEntity<?>  getBanks(HttpServletRequest request) {
        List<BankRequest> records;
        try {
            CompletableFuture<List<BankRequest>> futureRecord = banks.getBanks();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(records == null || records.isEmpty()){
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getActiveBanks")
    public ResponseEntity<?> getActiveBanks(HttpServletRequest request) {
        List<BankRequest> records;
        try {
            CompletableFuture<List<BankRequest>> futureRecord = banks.getActiveBanks();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(records == null || records.isEmpty()){
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createBank")
    public ResponseEntity<?> createBank(@RequestBody @Valid BankRequest bank,
                                        BindingResult bindingResult,
                                        HttpServletRequest request) {
        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        BankRequest record;
        try {
            //check whether branch name is not in use
            logger.info("Checking whether bank assigned name is not in use...");
            String bankName = bank.getBankName();
            boolean exists = this.banks.checkBankDuplicateName(bankName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another bank with name '%s' exists", bankName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Bank", "Name", bankName),
                        request);
            }

            //check whether bank sort code is not in use
            logger.info("Checking whether bank assigned sort code is not in use...");
            String sortCode = bank.getSortCode();
            exists = this.banks.checkBankDuplicateSortCode(sortCode);
            if(exists){
                logger.info(String.format("Resource Conflict! Another bank with SolId '%s' exists", sortCode));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Bank", "SortCode", sortCode),
                        request);
            }

            CompletableFuture<BankRequest> futureRecord = banks.createBank(bank);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving bank"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PutMapping("/updateBank/{userId}")
    public ResponseEntity<?> updateBank(@RequestBody @Valid BankRequest bank,
                                        @PathVariable("userId") long userId,
                                        BindingResult bindingResult,
                                        HttpServletRequest request){

        long recordId =  bank.getId();
        logger.info(String.format("Modify Bank with ID '%s' by user with id %s", recordId, userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        BankRequest record;
        try {

            CompletableFuture<BankRequest> bankRecord = this.banks.findBankById(bank.getId());
            record = bankRecord.join();
            if(record == null){
                logger.info(String.format("Bank with id %s not found. Returned a 404 code - Resource not found", bank.getId()));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Bank","ID",bank.getId()),
                        request);
            }

            //check whether bank name is not in use
            logger.info(String.format("Checking whether bank assigned name '%s' is not in use.", bank.getBankName()));
            String bankName = bank.getBankName();
            boolean exists = this.banks.checkBankDuplicateNameWithDifferentIds(bankName, bank.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another bank with name '%s' exists", bankName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Bank", "Name", bankName),
                        request);
            }

            //check whether bank SortCode is not in use
            logger.info("Checking whether bank assigned Sort Code is not in use...");
            String sortCode = bank.getSortCode();
            exists = this.banks.checkBankDuplicateSortCodeWithDifferentIds(sortCode, bank.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another bank with sortCode '%s' exists", sortCode));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Bank", "SortCode", sortCode),
                        request);
            }

            banks.updateBank(bank);

            //..return updated record
            bankRecord = this.banks.findBankById(bank.getId());
            record = bankRecord.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving bank"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @DeleteMapping("/softDeleteBank/{id}/{isDeleted}")
    public ResponseEntity<?> softDeleteBank(@PathVariable Long id,
                                            @PathVariable boolean isDeleted,
                                            HttpServletRequest request) {
        logger.info(String.format("Mark bank '%s' as deleted" ,id));

        try {

            CompletableFuture<BankRequest> bankRecord = this.banks.findBankById(id);
            BankRequest record = bankRecord.join();
            if(record == null){
                logger.info(String.format("Bank with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Bank","ID",id),
                        request);
            }

            banks.softDeleteBank(id, isDeleted);
            return new ResponseEntity<>("Bank record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    @DeleteMapping("/deleteBank/{id}")
    public ResponseEntity<?> deleteBank(@PathVariable Long id,
                                        HttpServletRequest request) {
        logger.info(String.format("Delete bank with ID '%s'" ,id));

        try {

            CompletableFuture<BankRequest> bankRecord = this.banks.findBankById(id);
            BankRequest record = bankRecord.join();
            if(record == null){
                logger.info(String.format("Bank with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Bank","ID",id),
                        request);
            }

            affiliations.deleteAffiliation(id);
            return new ResponseEntity<>("Bank record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region outlets
    @GetMapping("/getOutletById/{id}/{userId}")
    public ResponseEntity<?> getOutletById(@PathVariable("id") long id,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){

        logger.info(String.format("Retrieve Outlet with ID '%s' by user with id %s" ,id, userId));
        OutletRequest record;
        try {
            CompletableFuture<OutletRequest> futureRecord = outlets.findOutletsById(id);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Outlet with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Telecom","ID",id),
                        request);
            }

            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(record == null){
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Outlet", "Id", String.format("%s", id)),
                    request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/ getOutletByName/{name}/{userId}")
    public ResponseEntity<?> getOutletByName(@PathVariable("name") String name,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve outlet with name '%s' by user with id %s" ,name, userId));
        OutletRequest record;
        try {
            CompletableFuture<OutletRequest> futureRecord = outlets.findOutletsByName(name);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Outlet with name '%s' not found. Returned a 404 code - Resource not found", name));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Outlet","Name",name),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getOutlets/{userId}")
    public ResponseEntity<?>getOutlets(@PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Retrieve all outlets by user with id %s",userId));

        List<OutletRequest> records;
        try {
            CompletableFuture<List<OutletRequest>> telecomsRecords = outlets.getAllOutlets();
            records = telecomsRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getActiveOutlets/{userId}")
    public ResponseEntity<?>getActiveOutlets(@PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve all outlets by user with id %s",userId));

        List<OutletRequest> records;
        try {
            CompletableFuture<List<OutletRequest>> operatorRecords = outlets.getActiveOutlets();
            records = operatorRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/createOutlet/{userId}")
    public ResponseEntity<?> createOutlet(@RequestBody @Valid OutletRequest outlet,
                                           @PathVariable("userId") long userId,
                                           BindingResult bindingResult,
                                           HttpServletRequest request){
        logger.info(String.format("Create new outlet by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        OutletRequest record;
        try {
            //check whether outlet name is not in use
            String name = outlet.getOutletName();
            logger.info(String.format("Checking whether outlet assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.outlets.existsByName(name);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another outlet with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Outlet", "Name", name),
                        request);
            }

            //..return create record
            CompletableFuture<OutletRequest> telecomRecord = this.outlets.create(outlet);
            record = telecomRecord.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving Outlet"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PutMapping("/UpdateOutlet/{userId}")
    public ResponseEntity<?> UpdateOutlet(@RequestBody @Valid OutletRequest outlet,
                                           @PathVariable("userId") long userId,
                                           BindingResult bindingResult,
                                           HttpServletRequest request){
        logger.info(String.format("Modification of Outlet by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        OutletRequest record;
        try {

            //check for Outlet record
            long recordId = outlet.getId();
            CompletableFuture<Boolean> found = outlets.outletExists(recordId);
            boolean exists = found.join();
            if(!exists){
                logger.info(String.format("Outlet with ID '%s' not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Outlet","ID", recordId),
                        request);
            }

            //check whether telecom name is not in use
            String name = outlet.getOutletName();
            logger.info(String.format("Checking whether Outlet assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.outlets.existsByNameAndNotId(name, recordId);
            exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Outlet with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Outlet", "Name", name),
                        request);
            }

            //..update record
            this.outlets.update(outlet);
            record = outlet;
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/softDeleteOutlet/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteOutlet(@PathVariable Long recordId,
                                               @PathVariable Boolean isDeleted,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Mark Outlet '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<OutletRequest> futureRecord = outlets.findOutletsById(recordId);
            OutletRequest record = futureRecord.join();
            if(record == null){
                logger.info(String.format("Outlet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Telecom","ID",recordId),
                        request);
            }

            outlets.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("Outlet record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PutMapping("/deleteOutlet/{recordId}/{userId}")
    public ResponseEntity<?> deleteOutlet(@PathVariable Long recordId,
                                           @PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Delete Outlet with ID '%s' by user with id %s",recordId, userId));

        try {
            CompletableFuture<OutletRequest> outlet = this.outlets.findOutletsById(recordId);
            OutletRequest record = outlet.join();
            if(record == null){
                logger.info(String.format("Outlet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId ,userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Outlet","ID", recordId),
                        request);
            }

            outlets.delete(recordId);
            return new ResponseEntity<>("Telecom record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region districts
    @GetMapping("/getDistrictById/{id}/{userId}")
    public ResponseEntity<?> getDistrictById(@PathVariable("id") long id,
                                             @PathVariable("userId") long userId,
                                             HttpServletRequest request){

        logger.info(String.format("Retrieve District with ID '%s' by user with id %s" ,id, userId));
        DistrictRequest record;
        try {
            CompletableFuture<DistrictRequest> futureRecord = districts.findDistrictById(id);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("District with id %s not found. Returned a 404 code - Resource not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("District","ID",id),
                        request);
            }

            record = futureRecord.get();
        } catch (InterruptedException e) {
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if(record == null){
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("District", "Id", String.format("%s", id)),
                    request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getDistrictByName/{name}/{userId}")
    public ResponseEntity<?> getDistrictByName(@PathVariable("name") String name,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Retrieve District with name '%s' by user with id %s" ,name, userId));
        DistrictRequest record;
        try {
            CompletableFuture<DistrictRequest> futureRecord = districts.findDistrictByName(name);
            record = futureRecord.join();
            if(record == null){
                logger.info(String.format("District with name '%s' not found. Returned a 404 code - Resource not found", name));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("District","Name",name),
                        request);
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAllDistricts/{userId}")
    public ResponseEntity<?>getAllDistricts(@PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Retrieve all Districts by user with id %s",userId));

        List<DistrictRequest> records;
        try {
            CompletableFuture<List<DistrictRequest>> districtRecords = districts.getAllDistricts();
            records = districtRecords.get();

            //...check for null records
            if(records == null || records.isEmpty()){
                logger.info("No records found in database");
                records = new ArrayList<>();
            }
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/createDistrict/{userId}")
    public ResponseEntity<?> createDistrict(@RequestBody @Valid DistrictRequest district,
                                            @PathVariable("userId") long userId,
                                            BindingResult bindingResult,
                                            HttpServletRequest request){
        logger.info(String.format("Create new district by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        DistrictRequest record;
        try {
            //check whether district name is not in use
            String name = district.getDistrictName();
            logger.info(String.format("Checking whether district assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.districts.existsByName(name);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another district with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("District", "Name", name),
                        request);
            }

            //..return create record
            CompletableFuture<DistrictRequest> districtRecord = this.districts.create(district);
            record = districtRecord.join();
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return exceptionHandler.errorHandler(
                    new GeneralException("An error occurred while saving district"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PutMapping("/UpdateDistrict/{userId}")
    public ResponseEntity<?> UpdateDistrict(@RequestBody @Valid DistrictRequest district,
                                            @PathVariable("userId") long userId,
                                            BindingResult bindingResult,
                                            HttpServletRequest request){
        logger.info(String.format("Modification of district by user with id %s", userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        DistrictRequest record;
        try {

            //check for district record
            long recordId = district.getId();
            CompletableFuture<Boolean> found = districts.districtExists(recordId);
            boolean exists = found.join();
            if(!exists){
                logger.info(String.format("District with ID '%s' not found. Returned a 404 code - Resource not found", recordId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("District","ID", recordId),
                        request);
            }

            //check whether district name is not in use
            String name = district.getDistrictName();
            logger.info(String.format("Checking whether Telecom assigned name '%s' is not in use.", name));
            CompletableFuture<Boolean> recordExists = this.districts.existsByNameAndNotId(name, recordId);
            exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another District with name '%s' exists", name));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("District", "Name", name),
                        request);
            }

            //..update record
            this.districts.update(district);
            record = district;
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/softDeleteDistrict/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteDistrict(@PathVariable Long recordId,
                                                @PathVariable Boolean isDeleted,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Mark District '%s' as deleted by user with id %s" ,recordId, userId));

        try {
            CompletableFuture<DistrictRequest> futureRecord = districts.findDistrictById(recordId);
            DistrictRequest record = futureRecord.join();
            if(record == null){
                logger.info(String.format("District with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("District","ID",recordId),
                        request);
            }

            districts.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("District record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }
    @PutMapping("/deleteDistrict/{recordId}/{userId}")
    public ResponseEntity<?> deleteDistrict(@PathVariable Long recordId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Delete District with ID '%s' by user with id %s",recordId, userId));
        try {
            CompletableFuture<DistrictRequest> district = this.districts.findDistrictById(recordId);
            DistrictRequest record = district.join();
            if(record == null){
                logger.info(String.format("District with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found",recordId ,userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("District","ID", recordId),
                        request);
            }

            districts.delete(recordId);
            return new ResponseEntity<>("District record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region Settings

    @GetMapping("/ findSettingsByParamName/{paramName}")
    public ResponseEntity<?> findSettingsByParamName(@PathVariable("paramName") String paramName,
                                                     HttpServletRequest request){
        SettingsRequest config;
        try {
            CompletableFuture<SettingsRequest> settingsRecord = settings.findByParamName(paramName);
            config = settingsRecord.join();
            //...save parameter if not found
            if(config == null){
                config = ApplicationSettings.findByParamName(paramName);
                if(config != null){
                    settings.create(config);
                }
            }
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @GetMapping("/getLoginSettings")
    public ResponseEntity<?>getLoginSettings(HttpServletRequest request){
        logger.info("Retrieve login configurations");
        List<SettingsRequest> records;
        List<String> paramNames = new ArrayList<>();
        paramNames.add( AppConstants.TIMEOUT);
        paramNames.add( AppConstants.EXPRIREPWD);
        paramNames.add( AppConstants.EXPRIREPWD_DAYS);
        try {

            //..get settings
            CompletableFuture<List<SettingsRequest>> futureRecord = settings.findAllByParamNames(paramNames);
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.size() < paramNames.size()){

            //...save the missing settings with default values
            if(records == null){
                records = new ArrayList<>();
                for (String paramName: paramNames) {
                    SettingsRequest config = ApplicationSettings.findByParamName(paramName);
                    if(config != null){
                        try {
                            settings.create(config);
                            records.add(config);
                        } catch (InterruptedException e) {
                            return exceptionHandler.errorHandler(
                                    new GeneralException(e.getMessage()),request);
                        }
                    }
                }
            } else {
                List<SettingsRequest> newList = new ArrayList<>();
                for (String paramName : paramNames) {
                    boolean found = false;
                    for (SettingsRequest req : records) {
                        if (req.getParamName().equals(paramName)) {
                            newList.add(req);
                            found = true;
                            break;  // Exit the inner loop if a match is found
                        }
                    }
                    if (!found) {
                        // If no match is found, create a new SettingsRequest
                        SettingsRequest newReq = new SettingsRequest();
                        SettingsRequest config = ApplicationSettings.findByParamName(paramName);
                        if(config != null){
                            newList.add(newReq);
                        }

                    }
                }

                //..overwrite list with new
                records = newList;

                //save settings
                for (SettingsRequest record : records) {
                    try {
                        settings.create(record);
                    } catch (InterruptedException e) {
                        return exceptionHandler.errorHandler(
                                new GeneralException(e.getMessage()),request);
                    }
                }
            }
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getDocumentSettings")
    public ResponseEntity<?>getDocumentSettings(HttpServletRequest request){
        logger.info("Retrieve attachment configurations");
        List<SettingsRequest> records;
        List<String> paramNames = new ArrayList<>();
        paramNames.add( AppConstants.BIZ_URL);
        paramNames.add( AppConstants.BIZ_FOLDER);
        paramNames.add( AppConstants.IND_URL);
        paramNames.add( AppConstants.IND_FOLDER);
        try {

            //..get settings
            CompletableFuture<List<SettingsRequest>> futureRecord = settings.findAllByParamNames(paramNames);
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return exceptionHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.size() < paramNames.size()){

            //...save the missing settings with default values
            if(records == null){
                records = new ArrayList<>();
                for (String paramName: paramNames) {
                    SettingsRequest config = ApplicationSettings.findByParamName(paramName);
                    if(config != null){
                        try {
                            settings.create(config);
                            records.add(config);
                        } catch (InterruptedException e) {
                            return exceptionHandler.errorHandler(
                                    new GeneralException(e.getMessage()),request);
                        }
                    }
                }
            } else {
                List<SettingsRequest> newList = new ArrayList<>();
                for (String paramName : paramNames) {
                    boolean found = false;
                    for (SettingsRequest req : records) {
                        if (req.getParamName().equals(paramName)) {
                            newList.add(req);
                            found = true;
                            break;  // Exit the inner loop if a match is found
                        }
                    }
                    if (!found) {
                        // If no match is found, create a new SettingsRequest
                        SettingsRequest newReq = new SettingsRequest();
                        SettingsRequest config = ApplicationSettings.findByParamName(paramName);
                        if(config != null){
                            newList.add(newReq);
                        }

                    }
                }

                //..overwrite list with new
                records = newList;

                //save settings
                for (SettingsRequest record : records) {
                    try {
                        settings.create(record);
                    } catch (InterruptedException e) {
                        return exceptionHandler.errorHandler(
                                new GeneralException(e.getMessage()),request);
                    }
                }
            }
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/updateSettings/{userId}")
    public ResponseEntity<?> updateSettings(@RequestBody @Valid SettingsRequest setting,
                                            @PathVariable("userId") long userId,
                                            BindingResult bindingResult,
                                            HttpServletRequest request){
        long recordId =  setting.getId();
        logger.info(String.format("Update settings with ID '%s' by user with id %s", recordId, userId));

        // Validate request object
        if (bindingResult.hasErrors()) {
            return exceptionHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        SettingsRequest record;
        try {

            //check whether param name is not in use
            String paramName = setting.getParamName();
            logger.info(String.format("Checking whether Settings parameter name '%s' is not in use.", paramName));
            CompletableFuture<Boolean> exitsRecord = this.settings.existsByParamName(paramName);
            boolean exists = exitsRecord.get();
            if(exists){
                logger.info(String.format("Resource Conflict! Another Settings with paramName '%s' exists", paramName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Settings", "paramName", paramName),
                        request);
            }

            //..return create record
            this.settings.updateSettings(setting);
            record = setting;
        } catch (Exception e) {
            return exceptionHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/softDeleteSetting/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?> softDeleteSetting(@PathVariable Long recordId,
                                               @PathVariable Boolean isDeleted,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Mark Settings '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<Boolean> existsRecord = this.settings.existsById(recordId);
            Boolean exists = existsRecord.join();
            if(!exists){
                logger.info(String.format("Settings with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Settings","SettingsId",recordId),
                        request);
            }

            settings.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("Settings record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }

    }
    @GetMapping("/deleteSettings/{recordId}/{userId}")
    public ResponseEntity<?> deleteSettings(@PathVariable Long recordId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Delete Settings '%s' as deleted by user with id %s" ,recordId, userId));

        try {

            CompletableFuture<Boolean> existsRecord = this.settings.existsById(recordId);
            Boolean exists = existsRecord.join();
            if(!exists){
                logger.info(String.format("Settings with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Settings","SettingsId",recordId),
                        request);
            }

            settings.deleteById(recordId);
            return new ResponseEntity<>("Settings successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg),request);
        }
    }
    //endregion

    //region private methods

    private String resizeCode(SettingsRequest config,  String paramName, SettingService settings) throws InterruptedException {

        //...save parameter if not found
        if(config == null){
            config = ApplicationSettings.findByParamName(paramName);
            settings.create(config);
        }
        //get last used sortNumber and increment by 1
        String value = config != null ? (config.getParamValue() != null ? config.getParamValue() : "1"): "1";
        long code = Long.parseLong(value);
        code +=1;
        return Generators.resizeCode(code);
    }

    //endregion

    //region testing
    @GetMapping("/hello")
    public String SayHello(){
        return "Hello from AGENTS";
    }

    //endregion

}

