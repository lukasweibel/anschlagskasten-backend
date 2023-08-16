package ch.lukasweibel.anschlagkasten.db;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/groups")
@RegisterRestClient(configKey = "client-api")
public interface CeviDbAccessor {

    @Path("/{groupId}/people.json")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    String getPersons(@PathParam("groupId") String groupId, @QueryParam("user_email") String email,
            @QueryParam("user_token") String token);

}