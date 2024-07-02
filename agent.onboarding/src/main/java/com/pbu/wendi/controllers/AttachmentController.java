package com.pbu.wendi.controllers;

import com.pbu.wendi.configurations.ApplicationExceptionHandler;
import com.pbu.wendi.services.sam.services.LogService;
import com.pbu.wendi.requests.attachments.dto.AttachmentRequest;
import com.pbu.wendi.requests.attachments.dto.IdentificationRequest;
import com.pbu.wendi.requests.attachments.dto.TypeRequest;
import com.pbu.wendi.requests.sam.dto.LogRequest;
import com.pbu.wendi.services.attachments.services.AttachmentService;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.common.NetworkService;
import com.pbu.wendi.utils.exceptions.*;
import com.pbu.wendi.utils.helpers.Generators;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("attachments")
public class AttachmentController  {
    public final AppLoggerService logger;
    public final ApplicationExceptionHandler exceptionHandler;
    public final AttachmentService service;
    private final NetworkService networkService;
    private final LogService logService;
    public  AttachmentController(AppLoggerService logger, ApplicationExceptionHandler exceptionHandler, AttachmentService service,
                                 NetworkService networkService, LogService logService)  {
        this.logger = logger;
        this.exceptionHandler = exceptionHandler;
        this.service = service;
        this.networkService = networkService;
        this.logService = logService;
    }

    //region attachments

    @GetMapping("findAttachment")
    public ResponseEntity<?> findAttachment(@RequestParam("id") long id, @RequestParam("userId") long userId, HttpServletRequest request){
        try {

            CompletableFuture<AttachmentRequest> result = this.service.findAttachmentById(id);
            AttachmentRequest record = result.join();

            //try finding resource
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("Retrieve app Attachment id %s by user with id %s", id, userId)));

