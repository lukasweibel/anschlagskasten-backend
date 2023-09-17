package ch.lukasweibel.anschlagkasten.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class SecurityHandler {

    @Inject
    @RestClient
    OAuthClient oAuthClient;

    @ConfigProperty(name = "oauth.client.id")
    String clientId;

    @ConfigProperty(name = "oauth.client.secret")
    String clientSecret;

    public String getAccessToken(String code) {
        return oAuthClient.getToken("authorization_code",
                clientId,
                code, "https://anschlagskasten-web-fd337ce2917a.herokuapp.com",
                clientSecret);
    }

    public boolean isRole(String accessToken, String role) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(accessToken);
            String response = oAuthClient.getRoles("Bearer " + accessToken, "with_roles");
            System.out.println(response);
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode rolesNode = jsonNode.get("roles");
            for (JsonNode roleNode : rolesNode) {
                if (roleNode.get("role_name").asText().equals(role)) {
                    return true;
                }
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return false;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
