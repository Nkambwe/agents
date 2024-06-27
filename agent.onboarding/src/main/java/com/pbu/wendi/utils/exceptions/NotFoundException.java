package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: NotFoundException
 * Extends : System.RuntimeException class
 * Description: Class handles errors related to non-existing records in the database
 * Returns: Class returns Status Code HttpStatus.NOT_FOUND(404) which  indicates that the requested resource is not found
 * Created By: Nkambwe mark
 */

public class NotFoundException extends RuntimeException {
    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    public static final long serialVersionId = 1L;

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

    public NotFoundException(String resource, String field, Object value){
        super(String.format("Record not found! %s with %s '%s' not found.",resource,field, value));
        resourceName = resource;
        fieldName = field;
        fieldValue = value;
    }

}
