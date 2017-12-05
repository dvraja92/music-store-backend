package com.decipherzone.dropwizard.exceptions;

import com.decipherzone.dropwizard.util.JsonSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidDataException extends BaseCustomException implements ExceptionMapper<InvalidDataException> {
    private static final Log LOG = LogFactory.getLog(InvalidDataException.class);
    public InvalidDataException(String field, String message){
        super(Response.Status.BAD_REQUEST, field, message, InvalidDataException.class);
    }

    @Override
    public Response toResponse(InvalidDataException e) {
        String s = JsonSerializer.toJson(e);
        LOG.error(e.getMessage(),e);
        return Response.status(Response.Status.BAD_REQUEST).entity(s).header("Content-Type", "application/json").build();
    }
}
