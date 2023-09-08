package ch.lukasweibel.anschlagkasten.security;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "oauth2")
public interface OAuthClient {

        @POST
        @Path("/oauth/token")
        String getToken(
                        @FormParam("grant_type") String grantType,
                        @FormParam("client_id") String clientId,
                        @FormParam("code") String code,
                        @FormParam("redirect_uri") String redirectUri,
                        @FormParam("client_secret") String clientSecret);

        @GET
        @Path("/oauth/profile")
        String getRoles(
                        @HeaderParam("Authorization") String bearer,
                        @HeaderParam("X-Scope") String scope);

}
