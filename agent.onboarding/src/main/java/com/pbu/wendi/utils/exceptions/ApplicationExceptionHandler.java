package com.pbu.wendi.utils.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
/**
 * Class Name: ExceptionHandler
 * Description: Exception handling class
 * Returns: Class implements all system exceptions
 * Created By: Nkambwe mark
 */
@ControllerAdvice
public class ApplicationExceptionHandler {
    /**
     * Handle resource not found exceptions
     * remarks.Class throws error message when system resource is not found
     * @param e - NotFoundException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.NOT_FOUND} - Resource not found
     **/
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> resourceNotFoundExceptionHandler(NotFoundException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle Duplicate resource exceptions
     * remarks.Class throws error message when system resource exists and a possible duplicate if posted
     * @param e - DuplicateException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.CONFLICT} - Possible duplication causing conflict
     **/
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Error> duplicatesResourceExceptionHandler(DuplicateException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handle authorization credentials exceptions
     * remarks.Class throws error message when user's credentials are not valid or not allowed to access resource
     * @param e - SecurityException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.UNAUTHORIZED} - User not authorized to access resource
     **/
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Error> badCredentialsExceptionHandler(SecurityException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle inactive or deleted resource exceptions
     * remarks.Class throws error message when system resource is marked as deleted or inactive
     * @param e - NotAvailableException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.GONE} - Resource no longer available
     **/
    @ExceptionHandler(NotAvailableException.class)
    public ResponseEntity<Error> resourceInactiveExceptionHandler(NotAvailableException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.GONE.value(),//GONE - resource no longer available
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.GONE);
    }

    /**
     * Handle resource validation exceptions
     * remarks.Class throws error message when system resource is invalid
     * @param e - ValidationException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.BAD_REQUEST} - Client sent a bad request
     **/
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Error> validationExceptionHandler(ValidationException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle system server errors exceptions
     * remarks.Class throws error message when service mul-functions
     * @param e - Exception object
     * @param request - Exception object
     * @return Status code {@code HttpStatus.INTERNAL_SERVER_ERROR} - Server Error
     **/
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<Error> serverExceptionHandler(Exception e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Handle system thread interrupted exceptions
     * remarks.Class throws error message when system resource is invalid
     * @param e - CanceledException object
     * @param request - Exception object
     * @return Status code {@code HttpStatus.EXPECTATION_FAILED} - Client Error
     **/
    @ExceptionHandler(CanceledException.class)
    public ResponseEntity<Error> threadCanceledHandler(CanceledException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.EXPECTATION_FAILED.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * Handle General System exceptions
     * remarks.Class throws error WendiException when system has a general exception
     * @param e - GeneralException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.INTERNAL_SERVER_ERROR} - Server error
     **/
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Error> errorHandler(GeneralException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Bad request exceptions
     * remarks.Class throws error message when system has a general exception
     * @param e - ClientException object
     * @param request - HttpServletRequest object
     * @return Status code {@code HttpStatus.BAD_REQUEST} - Server error
     **/
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Error> clientErrorHandler(ClientException e, HttpServletRequest request){
        Error error = new Error(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

