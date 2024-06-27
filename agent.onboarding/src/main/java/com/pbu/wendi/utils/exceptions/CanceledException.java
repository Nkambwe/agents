package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: CanceledException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to internal service operations
 * Created By: Nkambwe mark
 */

public class CanceledException  extends InterruptedException {

    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    private static final long serialVersionId =1L;

    public CanceledException(){
        super("Transaction has been canceled by user");
    }

    public CanceledException(String message){
        super(message);
    }
}

