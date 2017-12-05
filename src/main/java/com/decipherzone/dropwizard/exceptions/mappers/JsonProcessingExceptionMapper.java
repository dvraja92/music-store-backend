package com.decipherzone.dropwizard.exceptions.mappers;

import com.decipherzone.dropwizard.exceptions.BaseError;
import com.decipherzone.dropwizard.util.JsonSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
    private static final Log LOG = LogFactory.getLog(JsonProcessingExceptionMapper.class);

    @Override
    public Response toResponse(JsonProcessingException exception) {
        StringBuilder sb = new StringBuilder();
        List<JsonMappingException.Reference> references = new ArrayList<>();
        if (exception instanceof JsonMappingException) {
            JsonMappingException mappingException = (JsonMappingException) exception;
            references = mappingException.getPath();
        } else {
            sb.append("Error parsing JSON.");
        }

        BaseError blazeError = new BaseError("Server Error", sb.toString(), exception.getClass().getCanonicalName());
        blazeError.setReferences(references);
        String s = JsonSerializer.toJson(blazeError);
        LOG.error(exception.getMessage(), exception);


        return Response.status(Response.Status.BAD_REQUEST).entity(s).header("Content-Type", "application/json").build();
    }
}