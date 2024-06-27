package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: NotAvailableException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to unavailable resources which are deactivated or soft deleted
 * Created By: Nkambwe mark
 */

public class NotAvailableException extends RuntimeException {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    private static final long serialVersionId =1L;
    private final String resourceName;
    public String getResourceName() {
        return resourceName;
    }

    private final String fieldName;
    public String getFieldName() {
        return fieldName;
    }

    private final Object fieldValue;
    public Object getFieldValue() {
        return fieldValue;
    }

    public NotAvailableException(String resource, String field, Object value){
        super(String.format("Resource Not Available! Resource %s with %s '%s' is deactivated or has been deleted.",resource,field, value));
        resourceName = resource;
        fieldName = field;
        fieldValue = value;
    }
}
