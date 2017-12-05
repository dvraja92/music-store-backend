package com.decipherzone.dropwizard.exceptions;

import javax.ws.rs.core.Response;

public class BaseCustomException extends RuntimeException{
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    private final String field;
    private final Class<?> clazz;
    private final Response.Status status;

    public BaseCustomException(Response.Status status, String field, String message, Class<?> clazz) {
        super(message);
        this.status = status;
        this.field = field;
        this.clazz = clazz;
    }

    public String getField() {
        return field;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Response.Status getStatus() {
        return status;
    }
}
