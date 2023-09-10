package ch.lukasweibel.anschlagkasten.controller;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.lukasweibel.anschlagkasten.security.SecurityHandler;

@Path("/security")
public class SecurityResource {

    @Inject
    SecurityHandler securityHandler;

    @POST
    @Path("/access-token/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("code") String code) {
        String response = securityHandler.getAccessToken(code);
        return Response.ok(response).build();
    }
}
