package com.pbu.wendi.controllers;

import com.pbu.wendi.requests.sam.dto.*;
import com.pbu.wendi.services.sam.services.*;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.common.NetworkService;
import com.pbu.wendi.utils.exceptions.*;
import com.pbu.wendi.utils.helpers.Generators;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("sam")
public class SamController {

    private final ApplicationExceptionHandler errorHandler;
    private final AppLoggerService logger;
    private final NetworkService networkService;
    private final AppService appService;
    private final PermissionService permissionService;
    private final SetService setService;
    private final UserService userService;
    private final RoleService roleService;
    private final ArchivedLogService aLogService;
    private final ArchivedUserService aUserService;
    private final BranchService branchService;
    private final LogService logService;

    public SamController(ApplicationExceptionHandler errorHandler,
                         AppLoggerService logger,
                         NetworkService networkService,
                         AppService appService,
                         PermissionService permissionService,
                         SetService setService,
                         UserService userService,
                         RoleService roleService,
                         ArchivedLogService aLogService,
                         ArchivedUserService aUserService,
                         BranchService branchService,
                         LogService logService) {
        this.errorHandler = errorHandler;
        this.logger = logger;
        this.networkService = networkService;
        this.appService = appService;
        this.permissionService = permissionService;
        this.setService = setService;
        this.userService = userService;
        this.roleService = roleService;
        this.aLogService = aLogService;
        this.aUserService = aUserService;
        this.branchService = branchService;
        this.logService = logService;
    }

    //region apps

    @GetMapping("/getAppWithId/{id}/{userId}")
    public ResponseEntity<?>getAppWithId(@PathVariable("id") long id, @PathVariable("userId") long userId, HttpServletRequest request){
        logger.info(String.format("Retrieve app with id %s by user with id %s", id, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        AppRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve app with id %s by user with id %s", id, userId)));

            //Retrieve record
            CompletableFuture<AppRequest> app = this.appService.findById(id);
            record = app.join();

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //Record not found, throw NOT_FOUND response
        if(record == null){
            logger.info(String.format("App with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", id, userId));
            return errorHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("App","ID",id),
                    request);
        }

        //return app record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAppWithName/{appName}/{userId}")
    public ResponseEntity<?>getAppWithName(@PathVariable("appName") String appName, @PathVariable("userId") long userId, HttpServletRequest request){
        logger.info(String.format("Retrieve app with name %s by user with id %s", appName, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        //Retrieve record
        AppRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve app with name %s by user with id %s", appName, userId)));

            //Retrieve record
            CompletableFuture<AppRequest> app = this.appService.findByName(appName);
            record = app.join();

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //Record not found, throw NOT_FOUND response
        if(record == null){
            return errorHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("App","Name",appName),
                    request);
        }

        //return app record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getAllUserApps/{userId}")
    public ResponseEntity<?>getAllUserApps(@PathVariable("userId") long userId, HttpServletRequest request){
        logger.info(String.format("Retrieve all apps for user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<AppRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all apps for user with id %s", userId)));

            CompletableFuture<List<AppRequest>> futureRecord = appService.findAll(userId);
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createApp/{userId}")
    public ResponseEntity<?> createApp(@RequestBody @Valid AppRequest app, @PathVariable("userId") long userId,
                                       BindingResult bindingResult, HttpServletRequest request) {
        logger.info(String.format("Create new app record by user with ID %s", userId));

        //...get user IP address
        String ip = networkService.getIncomingIpAddress(request);

        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        //Retrieve record
        AppRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Create new app record by user with ID %s",userId)));

            CompletableFuture<AppRequest> futureRecord = appService.create(app);
            record = futureRecord.get();
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        if (record == null || record.getId() == 0) {
            logger.error("Record not save. An error occurred");
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving app record"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    //endregion

    //region branches

    @GetMapping("/getBranchWithId/{branchId}/{userId}")
    public ResponseEntity<?>getBranchWithId(@PathVariable("branchId") long branchId,
                                            @PathVariable("userId") long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Retrieve branch with id %s by user with id %s", branchId, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        BranchRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve branch with ID %s by user with id %s", branchId, userId)));

            //Retrieve record
            CompletableFuture<BranchRequest> app = this.branchService.findById(branchId);
            record = app.join();
            if(record == null){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branchId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",branchId),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return branch record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getBranchWithSolId/{solId}/{userId}")
    public ResponseEntity<?>getBranchWithSolId(@PathVariable("solId") String solId,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Retrieve branch with Solid %s by user with id %s", solId, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        BranchRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve branch with name %s by user with id %s", solId, userId)));

            //Retrieve record
            CompletableFuture<BranchRequest> app = this.branchService.findBySolId(solId);
            record = app.join();

            //Record not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("Branch with id %s retrieval by user with solid %s failed. Returned a 404 code - Resource not found", solId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",solId),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return branch record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getBranchWithName/{branchName}/{userId}")
    public ResponseEntity<?>getBranchWithName(@PathVariable("branchName") String name,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Retrieve branch with name %s by user with id %s", name, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        BranchRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve branch with name %s by user with id %s", name, userId)));

