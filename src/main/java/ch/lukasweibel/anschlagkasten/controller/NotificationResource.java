package ch.lukasweibel.anschlagkasten.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.lukasweibel.anschlagkasten.messaging.Messanger;

@Path("/notify")
public class NotificationResource {

    @Inject
    Messanger messanger;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAnschlaege() throws JsonProcessingException {
        // messanger.triggerNotification("Genesis");
        return Response.ok().build();
    }

}
