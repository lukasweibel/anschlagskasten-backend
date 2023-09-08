package ch.lukasweibel.anschlagkasten.controller;

import java.security.Principal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.lukasweibel.anschlagkasten.security.OAuthClient;
import ch.lukasweibel.anschlagkasten.security.SecurityHandler;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.QueryParam;

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
