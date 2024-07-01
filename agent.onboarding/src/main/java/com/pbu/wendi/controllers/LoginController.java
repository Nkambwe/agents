package com.pbu.wendi.controllers;

import com.pbu.wendi.configurations.ApplicationExceptionHandler;
import com.pbu.wendi.requests.agents.dto.SettingsRequest;
import com.pbu.wendi.requests.helpers.dto.LoginModel;
import com.pbu.wendi.requests.sam.dto.LogRequest;
import com.pbu.wendi.requests.sam.dto.PermissionRequest;
import com.pbu.wendi.requests.sam.dto.UserRequest;
import com.pbu.wendi.responses.WendiResponse;
import com.pbu.wendi.services.agents.services.SettingService;
import com.pbu.wendi.services.sam.services.*;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.common.MailService;
import com.pbu.wendi.utils.common.NetworkService;
import com.pbu.wendi.utils.exceptions.ClientException;
import com.pbu.wendi.utils.exceptions.GeneralException;
import com.pbu.wendi.utils.exceptions.NotFoundException;
import com.pbu.wendi.utils.exceptions.ValidationException;
import com.pbu.wendi.utils.helpers.Generators;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("authentication")
public class LoginController {
    private final ModelMapper mapper;
    private final AppLoggerService logger;
    private final ApplicationExceptionHandler exceptionHandler;
    private final NetworkService networkService;
    private final SettingService settingsService;
    private final PermissionService permissionService;
    private final UserService userService;
    private final LogService logService;
    private final MailService mailService;

    public LoginController(ModelMapper mapper,
                           AppLoggerService logger,
                           ApplicationExceptionHandler exceptionHandler,
                           NetworkService networkService,
                           SettingService settingsService,
                           PermissionService permissionService,
                           UserService userService,
                           LogService logService,
                           MailService mailService) {
        this.mapper = mapper;
        this.logger = logger;
        this.exceptionHandler = exceptionHandler;
        this.networkService = networkService;
        this.settingsService = settingsService;
        this.permissionService = permissionService;
        this.userService = userService;
        this.logService = logService;
        this.mailService = mailService;
    }

