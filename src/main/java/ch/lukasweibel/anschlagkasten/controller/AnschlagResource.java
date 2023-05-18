package ch.lukasweibel.anschlagkasten.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/anschlaege")
public class AnschlagResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String listAnschlaege() {
        return "Listeanschlaege";
    }

    @GET
    @Path("/anschlaege/{anschlagId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAnschlag() {
        return "Anschlagkasten";
    }
}