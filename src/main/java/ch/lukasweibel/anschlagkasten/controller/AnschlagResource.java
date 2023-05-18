package ch.lukasweibel.anschlagkasten.controller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.model.Anschlag;

@Path("/anschlaege")
public class AnschlagResource {

    DbAccessor dbAccessor = new DbAccessor();
    ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnschlaege() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dbAccessor.getAnschlaege());
        return json;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAnschlag(String jsonString) throws JsonProcessingException {
        Anschlag anschlag = objectMapper.readValue(jsonString, Anschlag.class);
        return Response.ok().build();
    }

    @GET
    @Path("/{anschlagId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAnschlagById() {
        return "Anschlagkasten";
    }
}