            //Retrieve record
            CompletableFuture<BranchRequest> app = this.branchService.findByName(name);
            record = app.join();

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //Record not found, throw NOT_FOUND response
        if(record == null){
            logger.info(String.format("Branch with id %s retrieval by user with name %s failed. Returned a 404 code - Resource not found", name, userId));
            return errorHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("Branch","BranchName",name),
                    request);
        }

        //return branch record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getBranches/{userId}")
    public ResponseEntity<?>getBranches(@PathVariable("userId") long userId, HttpServletRequest request){
        logger.info(String.format("Retrieve all branches for user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<BranchRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all apps for user with id %s", userId)));

            CompletableFuture<List<BranchRequest>> futureRecord = branchService.getAll();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createBranch/{userId}")
    public ResponseEntity<?> createBranch(@RequestBody @Valid BranchRequest branch,
                                          @PathVariable("userId") long userId,
                                          BindingResult bindingResult,
                                          HttpServletRequest request) {
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Create new branch by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);
        //log user activity
        logService.create(generateLog(userId, ip, String.format("Create new branch by user with id %s", userId)));

        BranchRequest record;
        try {

            //check whether branch name is not in use
            logger.info(String.format("Checking whether branch assigned name is not in use... by user with ID %s", userId));
            String branchName = branch.getBranchName();
            boolean exists = this.branchService.checkIfExistsByName(branchName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another branch with name '%s' exists", branchName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Branch", "Name", branchName),
                        request);
            }

            //check whether branch Sol ID is not in use
            logger.info("Checking whether branch assigned SOLID is not in use...");
            String solId = branch.getSolId();
            exists = this.branchService.checkIfExistsBySolId(solId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another branch with SolId '%s' exists", solId));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Branch", "SolID", solId),
                        request);
            }

            CompletableFuture<BranchRequest> futureRecord = branchService.create(branch);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/updateBranch/{userId}")
    public ResponseEntity<?> updateBranch(@RequestBody @Valid BranchRequest branch,
                                          @PathVariable("userId") long userId,
                                          BindingResult bindingResult, HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Modify branch by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Modify branch by user with id %s", userId)));

        BranchRequest record;
        try {

            CompletableFuture<BranchRequest> app = this.branchService.findById(branch.getId());
            record = app.join();
            if(record == null){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branch.getId(), userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",branch.getId()),
                        request);
            }

            //check whether branch name is not in use
            logger.info(String.format("Checking whether branch assigned name is not in use... by user with ID %s", userId));
            String branchName = branch.getBranchName();
            boolean exists = this.branchService.checkNameDuplication(branchName, branch.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another branch with name '%s' exists", branchName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Branch", "Name", branchName),
                        request);
            }

            //check whether branch Sol ID is not in use
            logger.info("Checking whether branch assigned SOLID is not in use...");
            String solId = branch.getSolId();
            exists = this.branchService.checkSolIdDuplication(solId, branch.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another branch with SolId '%s' exists", solId));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Branch", "SolID", solId),
                        request);
            }

            branchService.updateBranch(branch);

            //..return updated record
            app = this.branchService.findById(branch.getId());
            record = app.join();
        } catch (Exception e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/deleteBranch/{branchId}/{userId}")
    public ResponseEntity<?>deleteBranch(@PathVariable Long branchId,
                                         @PathVariable("userId") long userId,
                                         HttpServletRequest request){
        logger.info(String.format("Delete branch with ID '%s' by user with id %s" ,branchId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Delete branch with ID '%s' by user with id %s", branchId, userId)));

            CompletableFuture<BranchRequest> app = this.branchService.findById(branchId);
            BranchRequest record = app.join();
            if(record == null){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branchId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",branchId),
                        request);
            }

            branchService.deleteBranch(branchId);
            return new ResponseEntity<>("Branch record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PostMapping("/activateBranch/{branchId}/{active}/{userId}")
    public ResponseEntity<?>activateBranch(@PathVariable Long branchId,
                                           @PathVariable Boolean active,
                                           @PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Modify branch active status by user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Modify branch '%s' active status to '%s' by user with id %s", branchId, active, userId)));

            CompletableFuture<BranchRequest> app = this.branchService.findById(branchId);
            BranchRequest record = app.join();
            if(record == null){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branchId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",branchId),
                        request);
            }

            branchService.activateBranch(branchId, active);
            return new ResponseEntity<>("Branch record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PostMapping("/softDeleteBranch/{branchId}/{isDeleted}/{userId}")
    public ResponseEntity<?>softDeleteBranch(@PathVariable Long branchId,
                                             @PathVariable Boolean isDeleted,
                                             @PathVariable("userId") long userId,
                                             HttpServletRequest request){
        logger.info(String.format("Mark branch '%s' as deleted by user with id %s" ,branchId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Modify branch '%s' IsDeleted status to '%s' by user with id %s", branchId, isDeleted, userId)));

            CompletableFuture<BranchRequest> app = this.branchService.findById(branchId);
            BranchRequest record = app.join();
            if(record == null){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branchId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","ID",branchId),
                        request);
            }

            branchService.softDelete(branchId, isDeleted);
            return new ResponseEntity<>("Branch record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    //endregion

    //region permissions
    @GetMapping("/getPermissionById/{permissionId}/{userId}")
    public ResponseEntity<?> getPermissionById(@PathVariable Long permissionId,
                                               @PathVariable Long userId,
                                               HttpServletRequest request){

        logger.info(String.format("Retrieve permission record with ID '%s' by user with ID '%s'" ,permissionId, userId));

        //..create log record
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("Retrieve permission record with ID '%s' by user with ID '%s", permissionId, userId)));

        PermissionRequest record;
        try {
            CompletableFuture<PermissionRequest> futureRecord = permissionService.findById(permissionId);
            record = futureRecord.get();
            if(record == null){
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Permission", "Id", String.format("%s", permissionId)),
                        request);
            }
        } catch (InterruptedException e) {
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getPermissions/{userId}")
    public ResponseEntity<?> getPermissions(@PathVariable Long userId,
                                            HttpServletRequest request){
        logger.info(String.format("Retrieve all permissions by user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<PermissionRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all permissions by user with id %s", userId)));

            CompletableFuture<List<PermissionRequest>> futureRecord = permissionService.findPermissions();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getPermissionById/{permissionSetId}/{userId}")
    public ResponseEntity<?> getSetPermissions(@PathVariable Long permissionSetId,
                                               @PathVariable Long userId,
                                               HttpServletRequest request){
        logger.info(String.format("Retrieve all permissions for a permission set wit ID '%s' by user with id %s", permissionSetId, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<PermissionRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all permissions for a permission set wit ID '%s' by user with id %s", permissionSetId, userId)));

            CompletableFuture<List<PermissionRequest>> futureRecord = permissionService.findPermissions(permissionSetId);
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/lockSetPermissions/{permissionSetId}/{isLocked}/{userId}")
    public ResponseEntity<?> lockSetPermissions(@PathVariable Long permissionSetId,
                                                @PathVariable boolean isLocked,
                                                @PathVariable Long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Set lock status for permissions assigned to a permission set with ID '%s' to '%s' by user with ID '%s'" ,permissionSetId, isLocked, userId));
        String ip = networkService.getIncomingIpAddress(request);
        try {

            logService.create(generateLog(userId, ip, String.format("Lock permissions for permission set with ID '%s' by user with id %s", permissionSetId, userId)));
            CompletableFuture<SetRequest> org = this.setService.findById(permissionSetId);
            SetRequest record = org.join();
            if(record == null){
                logger.info(String.format("Permission set with id %s not found. Returned a 404 code - Resource not found", permissionSetId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Permission set","ID",permissionSetId),
                        request);
            }

            this.permissionService.lockSetPermissions(permissionSetId, isLocked);
            return new ResponseEntity<>("Permissions locked successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    @PostMapping("/updatePermission/{userId}")
    public ResponseEntity<?> updatePermission(@RequestBody @Valid PermissionRequest permission,
                                              @PathVariable("userId") long userId,
                                              BindingResult bindingResult,
                                              HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Modify permission with ID '%s' by user with id %s", permission.getId(), userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Modify permission with ID '%s' by user with id %s", permission.getId(), userId)));

        PermissionRequest record;
        try {

            CompletableFuture<PermissionRequest> perm = this.permissionService.findById(permission.getId());
            record = perm.join();
            if(record == null){
                logger.info(String.format("Permission with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", permission.getId(), userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Permission","ID",permission.getId()),
                        request);
            }

            //check whether permission name is not in use
            logger.info(String.format("Checking whether permission assigned name is not in use... by user with ID %s", userId));
            String permName = permission.getName();
            boolean exists = this.permissionService.checkNameDuplication(permName, permission.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another permission with name '%s' exists", permName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Permission", "Name", permName),
                        request);
            }

            //check whether permission Description is not in use
            logger.info("Checking whether permission assigned description is not in use...");
            String descr = permission.getDescription();
            exists = this.permissionService.checkDescriptionDuplication(descr, permission.getId());
            if(exists){
                logger.info(String.format("Resource Conflict! Another permission with description '%s' exists", descr));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Permission", "Description", descr),
                        request);
            }

            permissionService.update(permission);

            //..return updated record
            perm = this.permissionService.findById(permission.getId());
            record = perm.join();
        } catch (Exception e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    //endregion

    //region permission sets
    @GetMapping("/getPermissionSetWithId/{setId}/{userId}")
    public ResponseEntity<?>getPermissionSetWithId(@PathVariable("setId") long setId,
                                                   @PathVariable("userId") long userId,
                                                   HttpServletRequest request){
        logger.info(String.format("Retrieve permission set with id %s by user with id %s", setId, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        SetRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve permission set with ID %s by user with id %s", setId, userId)));

            //Retrieve record
            CompletableFuture<SetRequest> app = this.setService.findById(setId);
            record = app.join();
            if(record == null){
                logger.info(String.format("PermissionSet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", setId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("PermissionSet","SetId",setId),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return permission set record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getPermissionSetWithName/{setName}/{userId}")
    public ResponseEntity<?>getPermissionSetWithName(@PathVariable("setName") String setName,
                                                     @PathVariable("userId") long userId,
                                                     HttpServletRequest request){
        logger.info(String.format("Retrieve permission with name %s by user with id %s", setName, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        SetRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve permission set with name %s by user with id %s", setName, userId)));

            //Retrieve record
            CompletableFuture<SetRequest> app = this.setService.findBySetName(setName);
            record = app.join();

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //Record not found, throw NOT_FOUND response
        if(record == null){
            logger.info(String.format("PermissionSet with id %s retrieval by user with name %s failed. Returned a 404 code - Resource not found", setName, userId));
            return errorHandler.resourceNotFoundExceptionHandler(
                    new NotFoundException("PermissionSet","SetName",setName),
                    request);
        }

        //return branch record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/softDeletePermissionSet/{setId}/{isDeleted}/{userId}")
    public ResponseEntity<?>softDeletePermissionSet(@PathVariable Long setId,
                                                    @PathVariable Boolean isDeleted,
                                                    @PathVariable("userId") long userId,
                                                    HttpServletRequest request){
        logger.info(String.format("Mark PermissionSet '%s' as deleted by user with id %s" ,setId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Modify PermissionSet '%s' IsDeleted status to '%s' by user with id %s", setId, isDeleted, userId)));

            CompletableFuture<SetRequest> app = this.setService.findById(setId);
            SetRequest record = app.join();
            if(record == null){
                logger.info(String.format("PermissionSet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", setId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("PermissionSet","SetId",setId),
                        request);
            }

            setService.softDelete(setId, isDeleted);
            return new ResponseEntity<>("PermissionSet record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    @PostMapping("/lockPermissionSet/{setId}/{isLocked}/{userId}")
    public ResponseEntity<?>lockPermissionSet(@PathVariable Long setId,
                                              @PathVariable Boolean isLocked,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request){
        logger.info(String.format("Modify permissionSet lock status by user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Modify permissionSet '%s' active status to '%s' by user with id %s", setId, isLocked, userId)));

            CompletableFuture<SetRequest> app = this.setService.findById(setId);
            SetRequest record = app.join();
            if(record == null){
                logger.info(String.format("PermissionSet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", setId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("PermissionSet","SetId",setId),
                        request);
            }

            setService.lockSet(setId, isLocked);
            return new ResponseEntity<>("PermissionSet record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    @PostMapping("/createPermissionSet/{userId}")
    public ResponseEntity<?> createPermissionSet(@RequestBody @Valid SetRequest permissionSet,
                                                 @PathVariable("userId") long userId,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Create new permissionSet by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);
        //log user activity
        logService.create(generateLog(userId, ip, String.format("Create new permissionSet by user with id %s", userId)));

        SetRequest record;
        try {

            //check whether permission set name is not in use
            logger.info(String.format("Checking whether permissionSet assigned name is not in use... by user with ID %s", userId));
            String setName = permissionSet.getSetName();
            boolean exists = this.setService.nameInUse(setName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another PermissionSet with name '%s' exists", setName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("PermissionSet", "Name", setName),
                        request);
            }

            //check whether permissionSet Description is not in use
            logger.info("Checking whether PermissionSet assigned Description is not in use...");
            String descr = permissionSet.getDescription();
            exists = this.setService.descriptionInUse(descr);
            if(exists){
                logger.info(String.format("Resource Conflict! Another permission set with Description '%s' exists", descr));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("PermissionSet", "Description", descr),
                        request);
            }

            CompletableFuture<SetRequest> futureRecord = setService.create(permissionSet);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving permission set"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/assignPermissionsToPermissionSet/{userId}")
    public ResponseEntity<?> assignPermissionsToPermissionSet(@RequestBody @Valid SetRequest permissionSet,
                                                              @PathVariable("userId") long userId,
                                                              HttpServletRequest request){

        long setId = permissionSet.getId();
        logger.info(String.format("Assign permissions to permission set with id %s by user with id %s",setId , userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        SetRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Assign permissions to permission set with ID %s by user with id %s", setId, userId)));

            //Retrieve record
            CompletableFuture<SetRequest> ps = this.setService.findById(setId);
            record = ps.join();
            if(record == null){
                logger.info(String.format("PermissionSet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", setId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("PermissionSet","SetId",setId),
                        request);
            }

            //..set permissions
            ps = setService.update(permissionSet);
            record = ps.join();
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return permission set record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/updatePermissionSet/{userId}")
    public ResponseEntity<?> updatePermissionSet(@RequestBody @Valid SetRequest permissionSet,
                                                 @PathVariable("userId") long userId,
                                                 BindingResult bindingResult,
                                                 HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Modify permission set by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Modify permission set by user with id %s", userId)));

        SetRequest record;
        try {

            long setId = permissionSet.getId();
            CompletableFuture<SetRequest> ps = this.setService.findById(setId);
            record = ps.join();
            if(record == null){
                logger.info(String.format("Permission with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", setId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("PermissionSet","ID",setId),
                        request);
            }

            //check whether permissionSet name is not in use
            logger.info(String.format("Checking whether permissionSet assigned name is not in use... by user with ID %s", userId));
            String setName = permissionSet.getSetName();
            boolean exists = this.setService.checkNameDuplication(setName, setId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another permissionSet with name '%s' exists", setName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("PermissionSet", "Name", setName),
                        request);
            }

            //check whether permissionSet Description is not in use
            logger.info("Checking whether permissionSet assigned description is not in use...");
            String descr = permissionSet.getDescription();
            exists = this.setService.checkNameDuplication(descr, setId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another permissionSet with description '%s' exists", descr));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("PermissionSet", "SolID", descr),
                        request);
            }

            setService.update(permissionSet);

            //..return updated record
            ps = this.setService.findById(permissionSet.getId());
            record = ps.join();
        } catch (Exception e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving permissionSet"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/deletePermissionSet/{setId}/{userId}")
    public ResponseEntity<?>deletePermissionSet(@PathVariable Long setId,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Delete permissionSet with ID '%s' by user with id %s" ,setId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Delete permissionSet with ID '%s' by user with id %s", setId, userId)));

            CompletableFuture<SetRequest> app = this.setService.findById(setId);
            SetRequest record = app.join();
            if(record == null){
                logger.info(String.format("PermissionSet with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", setId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("PermissionSet","SetId",setId),
                        request);
            }

            branchService.deleteBranch(setId);
            return new ResponseEntity<>("PermissionSet record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }
    }

    //endregion

    //region role
    @GetMapping("/getRoleById/{roleId}/{userId}")
    public ResponseEntity<?> getRoleById(@PathVariable Long roleId,
                                         @PathVariable Long userId,
                                         HttpServletRequest request){

        logger.info(String.format("Retrieve role record with ID '%s' by user with ID '%s'" ,roleId, userId));

        //..create log record
        String ip = networkService.getIncomingIpAddress(request);
        logService.create(generateLog(userId, ip, String.format("Retrieve role record with ID '%s' by user with ID '%s", roleId, userId)));

        RoleRequest record;
        try {
            CompletableFuture<RoleRequest> futureRecord = roleService.findById(roleId);
            record = futureRecord.get();
            if(record == null){
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role", "Id", String.format("%s", roleId)),
                        request);
            }
        } catch (InterruptedException e) {
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getRoles/{userId}")
    public ResponseEntity<?> getRoles(@PathVariable Long userId,
                                      HttpServletRequest request){
        logger.info(String.format("Retrieve all roles by user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<RoleRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all roles by user with id %s", userId)));

            CompletableFuture<List<RoleRequest>> futureRecord = roleService.findRolesAll();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }
    @PostMapping("/updateRole/{userId}")
    public ResponseEntity<?> updateRole(@RequestBody @Valid RoleRequest role,
                                        @PathVariable("userId") long userId,
                                        BindingResult bindingResult,
                                        HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        long roleId = role.getId();
        logger.info(String.format("Modify role with ID '%s' by user with id %s", roleId, userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Modify role with ID '%s' by user with id %s", roleId, userId)));
        RoleRequest record;
        try {

            CompletableFuture<RoleRequest> rRecord = this.roleService.findById(roleId);
            record = rRecord.join();
            if(record == null){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", roleId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId",roleId),
                        request);
            }

            //check whether role name is not in use
            logger.info(String.format("Checking whether role assigned name is not in use... by user with ID %s", userId));
            String roleName = role.getName();
            boolean exists = this.roleService.checkNameDuplication(roleName, roleId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another role with name '%s' exists", roleName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Role", "Name", roleName),
                        request);
            }

            //check whether role Description is not in use
            logger.info("Checking whether role assigned description is not in use...");
            String descr = role.getDescription();
            exists = this.roleService.checkDescriptionDuplication(descr, roleId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another permission with description '%s' exists", descr));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Permission", "Description", descr),
                        request);
            }
            roleService.update(role);

            //..return updated record
            rRecord = this.roleService.findById(roleId);
            record = rRecord.join();
        } catch (Exception e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/assignPermissionSetToRole/{userId}")
    public ResponseEntity<?> assignPermissionSetToRole(@RequestBody @Valid RoleRequest role,
                                                       @PathVariable("userId") long userId,
                                                       HttpServletRequest request){
        long roleId = role.getId();
        logger.info(String.format("Assign permissions sets to role set with id %s by user with id %s",roleId , userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        RoleRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Assign permissions set to role with ID %s by user with id %s", roleId, userId)));

            //Retrieve record
            CompletableFuture<RoleRequest> ps = this.roleService.findById(roleId);
            record = ps.join();
            if(record == null){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", roleId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId",roleId),
                        request);
            }

            //..set permissions sets
            ps = roleService.updateRolePermissions(role);
            record = ps.join();
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return role record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/createRole/{userId}")
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleRequest role,
                                        @PathVariable("userId") long userId,
                                        BindingResult bindingResult,
                                        HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Create new role by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);
        //log user activity
        logService.create(generateLog(userId, ip, String.format("Create new role by user with id %s", userId)));
        RoleRequest record;
        try {

            //check whether role name is not in use
            logger.info(String.format("Checking whether role assigned name is not in use... by user with ID %s", userId));
            String roleName = role.getName();
            boolean exists = this.roleService.nameInUse(roleName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another role with name '%s' exists", roleName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Role", "Name", roleName),
                        request);
            }

            //check whether role Description is not in use
            logger.info("Checking whether role assigned Description is not in use...");
            String descr = role.getDescription();
            exists = this.roleService.descriptionInUse(descr);
            if(exists){
                logger.info(String.format("Resource Conflict! Another role with Description '%s' exists", descr));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("Role", "RoleId", descr),
                        request);
            }

            CompletableFuture<RoleRequest> futureRecord = roleService.create(role);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving role"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/deleteRole/{roleId}/{userId}")
    public ResponseEntity<?>deleteRole(@PathVariable Long roleId,
                                       @PathVariable("userId") long userId,
                                       HttpServletRequest request){
        logger.info(String.format("Delete role with ID '%s' by user with id %s" ,roleId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Delete role with ID '%s' by user with id %s", roleId, userId)));

            CompletableFuture<RoleRequest> app = this.roleService.findById(roleId);
            RoleRequest record = app.join();
            if(record == null){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", roleId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId",roleId),
                        request);
            }

            roleService.delete(roleId);
            return new ResponseEntity<>("Role record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PostMapping("/softDeleteRole/{roleId}/{isDeleted}/{userId}")
    public ResponseEntity<?>softDeleteRole(@PathVariable Long roleId,
                                           @PathVariable Boolean isDeleted,
                                           @PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Mark role '%s' as deleted by user with id %s" ,roleId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Modify role '%s' IsDeleted status to '%s' by user with id %s", roleId, isDeleted, userId)));

            CompletableFuture<RoleRequest> app = this.roleService.findById(roleId);
            RoleRequest record = app.join();
            if(record == null){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", roleId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId",roleId),
                        request);
            }

            roleService.softDelete(roleId, isDeleted);
            return new ResponseEntity<>("Role record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    //endregion

    //region users
    @GetMapping("/getUserWithId/{recordId}/{userId}")
    public ResponseEntity<?>getUserWithId(@PathVariable("recordId") long recordId,
                                          @PathVariable("userId") long userId,
                                          HttpServletRequest request){
        logger.info(String.format("Retrieve user with UserID %s by user with id %s", recordId, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        UserRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve user with UserID %s by user with id %s", recordId, userId)));

            //Retrieve record
            CompletableFuture<UserRequest> userRecord = this.userService.findById(recordId);
            record = userRecord.join();

            //Record not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("User with id %s retrieval by user with UserId %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","UserId",recordId),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return user record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getUserWithUsername/{username}/{userId}")
    public ResponseEntity<?>getUserWithUsername(@PathVariable("username") String username,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){
        logger.info(String.format("Retrieve user with Username %s by user with id %s", username, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        UserRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve user with Username %s by user with id %s", username, userId)));

            //Retrieve record
            CompletableFuture<UserRequest> userRecord = this.userService.findByUsername(username);
            record = userRecord.join();

            //Record not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("User with Username %s retrieval by user with Username %s failed. Returned a 404 code - Resource not found", username, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","Username",username),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return user record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getUserWithEmail/{email}/{userId}")
    public ResponseEntity<?>getUserWithEmail(@PathVariable("email") String email,
                                             @PathVariable("userId") long userId,
                                             HttpServletRequest request){
        logger.info(String.format("Retrieve user with email %s by user with id %s", email, userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        UserRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve user with email %s by user with id %s", email, userId)));

            //Retrieve record
            CompletableFuture<UserRequest> userRecord = this.userService.findByEmail(email);
            record = userRecord.join();

            //Record not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("User with Username %s retrieval by user with email %s failed. Returned a 404 code - Resource not found", email, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","Email",email),
                        request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return user record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getLoginInfo/{username}")
    public ResponseEntity<?>getLoginInfo(@PathVariable("username") String username,
                                         HttpServletRequest request){
        logger.info(String.format("Retrieve user with Username '%s'", username));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        LoginRequest record;
        try{
            //log user activity
            logService.create(generateLog(0, ip, String.format("Retrieve user login details for user with Username '%s'", username)));

            //Retrieve record
            CompletableFuture<LoginRequest> userRecord = this.userService.loginUser(username);
            record = userRecord.join();

            //Record not found, throw NOT_FOUND response
            if(record == null){
                logger.info(String.format("User with Username '%s' retrieval failed. Returned a 404 code - Resource not found", username));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","Username",username),
                        request);
            }

            //...get user permissions
            long roleId = record.getRoleId();
            CompletableFuture<List<PermissionRequest>> permissionRecords = permissionService.findPermissionsByRoleId(roleId);
            List<PermissionRequest> permissions = permissionRecords.get();

            List<String> assignedPermissions = new ArrayList<>();
            if(!permissions.isEmpty()){
                for(PermissionRequest permission : permissions){
                    assignedPermissions.add(permission.getName());
                }
            }
            record.setPermissions(assignedPermissions);

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        //return user record
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/getUsers/{userId}")
    public ResponseEntity<?>getUsers(@PathVariable("userId") long userId, HttpServletRequest request){
        logger.info(String.format("Retrieve all users by user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<UserRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all users by user with id %s", userId)));

            CompletableFuture<List<UserRequest>> futureRecord = userService.findAll();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getActiveUsers/{userId}")
    public ResponseEntity<?>getActiveUsers(@PathVariable("userId") long userId, HttpServletRequest request){
        logger.info(String.format("Retrieve only active user by user with id %s", userId));
        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<UserRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve only active user by user with id %s", userId)));

            CompletableFuture<List<UserRequest>> futureRecord = userService.findActive();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createUser/{userId}")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest user,
                                        @PathVariable("userId") long userId,
                                        BindingResult bindingResult,
                                        HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        logger.info(String.format("Create new user record by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Create new user record by user with id %s", userId)));
        UserRequest record;
        try {

            //check whether username is not in use
            logger.info(String.format("Checking whether user assigned Username is not in use... by user with ID %s", userId));
            String userName = user.getUsername();
            boolean exists = this.userService.usernameTaken(userName);
            if(exists){
                logger.info(String.format("Resource Conflict! Another user with Username '%s' exists", userName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("User", "Username", userName),
                        request);
            }

            //check whether pf_no is not in use
            logger.info("Checking whether user assigned PF_NO is not in use...");
            String pfNo = user.getPfNo();
            exists = this.userService.pfNoTaken(pfNo);
            if(exists){
                logger.info(String.format("Resource Conflict! Another user with PF_NO '%s' exists", pfNo));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("User", "PF_NO", pfNo),
                        request);
            }
            //check whether email is not in use
            logger.info("Checking whether user assigned email is not in use...");
            String email = user.getEmail();
            exists = this.userService.emailTaken(email);
            if(exists){
                logger.info(String.format("Resource Conflict! Another user with email '%s' exists", email));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("User", "Email", email),
                        request);
            }

            //...check if branch exists
            long branchId = user.getBranchId();
            boolean branchFound = this.branchService.checkIfExistsById(branchId);
            if(!branchFound){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branchId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","BranchId", branchId),
                        request);
            }

            //...check if role exists
            long roleId = user.getRoleId();
            boolean roleFound = this.roleService.exists(roleId);
            if(!roleFound){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", roleId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId", branchId),
                        request);
            }

            //...create user record
            CompletableFuture<UserRequest> futureRecord = userService.create(user);
            record = futureRecord.get();
        } catch (InterruptedException e) {
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserRequest user,
                                        @PathVariable("userId") long userId,
                                        BindingResult bindingResult,
                                        HttpServletRequest request){
        // Validate request object
        if (bindingResult.hasErrors()) {
            return errorHandler.validationExceptionHandler(
                    new ValidationException(Generators.buildErrorMessage(bindingResult)),
                    request);
        }

        long recordId = user.getId();
        logger.info(String.format("Modify user with ID '%s' by user with id %s", recordId, userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Modify user with UserId '%s' by user with id %s", recordId, userId)));
        UserRequest record;
        try {

            CompletableFuture<UserRequest> userRecord = this.userService.findById(recordId);
            record = userRecord.join();
            if(record == null){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId",recordId),
                        request);
            }

            //check whether username is not in use
            logger.info(String.format("Checking whether user assigned Username is not in use... by user with ID %s", userId));
            String userName = user.getUsername();
            boolean exists = this.userService.usernameDuplicated(userName, recordId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another user with Username '%s' exists", userName));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("User", "Username", userName),
                        request);
            }

            //check whether pf_no is not in use
            logger.info("Checking whether user assigned PF_NO is not in use...");
            String pfNo = user.getPfNo();
            exists = this.userService.pfNoDuplicated(pfNo, recordId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another user with PF_NO '%s' exists", pfNo));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("User", "PF_NO", pfNo),
                        request);
            }
            //check whether email is not in use
            logger.info("Checking whether user assigned email is not in use...");
            String email = user.getEmail();
            exists = this.userService.emailDuplicated(email, recordId);
            if(exists){
                logger.info(String.format("Resource Conflict! Another user with email '%s' exists", email));
                return errorHandler.duplicatesResourceExceptionHandler(
                        new DuplicateException("User", "Email", email),
                        request);
            }

            //...check if branch exists
            long branchId = user.getBranchId();
            boolean branchFound = this.branchService.checkIfExistsById(branchId);
            if(!branchFound){
                logger.info(String.format("Branch with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", branchId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Branch","BranchId", branchId),
                        request);
            }

            //...check if role exists
            long roleId = user.getRoleId();
            boolean roleFound = this.roleService.exists(roleId);
            if(!roleFound){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", roleId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("Role","RoleId", branchId),
                        request);
            }

            //...update user record
            userService.update(user, roleId, branchId);

            //..return updated record
            userRecord = this.userService.findByIdWithRole(recordId);
            record = userRecord.join();
        } catch (Exception e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving branch"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PostMapping("/activateUser/{recordId}/{activeStatus}/{userId}")
    public ResponseEntity<?>activateUser(@PathVariable Long recordId,
                                         @PathVariable Boolean activeStatus,
                                         @PathVariable("userId") long userId,
                                         HttpServletRequest request){
        logger.info(String.format("Delete role with ID '%s' by user with id %s" ,recordId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Change active status of user with ID '%s' to '%s' by user with id %s", activeStatus, recordId, userId)));

            CompletableFuture<RoleRequest> app = this.roleService.findById(recordId);
            RoleRequest record = app.join();
            if(record == null){
                logger.info(String.format("User with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","UserId",recordId),
                        request);
            }

            //..modifier
            String modifiedBy = String.format("%s", userId);

            //..modified on
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
            String modifiedOn = now.format(formatter);

            //..update active status
            userService.setActiveStatus(recordId, activeStatus, modifiedBy, modifiedOn);
            return new ResponseEntity<>("User record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }
    }
    @PostMapping("/deleteUser/{recordId}/{userId}")
    public ResponseEntity<?>deleteUser(@PathVariable Long recordId,
                                       @PathVariable("userId") long userId,
                                       HttpServletRequest request){
        logger.info(String.format("Delete user with UserId '%s' by user with id %s" ,recordId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Delete user with ID '%s' by user with id %s", recordId, userId)));

            CompletableFuture<UserRequest> app = this.userService.findById(recordId);
            UserRequest record = app.join();
            if(record == null){
                logger.info(String.format("User with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","UserId",recordId),
                        request);
            }

            userService.delete(recordId);
            return new ResponseEntity<>("User record deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    @PostMapping("/softDeleteUser/{recordId}/{isDeleted}/{userId}")
    public ResponseEntity<?>softDeleteUser(@PathVariable Long recordId,
                                           @PathVariable Boolean isDeleted,
                                           @PathVariable("userId") long userId,
                                           HttpServletRequest request){
        logger.info(String.format("Mark user '%s' as deleted by user with id %s" ,recordId, userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Modify user '%s' IsDeleted status to '%s' by user with id %s", recordId, isDeleted, userId)));

            CompletableFuture<UserRequest> app = this.userService.findById(recordId);
            UserRequest record = app.join();
            if(record == null){
                logger.info(String.format("User with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", recordId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","ID",recordId),
                        request);
            }

            userService.softDelete(recordId, isDeleted);
            return new ResponseEntity<>("User record updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

    }

    //endregion

    //region passwords
    @PostMapping("/updateUserPassword/{userId}")
    public ResponseEntity<?> updateUserPassword(@RequestBody @Valid String password,
                                                @PathVariable("userId") long userId,
                                                HttpServletRequest request){

        logger.info(String.format("Update user password by user with id %s", userId));
        String ip = networkService.getIncomingIpAddress(request);

        //log user activity
        logService.create(generateLog(userId, ip, String.format("Update user password by user with id  %s", userId)));
        UserRequest record;
        try {

            CompletableFuture<UserRequest> uRecord = this.userService.findById(userId);
            record = uRecord.join();
            if(record == null){
                logger.info(String.format("Role with id %s retrieval by user with id %s failed. Returned a 404 code - Resource not found", userId, userId));
                return errorHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","UserId",userId),
                        request);
            }

            //..update password
            String passwordHash = Generators.getHashedPassword(password, logger);
            userService.updatePassword(userId, passwordHash);

            //..return updated record
            uRecord = this.userService.findById(userId);
            record = uRecord.join();
        } catch (Exception e) {
            return errorHandler.errorHandler(
                    new GeneralException(e.getMessage()),request);
        }

        if (record == null || record.getId() == 0) {
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while updating user password"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @PostMapping("/passwordMatch/{password}/{passwordHash}/{userId}")
    public ResponseEntity<?> passwordMatch(@PathVariable String password,
                                           @PathVariable String passwordHash,
                                           @PathVariable long userId,
                                           HttpServletRequest request) {
        logger.info(String.format("Check password match for user by user with ID %s", userId));

        String ip = networkService.getIncomingIpAddress(request);
        boolean isMatched;
        try {
            logService.create(generateLog(userId, ip, String.format("Check password match  for user with ID %s", userId)));
            isMatched = Generators.isPasswordMatch(password, passwordHash, logger);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(isMatched, HttpStatus.OK);
    }

    @GetMapping("/generatePasswordHash/{password}/{userId}")
    public ResponseEntity<?> generatePasswordHash(@PathVariable String password, @PathVariable Long userId, HttpServletRequest request) {
        logger.info(String.format("Hash user password request by user with ID %s", userId));
        String ip = networkService.getIncomingIpAddress(request);
        String hashedPassword;
        try {
            logService.create(generateLog(userId, ip, String.format("Hash user password request by user with ID  %s", userId)));
            hashedPassword = Generators.getHashedPassword(password, logger);
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(hashedPassword, HttpStatus.OK);
    }

    //endregion

    //region archived_users
    @GetMapping("/getArchiveUserById/{logId}/{userId}")
    public ResponseEntity<?> getArchiveUserById(@PathVariable Long logId,
                                                @PathVariable Long userId,
                                                HttpServletRequest request) {
        String date = Generators.currentDate();
        logger.info(String.format("Retrieving archived user record for '%s'. Accessed by user with id %s on %s",logId, userId, date));

        ArchiveUserRequest record;
        try {
            //log user activity
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("Retrieving archived user record for '%s'. Accessed by user with id %s",logId, userId)));

            CompletableFuture<ArchiveUserRequest> futureRecord = aUserService.findById(logId);
            record = futureRecord.get();
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getArchiveUserByPfNo/{pfNo}/{userId}")
    public ResponseEntity<?> getArchiveUserByPfNo(@PathVariable String pfNo,
                                                  @PathVariable Long userId,
                                                  HttpServletRequest request) {
        String date = Generators.currentDate();
        logger.info(String.format("Retrieving archived user record for PF_NO '%s'. Accessed by user with id %s on %s",pfNo, userId, date));

        ArchiveUserRequest record;
        try {
            //log user activity
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("Retrieving archived user record for PF_NO '%s'. Accessed by user with id %s",pfNo, userId)));

            CompletableFuture<ArchiveUserRequest> futureRecord = aUserService.findByPfNo(pfNo);
            record = futureRecord.get();
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    @GetMapping("/getArchiveUsers/{userId}")
    public ResponseEntity<?> getArchiveUsers(@PathVariable Long userId,
                                             HttpServletRequest request) {
        logger.info(String.format("Retrieve all archived users records by user with ID %s", userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<ArchiveUserRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all archived user records bu user with ID %s", userId)));

            CompletableFuture<List<ArchiveUserRequest>> futureRecord = aUserService.findAll();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createArchiveUser/{userId}")
    public ResponseEntity<?> createArchiveUser(@RequestBody @Valid ArchiveUserRequest log,
                                               @PathVariable("userId") long userId,
                                               HttpServletRequest request) {
        logger.info(String.format("Create new archive user record by user with ID %s", userId));

        //...get user IP address
        String ip = networkService.getIncomingIpAddress(request);

        //Retrieve record
        ArchiveUserRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Create new archive user record by user with ID %s",userId)));

            CompletableFuture<ArchiveUserRequest> futureRecord = aUserService.create(log);
            record = futureRecord.get();
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        if (record == null || record.getId() == 0) {
            logger.error("Record not save. An error occurred");
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving user record"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    //endregion

    //region system_logs
    @GetMapping("/getLogs/{userId}")
    public ResponseEntity<?> getLogs(@PathVariable Long userId,
                                     HttpServletRequest request) {
        logger.info(String.format("Retrieve all user logs by user with ID %s", userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<LogRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all user logs bu user with ID %s", userId)));

            CompletableFuture<List<LogRequest>> futureRecord = logService.findAll();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getUserLogs/{startDate}/{endDate}/{userId}")
    public ResponseEntity<?> getUserLogs(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate,
                                         @PathVariable Long userId,
                                         HttpServletRequest request) {
        String date = Generators.currentDate();
        logger.info(String.format("Retrieving a list of system logs between '%s' and '%s'. Accessed by user with id %s on %s",startDate, endDate, userId, date));

        List<LogRequest> records;
        try {
            //log user activity
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("Retrieve all user logs bu user with ID %s", userId)));

            CompletableFuture<List<LogRequest>> futureRecord = logService.findAll(startDate, endDate);
            records = futureRecord.get();
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createLog/{userId}")
    public ResponseEntity<?> createLog(@RequestBody @Valid LogRequest log,
                                       @PathVariable("userId") long userId,
                                       HttpServletRequest request) {
        logger.info(String.format("Create new log record by user with ID %s", userId));

        //...get user IP address
        String ip = networkService.getIncomingIpAddress(request);

        //Retrieve record
        LogRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Create new log record by user with ID %s",userId)));

            CompletableFuture<LogRequest> futureRecord = logService.create(log);
            record = futureRecord.get();
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        if (record == null || record.getId() == 0) {
            logger.error("Record not save. An error occurred");
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving app record"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }
    //endregion

    //region archived_logs

    @GetMapping("/getArchiveLogs/{userId}")
    public ResponseEntity<?> getArchiveLogs(@PathVariable Long userId,
                                            HttpServletRequest request) {
        logger.info(String.format("Retrieve all archived logs by user with ID %s", userId));

        //try finding resource
        String ip = networkService.getIncomingIpAddress(request);

        List<ArchiveLogRequest> records;
        try {
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Retrieve all archived user logs bu user with ID %s", userId)));

            CompletableFuture<List<ArchiveLogRequest>> futureRecord = aLogService.findAll();
            records = futureRecord.get();
        } catch (InterruptedException e) {
            logger.info("Thread Exception:: Action cancelled by user");
            return errorHandler.threadCanceledHandler(
                    new CanceledException(e.getMessage()),request);
        } catch (ExecutionException e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/getArchiveLogs/{archiveDate}/{userId}")
    public ResponseEntity<?> getArchiveLogs(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime archiveDate,
                                            @PathVariable Long userId,
                                            HttpServletRequest request) {
        String date = Generators.currentDate();
        logger.info(String.format("Retrieving a list of archived logs for '%s'. Accessed by user with id %s on %s",archiveDate, userId, date));

        List<ArchiveLogRequest> records;
        try {
            //log user activity
            String ip = networkService.getIncomingIpAddress(request);
            logService.create(generateLog(userId, ip, String.format("Retrieve all archived logs bu user with ID %s", userId)));

            CompletableFuture<List<ArchiveLogRequest>> futureRecord = aLogService.findAll(archiveDate);
            records = futureRecord.get();
        } catch (Exception e) {
            String msg = e.getMessage();
            logger.error(String.format("General Error:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg),request);
        }

        //...check for null records
        if(records == null || records.isEmpty()){
            logger.info("No records found in database");
            records = new ArrayList<>();
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PostMapping("/createArchiveLog/{userId}")
    public ResponseEntity<?> createArchiveLog(@RequestBody @Valid ArchiveLogRequest log,
                                              @PathVariable("userId") long userId,
                                              HttpServletRequest request) {
        logger.info(String.format("Create new archive log record by user with ID %s", userId));

        //...get user IP address
        String ip = networkService.getIncomingIpAddress(request);

        //Retrieve record
        ArchiveLogRequest record;
        try{
            //log user activity
            logService.create(generateLog(userId, ip, String.format("Create new archive of record by user with ID %s",userId)));

            CompletableFuture<ArchiveLogRequest> futureRecord = aLogService.create(log);
            record = futureRecord.get();
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return errorHandler.errorHandler(new GeneralException(msg), request);
        }

        if (record == null || record.getId() == 0) {
            logger.error("Record not save. An error occurred");
            return errorHandler.errorHandler(
                    new GeneralException("An error occurred while saving app record"),request);
        }

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    //endregion

    //region testing endpoint

    @GetMapping("hello")
    public String SayHello(){
        return "Hello from SAM";
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

}

