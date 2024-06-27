package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: SecurityException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to incorrect user access credentials
 * Created By: Nkambwe mark
 */

public class SecurityException extends RuntimeException {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    public static final long serialVersionId = 1L;

    public SecurityException(String message){
        super(message);
    }
}
