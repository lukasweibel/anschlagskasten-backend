package ch.lukasweibel.anschlagkasten.controller;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.lukasweibel.anschlagkasten.db.CeviDbAccessor;
import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.scheduler.CeviDB;

@Path("/persons")
public class PersonResource {

    @Inject
    DbAccessor dbAccessor;
    ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    @RestClient
    CeviDbAccessor ceviDbAccessor;

    @Inject
    CeviDB ceviDB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listPersons() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dbAccessor.getPersonsByStufe());
        return json;
    }

    @POST
    @Path("/add/{personId}/{anschlagId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAnschlag(@PathParam("anschlagId") String anschlagId, @PathParam("personId") String personId)
            throws JsonProcessingException {
        dbAccessor.addAnschlagToPerson(personId, anschlagId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/remove/{personId}/{anschlagId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAnschlag(@PathParam("anschlagId") String anschlagId, @PathParam("personId") String personId)
            throws JsonProcessingException {
        dbAccessor.removeAnschlagFromPerson(personId, anschlagId);
        return Response.ok().build();
    }

    @POST
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeAnschlag()
            throws JsonProcessingException {
        ceviDB.loadPeopleIntoDB();
        return Response.ok().build();
    }
}
