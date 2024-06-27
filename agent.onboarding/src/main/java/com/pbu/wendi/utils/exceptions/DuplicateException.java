package com.pbu.wendi.utils.exceptions;

/**
 * Class Name: DuplicateException
 * Extends : System.RuntimeException class
 * Description: Class handles conflicting resources such as duplicates in the system
 * Created By: Nkambwe mark
 */

public class DuplicateException extends RuntimeException {

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

    public DuplicateException(String resource, String field, Object value){
        super(String.format("Resource Conflict! Another %s with %s '%s' found",resource,field, value));
        resourceName = resource;
        fieldName = field;
        fieldValue = value;
    }
}