            //attachment not found, throw NOT_FOUND response
            if(record == null){
                //log user activity
                logger.info(String.format("Resource not found! Attachment with ID'%s' not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                                new NotFoundException("Attachment","ID", id),
                                request);
            }

            //return attachment record
            return new ResponseEntity<>(record, HttpStatus.OK);

        } catch(Exception ex){
            logger.info("An error occurred in method 'findAttachment'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @GetMapping("getAttachments")
    public ResponseEntity<?>getAttachments(@RequestParam("ownerId") long ownerId, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Retrieving a list of attachments for owner with Owner ID %s", ownerId);
        logger.info(log);

        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("Retrieve list of attachments by user with id %s", userId)));

        try {
            CompletableFuture<List<AttachmentRequest>> attachments = service.getAttachments(ownerId);
            List<AttachmentRequest> records = attachments.join();
            return ResponseEntity.ok(records);

        } catch(Exception ex){
            logger.info("An error occurred in method 'getAttachments'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @GetMapping("checkAttachment")
    public ResponseEntity<?>checkAttachment(@RequestParam("id") long id, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Check if attachment with id. '%s' exists", id);
        logger.info(log);
        try {
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

            //...check if exists
            CompletableFuture<Boolean> record = this.service.attachmentExists(id);
            boolean result = record.join();

            //return attachment record
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch(Exception ex){
            logger.info("An error occurred in method 'checkAttachment'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @PostMapping("createAttachments")
    public ResponseEntity<?> createAttachments(@RequestParam("userId") long userId, @RequestBody @Valid AttachmentRequest[] attachments, BindingResult bindingResult, HttpServletRequest request) {
        //..creation date
        String log = "Creating file attachments";
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));
        try {
            //Validate request object
            logger.info("Validating attachment record :: ");
            if (bindingResult.hasErrors()) {
                logger.info("Attachment record Validation error....");
                ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
                logger.info(String.format("%s", validationException.getMessage()));
                return exceptionHandler.validationExceptionHandler(validationException, request);
            }

            //...save type
            CompletableFuture<AttachmentRequest[]> records = this.service.createAttachments(attachments);
            AttachmentRequest[] result = records.join();

            //return attachment record
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (InterruptedException ex) {
            //log error
            logger.info("Error creating attachment file(s)");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            return exceptionHandler.threadCanceledHandler(new CanceledException(), request);
        }catch(Exception ex){
            logger.info("An error occurred in method 'createAttachments'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @PutMapping("updateAttachment")
    public ResponseEntity<?> updateAttachment(@RequestParam("userId") long userId, @RequestBody @Valid AttachmentRequest attachment, BindingResult bindingResult, HttpServletRequest request){
        String log = String.format("Modifying ID Type with name '%s'",attachment.getAttDescr());
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //Validate request object
        logger.info("Validating ID Type record :: ");
        if (bindingResult.hasErrors()) {
            logger.info("ID Type record Validation error....");
            ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
            logger.info(String.format("%s", validationException.getMessage()));

            return exceptionHandler.validationExceptionHandler(validationException, request);
        }

        try{
            //check whether type with name is not in use
            logger.info("Checking whether attachment assigned name is not in use...");
            String name = attachment.getAttDescr();
            long agent_id = attachment.getAgentId();
            boolean exists = this.service.checkAttachmentDuplicateNameWithSameIds(name, agent_id);
            if(exists){
                logger.info(String.format("Resource Conflict! Another Attachment with name '%s' exists for owner with ID '%s'", name, agent_id));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Attachment","Owner", agent_id),
                        request);
            }

            //update attachment record in database
            this.service.updateAttachment(attachment);

            //return attachment record
            return new ResponseEntity<>(attachment, HttpStatus.OK);

        } catch(Exception ex){
            logger.info("An error occurred in method 'updateType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @DeleteMapping("softDeleteAttachment")
    public ResponseEntity<?> softDeleteAttachment(@RequestParam Long id, @RequestParam boolean isDeleted, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Deleting attachment record. Attachment with id %s is_deleted set to %s",id, isDeleted ? "True" : "False");
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.attachmentExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Attachment", "id",id),
                    request);
        }

        //delete Attachment
        service.softDeleteAttachment(id, isDeleted);
        return new ResponseEntity<>("Attachment updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("deleteAttachment")
    public ResponseEntity<?> deleteAttachment(@RequestParam Long id, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Deleting attachment record. Attachment id %s is being deleted",id);
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.attachmentExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Attachment", "id",id),
                    request);
        }

        //delete attachment
        service.deleteAttachment(id);
        return new ResponseEntity<>("Attachment deleted successfully", HttpStatus.OK);
    }

    //endregion

    //region ID Types

    @GetMapping("findType")
    public ResponseEntity<?> findType(@RequestParam("id") long id, @RequestParam("userId") long userId, HttpServletRequest request){
        try {
            String log = String.format("Retrieve ID type with %s", id);
            logger.info(log);
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

            CompletableFuture<TypeRequest> result = this.service.findTypeById(id);
            TypeRequest record = result.join();

            //...identification type not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("Resource not found! Identification type with ID'%s' not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Identification Type","ID", id),
                        request);
            }

            //return identification record
            return new ResponseEntity<>(record, HttpStatus.OK);

        } catch(Exception ex){
            logger.info("An error occurred in method 'findType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @GetMapping("getTypes")
    public ResponseEntity<?> getTypes(HttpServletRequest request, @RequestParam("userId") long userId) {
        String log = "Retrieving a list of all identifications types";
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        try {
            CompletableFuture<List<TypeRequest>> types = service.getTypes();
            List<TypeRequest> records = types.join();
            return ResponseEntity.ok(records);

        } catch(Exception ex){
            logger.info("An error occurred in method 'getTypes'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @GetMapping("checkType")
    public ResponseEntity<?> checkType(@RequestParam("id") long id, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Check if ID type with ID '%s' exists", id);
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        try {
            //...save type
            CompletableFuture<Boolean> record = this.service.idTypeExists(id);
            boolean result = record.join();

            //return ID types record
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch(Exception ex){
            logger.info("An error occurred in method 'checkType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @PostMapping("createType")
    public ResponseEntity<?> createIdType(@RequestBody @Valid TypeRequest idType, @RequestParam("userId") long userId, BindingResult bindingResult, HttpServletRequest request) {
        //..creation date
        String log = String.format("Creating new ID Type %s", idType.getTypeName());
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));
        try {
            //Validate request object
            logger.info("Validating ID Type record :: ");
            if (bindingResult.hasErrors()) {
                logger.info("ID Type record Validation error....");
                ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
                logger.info(String.format("%s", validationException.getMessage()));
                return exceptionHandler.validationExceptionHandler(validationException, request);
            }

            //check for duplicate type names
            logger.info("Checking whether ID type assigned name is not in use...");
            String typeName = idType.getTypeName();
            boolean exists = this.service.checkTypeDuplicateName(typeName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Type with name '%s' exists", typeName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("ID Type","Name", typeName),
                        request);
            }

            //...save type
            CompletableFuture<TypeRequest> record = this.service.createIdType(idType);
            TypeRequest result = record.join();

            //return ID types record
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (InterruptedException ex) {
            //log error
            logger.info("Error creating ID Type");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            return exceptionHandler.threadCanceledHandler(new CanceledException(), request);
        } catch(Exception ex){
            logger.info("An error occurred in method 'createIdType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @PutMapping("updateType")
    public ResponseEntity<?> updateType(@RequestBody @Valid TypeRequest idType, @RequestParam("userId") long userId, BindingResult bindingResult, HttpServletRequest request){
        String log = String.format("Modifying ID Type with name %s",idType.getTypeName());
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //Validate request object
        logger.info("Validating ID Type record :: ");
        if (bindingResult.hasErrors()) {
            logger.info("ID Type record Validation error....");
            ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
            logger.info(String.format("%s", validationException.getMessage()));
            return exceptionHandler.validationExceptionHandler(validationException, request);
        }

        try{
            //check whether type with name is not in use
            logger.info("Checking whether ID type assigned name is not in use...");
            String typeName = idType.getTypeName();
            boolean exists = this.service.checkTypeDuplicateNameWithDifferentIds(typeName, idType.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Type with name '%s' exists", typeName));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("ID Type","Name", typeName),
                        request);
            }

            //update ID Type record in database
            this.service.updateIdType(idType);

            //return Type record
            return new ResponseEntity<>(idType, HttpStatus.OK);

        } catch(Exception ex) {
            logger.info("An error occurred in method 'updateType'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @DeleteMapping("softDeleteType")
    public ResponseEntity<?> softDeleteType(@RequestParam Long id, @RequestParam boolean isDeleted, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Deleting ID Type record. ID Type with id %s is_deleted set to %s",id, isDeleted ?"True":"False");
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.idTypeExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("ID Type", "id",id),
                    request);
        }

        //delete ID Type
        service.softDeleteIdType(id, isDeleted);

        //return result
        return new ResponseEntity<>("ID Type updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/deleteType")
    public ResponseEntity<?> deleteType(@RequestParam Long id, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Deleting ID Type record. ID with id %s is being deleted",id);
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.idTypeExists(id);
        Boolean exists = existResult.join();
        if (!exists) {
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("ID Type", "id",id),
                    request);
        }

        //delete ID type
        service.deleteIdType(id);

        //return result
        return new ResponseEntity<>("ID Type deleted successfully", HttpStatus.OK);
    }

    //endregion

    //region Identifications

    @GetMapping("findIdentification")
    public ResponseEntity<?> findIdentification(@RequestParam("id") long id, @RequestParam("userId") long userId,HttpServletRequest request){
        try {
            String log = String.format("Retrieve Identification with ID'%s'", id);
            logger.info(log);
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));
            CompletableFuture<IdentificationRequest> result = this.service.findIdentificationById(id);
            IdentificationRequest record = result.join();

            //...identification not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("Resource not found! Identification with ID'%s' not found", id));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Identification","ID", id),
                        request);
            }

            //return identification record
            return new ResponseEntity<>(record, HttpStatus.OK);

        } catch(Exception ex) {
            //...exception object
            logger.info("An error occurred in method 'findIdentification'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()),
                    request);
        }
    }

    @GetMapping("getIdentifications")
    public ResponseEntity<?> getIdentifications(@RequestParam Long ownerId, @RequestParam("userId") long userId,HttpServletRequest request) {
        String log = String.format("Retrieving a list of identifications for owner with Owner ID %s", ownerId);
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));
        try{
            CompletableFuture<List<IdentificationRequest>> identifications = service.getIdentifications(ownerId);
            List<IdentificationRequest> records = identifications.join();
            return ResponseEntity.ok(records);

        } catch(Exception ex) {
            logger.info("An error occurred in method 'getIdentifications'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @GetMapping("checkIdentification")
    public ResponseEntity<?> checkIdentification(@RequestParam("idNumber") String idNumber, @RequestParam("userId") long userId,HttpServletRequest request) {
        String log = String.format("Check if ID document with ID No. '%s' exists", idNumber);
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        try {
            //...check if exists
            CompletableFuture<Boolean> record = this.service.identificationExists(idNumber);
            boolean result = record.join();

            //return ID document record
            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch(Exception ex){
            logger.info("An error occurred in method 'checkIdentification'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @PostMapping("createIdentification")
    public ResponseEntity<?> createId(@RequestBody @Valid IdentificationRequest identification, @RequestParam("userId") long userId,BindingResult bindingResult, HttpServletRequest request) {
        //..creation date
        String log = String.format("Creating new ID Document %s", identification.getIdNo());
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        try {
            //Validate request object
            logger.info("Validating ID document record :: ");
            if (bindingResult.hasErrors()) {
                logger.info("ID document record Validation error....");
                ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
                logger.info(String.format("%s", validationException.getMessage()));
                return exceptionHandler.validationExceptionHandler(validationException, request);
            }

            //...check for duplicate ID No
            logger.info("Checking whether ID type assigned name is not in use...");
            String docNumber = identification.getIdNo();
            CompletableFuture<Boolean> recordExists = this.service.identificationExists(docNumber);
            boolean exists = recordExists.join();
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Document with ID NO. '%s' exists", docNumber));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Identification","ID No", docNumber),
                        request);
            }

            //...save ID document
            CompletableFuture<IdentificationRequest> record = this.service.createIdentification(identification);
            IdentificationRequest result = record.join();

            //return ID document record
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (InterruptedException ex) {
            //log error
            logger.info("Error creating ID document");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            return exceptionHandler.threadCanceledHandler(new CanceledException(), request);
        } catch(Exception ex) {
            logger.info("An error occurred in method 'createId'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @PutMapping("updateIdentification")
    public ResponseEntity<?> updateId(@RequestBody @Valid IdentificationRequest identification, @RequestParam("userId") long userId,BindingResult bindingResult, HttpServletRequest request){
        String log = String.format("Modifying Identification Document with no. %s",identification.getIdNo());
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //Validate request object
        logger.info("Validating ID Document record :: ");
        if (bindingResult.hasErrors()) {
            logger.info("ID Type record Validation error....");
            ValidationException validationException = new ValidationException(Generators.buildErrorMessage(bindingResult));
            logger.info(String.format("%s", validationException.getMessage()));
            return exceptionHandler.validationExceptionHandler(validationException, request);
        }

        try{
            //check whether ID Document with ID No. is not in use
            logger.info("Checking whether ID type assigned name is not in use...");
            String docNumber = identification.getIdNo();
            boolean exists = this.service.checkIdNumberDuplicateWithDifferentIds(docNumber, identification.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another ID Document with ID NO. '%s' exists", docNumber));
                return exceptionHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Identification","ID No", docNumber),
                        request);
            }

            //update ID Document record in database
            this.service.updateIdentification(identification);

            //return Document record
            return new ResponseEntity<>(identification, HttpStatus.OK);
        } catch(Exception ex) {
            logger.info("An error occurred in method 'updateId'");
            logger.error(ex.getMessage());
            logger.info("StackTrace Details::");
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            logger.info(stackTrace);
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            return exceptionHandler.errorHandler(new GeneralException(ex.getMessage()), request);
        }
    }

    @DeleteMapping("softDeleteIdentification")
    public ResponseEntity<?> softDeleteIdentification(@RequestParam Long id, @RequestParam boolean isDeleted, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Deleting Identification record. Identification with id %s is_deleted set to %s",id, isDeleted ?"True":"False");
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.identificationExistsById(id);
        Boolean exists = existResult.join();
        if (!exists) {
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Identification", "id",id),
                    request);
        }

        //delete Identification
        service.softDeleteIdentification(id, isDeleted);

        //return result
        return new ResponseEntity<>("Identification updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("deleteIdentification")
    public ResponseEntity<?> deleteIdentification(@RequestParam Long id, @RequestParam("userId") long userId, HttpServletRequest request) {
        String log = String.format("Deleting identification record. Identification id %s is being deleted",id);
        logger.info(log);
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("%s by user with id %s",log, userId)));

        //...check if record exists by ID
        CompletableFuture<Boolean> existResult = this.service.identificationExistsById(id);
        Boolean exists = existResult.join();
        if (!exists) {
            return exceptionHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Identification", "id", id),
                    request);
        }

        //delete Identification
        service.deleteIdentification(id);

        //return result
        return new ResponseEntity<>("Identification deleted successfully", HttpStatus.OK);
    }

    //endregion

    //region protected methods

    private LogRequest generateLog(long userId, String ipAddress, String action){
        LogRequest log = new LogRequest();
        log.setAction(action);
        log.setLogTime(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        log.setUser(String.format("%s", userId));
        log.setDeleted(false);
        return log;
    }

    //endregion

    //region test requests
    @GetMapping("/hello")
    public String SayHello(){
        return "Hello from ATTACHMENTS";
    }
    //endregion

}