    @GetMapping("/login/{userName}/{password}/{attempts}")
    public ResponseEntity<?> login(@PathVariable("userName") String userName,
                                   @PathVariable("password") String password,
                                   @PathVariable("attempts") int attempts,
                                   HttpServletRequest request){
        // current date and time
        String currentDate = Generators.currentDate();

        //Network IP Address
        String ip = networkService.getIncomingIpAddress(request);
        String logMsg = String.format("SAM LOGIN :: Attempted system access at '%s' from IP Address :: %s", currentDate, ip);
        logService.create(generateLog(0, ip, logMsg));
        logger.info(logMsg);

        //check username
        if(userName == null || userName.equals("")){
            logMsg = String.format("Attempted login at '%s' failed due to no username being provided.", currentDate);
            logger.info("LOGIN FAILED:: Username required");
            logService.create(generateLog(0, ip, logMsg));
            return exceptionHandler.validationExceptionHandler(new ValidationException("Invalid Username!  Must provide a Username to login"),request);
        }

        //check password
        if(password == null || password.equals("")){
            logMsg = String.format("Attempted login by user '%s' at '%s' failed due to no password being provided.", userName, currentDate);
            logger.info("LOGIN FAILED :: Password required");
            logService.create(generateLog(0, ip, logMsg));
            return exceptionHandler.validationExceptionHandler(new ValidationException("Invalid Password! Must provide a password to login"),request);
        }

        logMsg = String.format("Attempted login to the system by User '%s' at '%s'.", userName, currentDate);
        logger.info(logMsg);
        logService.create(generateLog(0, ip, logMsg));

        //retrieve user by username
        UserRequest user;
        LoginModel login;
        try{
            //log user activity
            logger.info(String.format("Retrieve user record by Username '%s'", userName));

            //Retrieve record
            CompletableFuture<UserRequest> userRecord = this.userService.findByUsername(userName);
            user = userRecord.join();

            //Record not found, throw NOT_FOUND response
            if(user == null) {
                logger.info(String.format("LOGIN FAILED :: User with Username '%s' not found. Returned a 404 code - Resource not found", userName));
                logService.create(generateLog(0, ip, "Login failed. User record not found"));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","Username",userName),
                        request);
            }

            //map user record
            login = this.mapper.map(user, LoginModel.class);

            //make sure user is not already logged in
            if(login.isLoggedIn()){
                logService.create(generateLog(user.getId(), ip, String.format("ACCESS DENIED :: Account with Username '%s' is already logged in", userName)));
                logger.info("LOGIN FAILED :: Attempt to login with an account in use");
                return exceptionHandler.clientErrorHandler(new ClientException("Your account is already logged in. If this is not you, ask your administrator for help"), request);
            }

            //make sure user is verified
            if(!login.isVerified()){
                logService.create(generateLog(user.getId(), ip, String.format("ACCESS DENIED :: Account with Username '%s' is not verified", userName)));
                logger.info("LOGIN FAILED :: Attempt to login with an account which is not verified");
                return exceptionHandler.clientErrorHandler(new ClientException("Your account is not verified. Ask your Administrator for help"), request);
            }

            //make sure user is active
            if(!login.isActive()){
                logService.create(generateLog(user.getId(), ip, String.format("ACCESS DENIED :: Account with Username '%s' is Deactivated", userName)));
                logger.info("LOGIN FAILED :: Attempt to login with an account which is deactivated");
                return exceptionHandler.clientErrorHandler(new ClientException("Your account has been Deactivated Ask your Administrator for help"), request);
            }

            //find login settings
            CompletableFuture<List<SettingsRequest>> settingsRecord = settingsService.findAllByParamNames(Generators.loginParams());
            List<SettingsRequest> loginSettings = settingsRecord.get();

            //login attempts
            int loginAttempts = Generators.getAttempts(loginSettings);

            //..try login
            if(loginAttempts >= attempts){
                //..validate password
                boolean isMatched = Generators.isPasswordMatch(password, user.getPassword(), logger);
                if(!isMatched){
                    logService.create(generateLog(user.getId(), ip, String.format("ACCESS DENIED :: Account with Username '%s' provided wrong password", userName)));
                    logger.info("LOGIN FAILED :: Attempt to login with wrong password");
                    return exceptionHandler.clientErrorHandler(new ClientException("Password is not correct. Please enter correct password and try again"), request);
                }

                int expiresIn = Generators.calculateDaysLeft(user.getLastPasswordChange(), Generators.getExpiryDays(loginSettings));
                login.setExpirePassword(Generators.getExpireStatus(loginSettings));
                login.setExpiresIn(expiresIn);
                login.setTimeout(Generators.getTimeout(loginSettings));

                List<String> grants = new ArrayList<>();
                //...add roles permissions
                if(user.getRoleId() != 0){
                    permissionService.findPermissionsByRoleId(user.getRoleId())
                            .thenApply(permissionRecords -> {

                                if (permissionRecords != null && !permissionRecords.isEmpty()) {
                                    for (PermissionRequest permission : permissionRecords) {
                                        grants.add(permission.getName());
                                    }
                                }
                                return null; // or return some meaningful value
                            }).exceptionally(ex -> {
                                // Handle exceptions here, e.g., log the error
                                ex.printStackTrace();
                                return null;
                            });
                }
                login.setPermissions(grants);
                login.setLoggedIn(true);

                //...update logged in status
                this.userService.setLoginStatus(user.getId(), true, LocalDateTime.now());
            } else {
                //...deactivate user account
                this.userService.setActiveStatus(user.getId(), false, "SAM", LocalDateTime.now());
                return exceptionHandler.clientErrorHandler(new ClientException("Your account has been Deactivated Ask your Administrator for help"), request);
            }

        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException(msg), request);
        }

        //return branch record
        return new ResponseEntity<>(login, HttpStatus.OK);
    }
    @GetMapping("/logout/{userId}/{userName}")
    public ResponseEntity<?> logout(@PathVariable("userId") long userId,
                                    @PathVariable("userName") String userName,
                                    HttpServletRequest request){
        // current date and time
        String currentDate = Generators.currentDate();

        //Network IP Address
        String ip = networkService.getIncomingIpAddress(request);
        String logMsg = String.format("SAM LOGOUT :: System logout at '%s' by username '%s' from IP Address :: %s", currentDate, userName, ip);
        logService.create(generateLog(0, ip, String.format("Logout by user '%s' at '%s'", userName,currentDate)));
        logger.info(logMsg);
        try{
            //...update logged in status
            this.userService.setLoginStatus(userId, false, LocalDateTime.now());
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException("Failed to logout user. An error occurred"), request);
        }

        //response
        WendiResponse wendiResponse = new WendiResponse();
        wendiResponse.setStatus(200);
        wendiResponse.setMessage("Successfully logged out");
        wendiResponse.setData(String.format("%s", "true"));
        return new ResponseEntity<>(wendiResponse, HttpStatus.OK);
    }

    @GetMapping("/passwordReset/{userId}/{oldPassword}/{newPassword}/{confirmPassword}")
    public ResponseEntity<?> passwordReset(@PathVariable("userId") long userId,
                                           @PathVariable("oldPassword") String oldPassword,
                                           @PathVariable("newPassword") String newPassword,
                                           @PathVariable("confirmPassword") String confirmPassword,
                                           HttpServletRequest request){
                // current date and time
                String currentDate = Generators.currentDate();

                //Network IP Address
                String ip = networkService.getIncomingIpAddress(request);
                String logMsg = String.format("SAM PASSWORD_CHANGE :: Request for password reset for user ID'%s' from IP Address :: %s at '%s'", userId, ip, currentDate);
                logService.create(generateLog(0, ip, String.format("Password change for user ID '%s'", userId)));
                logger.info(logMsg);
                try{
                    //Retrieve record
                    CompletableFuture<UserRequest> userRecord = this.userService.findById(userId);
                    UserRequest user = userRecord.join();

                    //Record not found, throw NOT_FOUND response
                    if(user == null) {
                        logger.info(String.format("NOT FOUND :: User with User ID '%s' not found. Returned a 404 code - Resource not found", userId));
                        return exceptionHandler.resourceNotFoundExceptionHandler(
                                new NotFoundException("User","ID",userId),
                                request);
                    }

                    //...make sure old password is the same
                    if(oldPassword != null && !oldPassword.equals("")){
                        logger.info("SAM PASSWORD_CHANGE :: Attempt to change password failed. Old password not provided");
                        return exceptionHandler.clientErrorHandler(new ClientException("Old Password is required. Please enter old password and try again"), request);
                    }

                    boolean isMatched = Generators.isPasswordMatch(oldPassword, user.getPassword(), logger);
                    if(!isMatched){
                        logger.info("SAM PASSWORD_CHANGE :: Attempt to change password failed. Wrong old password provided");
                        return exceptionHandler.clientErrorHandler(new ClientException("Old Password is not correct. Please enter correct password and try again"), request);
                    }

                    String nPwd = newPassword.trim();
                    String cPwd = confirmPassword.trim();
                    if(!nPwd.equals(cPwd)){
                        logger.info("PASSWORD MISS_MATCH :: User passwords do not match");
                        return exceptionHandler.clientErrorHandler(new ClientException("Your passwords do not match. Please enter correct password and try again"), request);
                    }

                    //hash password
                    String  passwordHash = Generators.getHashedPassword(nPwd, logger);
                    userService.updatePassword(userId, passwordHash);

                } catch(Exception ex){
                    String msg = ex.getMessage();
                    logger.error(String.format("General exception:: %s", msg));
                    return exceptionHandler.errorHandler(new GeneralException("Failed to logout user. An error occurred"), request);
                }

                return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
            }

    @GetMapping("/forgotPassword/{email}")
    public ResponseEntity<?> forgotPassword( @PathVariable("email") String email, HttpServletRequest request){
        // current date and time
        String currentDate = Generators.currentDate();

        //Network IP Address
        String  pwd;
        String ip = networkService.getIncomingIpAddress(request);
        String logMsg = String.format("SAM FORGOTTEN_PASSWORD :: Request for system password reset at '%s' by email '%s' from IP Address :: %s", currentDate, email, ip);
        logger.info(logMsg);
        logService.create(generateLog(0, ip, String.format("Request for system password reset at '%s' by email '%s' from IP Address :: %s", currentDate, email, ip)));

        try{
            //Retrieve record
            CompletableFuture<UserRequest> userRecord = this.userService.findByEmail(email);
            UserRequest user = userRecord.join();

            //Record not found, throw NOT_FOUND response
            if(user == null) {
                logger.info(String.format("NOT FOUND :: User with Email '%s' not found. Returned a 404 code - Resource not found", email));
                return exceptionHandler.resourceNotFoundExceptionHandler(
                        new NotFoundException("User","Email",email),
                        request);
            }

            //...reset user password to system reset password
            pwd = Generators.generateTempPassword();
            userService.updatePassword(user.getId(), Generators.getHashedPassword(pwd, logger));

            //...Send new password via email
            String msg = Generators.mailMsg(user.getFirstname(), pwd);
            mailService.sendMultipleEmail(email, null, msg,"Wendi Agent App password reset", logger);
        } catch(Exception ex){
            String msg = ex.getMessage();
            logger.error(String.format("General exception:: %s", msg));
            return exceptionHandler.errorHandler(new GeneralException("System Password reset failed. An error occurred and process was cancelled"), request);
        }

        return new ResponseEntity<>(pwd, HttpStatus.OK);
    }

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
