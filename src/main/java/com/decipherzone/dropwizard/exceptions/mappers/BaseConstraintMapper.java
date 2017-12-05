package com.decipherzone.dropwizard.exceptions.mappers;

import com.decipherzone.dropwizard.exceptions.BaseError;
import com.decipherzone.dropwizard.util.JsonSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Iterator;


public class BaseConstraintMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Log LOG = LogFactory.getLog(BaseConstraintMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException e) {
        assert e != null;


        StringBuilder fields = new StringBuilder();
        StringBuilder messages = new StringBuilder();
        Object[] items = e.getConstraintViolations().toArray();
        for (int i = 0; i < e.getConstraintViolations().size(); i++) {
            ConstraintViolation v = (ConstraintViolation) items[i];

            String msg = v.getMessage();
            Path path = v.getPropertyPath();
            String lastNode = "";
            Iterator<Path.Node> nodes = path.iterator();
            while(nodes.hasNext()) {
                Path.Node node = nodes.next();
                lastNode = node.getName();
            }
            fields.append(lastNode);
            messages.append(lastNode + " " + msg);
            if (i < e.getConstraintViolations().size()-1 ) {
                fields.append(",");
                messages.append(",\n");
            }
        }

        BaseError blazeError = new BaseError(fields.toString(),messages.toString(),"ConstraintViolation");
        String s = JsonSerializer.toJson(blazeError);
        LOG.error(e.getMessage(), e);

        return Response.status(Response.Status.BAD_REQUEST).entity(blazeError).build();
    }
}
