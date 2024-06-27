package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: ClientException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to client errors
 * Created By: Nkambwe mark
 */
public class ClientException  extends RuntimeException {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    public static final long serialVersionId = 1L;
    public ClientException(String message){
        super(message);
    }
}

