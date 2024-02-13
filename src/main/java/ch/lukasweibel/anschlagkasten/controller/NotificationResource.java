package ch.lukasweibel.anschlagkasten.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.messaging.Messanger;
import ch.lukasweibel.anschlagkasten.model.Contact;

@Path("/notification")
public class NotificationResource {

    @Inject
    Messanger messanger;

    @Inject
    DbAccessor dbAccessor;

    ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response sendLoginCode(@FormParam("phoneNumber") String phoneNumber) throws JsonProcessingException {
        messanger.sendLoginCode(phoneNumber);
        return Response.ok().build();
    }

    @POST
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response validateLoginCode(@FormParam("phoneNumber") String phoneNumber, @FormParam("code") int code)
            throws JsonProcessingException {
        if (messanger.validateLoginCode(phoneNumber, code)) {
            return Response.ok().build();
        }
        return Response.status(401).build();

    }

    @POST
    @Path("/contact")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addContactInformations(String newContactJsonString)
            throws JsonProcessingException {
        Contact contact = objectMapper.readValue(newContactJsonString, Contact.class);
        JsonNode rootNode = objectMapper.readTree(newContactJsonString);
        // Extract the code directly
        int code = rootNode.get("code").asInt();
        if (messanger.validateLoginCode(contact.getPhoneNumber(), code)) {
            dbAccessor.addContact(contact);
            return Response.ok().build();
        }
        return Response.status(401).build();
    }

}
