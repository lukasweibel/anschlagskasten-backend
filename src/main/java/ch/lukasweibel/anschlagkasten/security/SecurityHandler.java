package ch.lukasweibel.anschlagkasten.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class SecurityHandler {

    @Inject
    @RestClient
    OAuthClient oAuthClient;

    // This is just a test secret.
    public String getAccessToken(String code) {
        return oAuthClient.getToken("authorization_code",
                "aAtkRhd13axIl-SxZWb2Q2ZzzlqR5Y5ojlJd92_bxjw",
                code, "urn:ietf:wg:oauth:2.0:oob",
                "tNNwMFW1QScmzB-jl1nZp51E5rAExfQF2mtAzNAWZ-c");
    }

    public boolean isAdmin(String accessToken) {
        System.out.println(oAuthClient.getRoles("Bearer " + accessToken, "email"));
        return false;
    }

}
