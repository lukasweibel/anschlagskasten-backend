package ch.lukasweibel.anschlagkasten.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;

@Path("/persons")
public class PersonResource {

    DbAccessor dbAccessor = new DbAccessor();
    ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnschlaege() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dbAccessor.getPersonsByStufe());
        return json;
    }
}
