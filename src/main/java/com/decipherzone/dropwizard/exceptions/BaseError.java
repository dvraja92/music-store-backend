package com.decipherzone.dropwizard.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.List;

public class BaseError {
    private String field;
    private String message;
    private String errorType;
    private List<JsonMappingException.Reference> references;


    public BaseError(String field, String message, String errorType) {
        this.message = message;
        this.errorType = errorType;
        this.field = field;
    }

    public List<JsonMappingException.Reference> getReferences() {
        return references;
    }

    public void setReferences(List<JsonMappingException.Reference> references) {
        this.references = references;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
