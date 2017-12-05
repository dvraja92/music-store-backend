package com.decipherzone.dropwizard.exceptions.mappers;

import com.decipherzone.dropwizard.exceptions.BaseError;
import com.decipherzone.dropwizard.util.JsonSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Log LOG = LogFactory.getLog(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException e) {
        StringBuilder msg = new StringBuilder(e.getMessage());

        BaseError blazeError = new BaseError("ERROR Parsing JSON",msg.toString(),e.getClass().getCanonicalName());

        String s = JsonSerializer.toJson(blazeError);
        LOG.error(e.getMessage(),e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(s).header("Content-Type", "application/json").build();

    }
}
