package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: ValidationException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to request validation
 * Created By: Nkambwe mark
 */
public class ValidationException  extends Exception {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    private static final long serialVersionId =1L;

    public ValidationException(String message){
        super(message);
    }
}