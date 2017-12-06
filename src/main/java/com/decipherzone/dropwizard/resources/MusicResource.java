package com.decipherzone.dropwizard.resources;


import com.codahale.metrics.annotation.Timed;
import com.decipherzone.dropwizard.response.APIResponse;
import com.decipherzone.dropwizard.rest.MusicAddRequest;
import com.decipherzone.dropwizard.services.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.decipherzone.dropwizard.domain.models.Music;

import java.util.List;


@Path("/music")
@Api("Music Management")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MusicResource {

    private MusicService musicService;

    @Inject
    public MusicResource(MusicService musicService){
        this.musicService = musicService;
    }

    @POST
    @Path("/")
    @Timed(name = "addRecord")
    @ApiOperation(value = "Add Record")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Music addRecord(@Valid MusicAddRequest request){
       return musicService.addMusic(request);

    }

    @GET
    @Path("/")
    @Timed(name = "getAllRecords")
    @ApiOperation(value = "Get All Records")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public List<Music> getAllRecords(){
        return musicService.getAllRecords();
    }

    @GET
    @Path("/{recordId}")
    @Timed(name = "getRecord")
    @ApiOperation(value = "Get Record")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Music getRecord(@PathParam("recordId") String recordId){
        return musicService.getRecord(recordId);
    }

    @PUT
    @Path("/")
    @Timed(name = "updateRecord")
    @ApiOperation(value = "Update Record")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Music updateRecord(@Valid Music music){
        return musicService.updateRecord(music);
    }

    @GET
    @Path("/artist/{artistName}")
    @Timed(name = "getRecordByArtist")
    @ApiOperation(value = "Get Record By Artist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public List<Music> getRecordByArtist(@PathParam("artistName") String artistName){
        return musicService.getRecordByArtist(artistName);
    }

    @GET
    @Path("/title/{titleName}")
    @Timed(name = "getRecordByTitle")
    @ApiOperation(value = "Get Record By Title")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Music getRecordByTitle(@PathParam("titleName") String titleName){
        return musicService.getRecordByTitle(titleName);
    }


    @DELETE
    @Path("/{recordId}")
    @Timed(name = "Delete Record")
    @ApiOperation(value = "Delete Record")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public APIResponse deleteRecord(@PathParam(value = "recordId") String recordId){

        musicService.deleteRecord(recordId);
        return new APIResponse.ResponseBuilder(Response.Status.OK.getStatusCode(), "success").setMsg("Record deleted successfully").build();

    }

}
