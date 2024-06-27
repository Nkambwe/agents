package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: GeneralException
 * Extends : System.RuntimeException class
 * Description: Class handles General service exceptions
 * Created By: Nkambwe mark
 */
public class GeneralException  extends RuntimeException {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    public static final long serialVersionId = 1L;

    public GeneralException(String message){
        super(message);
    }
}
