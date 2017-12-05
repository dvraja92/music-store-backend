package com.decipherzone.dropwizard.exceptions.mappers;

import com.decipherzone.dropwizard.exceptions.BaseCustomException;
import com.decipherzone.dropwizard.exceptions.BaseError;
import com.decipherzone.dropwizard.util.JsonSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class BaseExceptionMapper implements ExceptionMapper<BaseCustomException> {
    private static final Log LOG = LogFactory.getLog(BaseExceptionMapper.class);
    @Override
    public Response toResponse(BaseCustomException e) {

        BaseError blazeError = new BaseError(e.getField(),e.getMessage(),e.getClass().getCanonicalName());
        String s = JsonSerializer.toJson(blazeError);
        LOG.error(e.getMessage(),e);

        return Response.status(e.getStatus()).entity(s).header("Content-Type", "application/json").build();
    }
}