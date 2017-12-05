package com.decipherzone.dropwizard.resources;

import com.decipherzone.dropwizard.domain.models.UserDetails;
import com.decipherzone.dropwizard.domain.models.UserLogin;
import com.decipherzone.dropwizard.response.APIResponse;
import com.decipherzone.dropwizard.rest.InsertUserDetails;
import com.decipherzone.dropwizard.rest.result.UserResult;
import com.decipherzone.dropwizard.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@Api(value = "Authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    private AuthService authService;

    @Inject
    public AuthResource(AuthService authService){
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @ApiOperation(value = "Logs in a user using credentials provided by end user", notes = "Logs in a user using credentials", response = APIResponse.class)
    public APIResponse login(@ApiParam(value = "Login details", required = true) @Valid UserLogin login)
    {
        UserResult userResult = authService.login(login);
        if (userResult == null)
        {
            return new APIResponse.ResponseBuilder(Response.Status.BAD_REQUEST.getStatusCode(), "Fail").setMsg("Authorization failed").build();
        }
        return new APIResponse.ResponseBuilder(Response.Status.OK.getStatusCode(), "Success").setMsg("Authorization token").setData(userResult).build();
    }

    @POST
    @Path("/signup")
    @ApiOperation(value = "Signs up a user using details", notes = "Signs up a user using details", response = APIResponse.class)
    public APIResponse addUser(@ApiParam(value = "Member details", required = true) @Valid InsertUserDetails user)
    {
        UserDetails userDetails = authService.addUser(user);
        if (userDetails == null)
        {
            return new APIResponse.ResponseBuilder(Response.Status.BAD_REQUEST.getStatusCode(), "Fail").setMsg("User sign up failed").build();
        }
        return new APIResponse.ResponseBuilder(Response.Status.OK.getStatusCode(), "success").setMsg("User signed up").setData(userDetails).build();
    }

    @POST
    @Path("/logout")
    @ApiOperation(value = "Logs out a user", notes = "Logs out a user", response = APIResponse.class)
    public APIResponse logout()
    {
        String msg = "User logged out successfully";
        return new APIResponse.ResponseBuilder(Response.Status.OK.getStatusCode(), "success").setMsg(msg).build();
    }
}
