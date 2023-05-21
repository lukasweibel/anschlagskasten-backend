package ch.lukasweibel.anschlagkasten.controller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.model.Anschlag;
import ch.lukasweibel.anschlagkasten.model.Comment;

@Path("/anschlaege")
public class AnschlagResource {

    DbAccessor dbAccessor = new DbAccessor();
    ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAnschlaege() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dbAccessor.getAnschlaege());
        return Response.ok(json).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAnschlag(String jsonString) throws JsonProcessingException {
        Anschlag anschlag = objectMapper.readValue(jsonString, Anschlag.class);
        dbAccessor.saveAnschlag(anschlag);
        return Response.ok().build();
    }

    @POST
    @Path("/comment/{anschlagId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAnschlag(@PathParam("anschlagId") String anschlagId, String newCommentJsonString)
            throws JsonProcessingException {
        Comment comment = objectMapper.readValue(newCommentJsonString, Comment.class);
        dbAccessor.addComment(anschlagId, comment);
        return Response.ok().build();
    }
}