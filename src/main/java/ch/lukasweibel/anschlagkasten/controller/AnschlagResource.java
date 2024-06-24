package ch.lukasweibel.anschlagkasten.controller;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.messaging.Messanger;
import ch.lukasweibel.anschlagkasten.model.Anschlag;
import ch.lukasweibel.anschlagkasten.model.Comment;
import ch.lukasweibel.anschlagkasten.security.SecurityHandler;

@Path("/anschlaege")
public class AnschlagResource {

    @Inject
    Messanger messanger;

    @Inject
    DbAccessor dbAccessor;
    ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    SecurityHandler securityHandler;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAnschlaege() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dbAccessor.getActiveAnschlaege());
        return Response.ok(json).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAnschlag(String jsonString, @HeaderParam("Access-Token") String accessToken)
            throws JsonProcessingException {
        System.out.println(accessToken);
        if (securityHandler.isRole(accessToken, "editor") || securityHandler.isRole(accessToken, "admin")) {
            Anschlag anschlag = objectMapper.readValue(jsonString, Anschlag.class);
            String id = dbAccessor.saveAnschlag(anschlag);
            messanger.triggerNotifications(anschlag.getStufe());
            return Response.ok(id).build();
        }
        return Response.status(401).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAnschlag(String jsonString, @HeaderParam("Access-Token") String accessToken)
            throws JsonProcessingException {
        if (securityHandler.isRole(accessToken, "editor") || securityHandler.isRole(accessToken, "admin")) {
            Anschlag anschlag = objectMapper.readValue(jsonString, Anschlag.class);
            dbAccessor.updateAnschlag(anschlag);
            return Response.ok().build();
        } else {
            return Response.status(401).build();
        }

    }

    @POST
    @Path("/comment/{anschlagId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("anschlagId") String anschlagId, String newCommentJsonString)
            throws JsonProcessingException {
        Comment comment = objectMapper.readValue(newCommentJsonString, Comment.class);
        dbAccessor.addComment(anschlagId, comment);
        return Response.ok().build();
    }

    @GET
    @Path("/ordered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderedAnschlaege()
            throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dbAccessor.getOrderedAnschlaege());
        return Response.ok(json).build();
    }
}