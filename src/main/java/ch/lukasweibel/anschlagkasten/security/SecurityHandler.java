package ch.lukasweibel.anschlagkasten.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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

    // This is just a test secret.
    public String getAccessToken(String code) {
        return oAuthClient.getToken("authorization_code",
                "IEss0jc-AXKefiCiZHc2AObgt75tczmrnYxVC2YSOgA",
                code, "https://anschlagskasten-web-fd337ce2917a.herokuapp.com",
                "1Q3C7OGvdVC2l4guadB7dglj3tz9KjBKB3CdQ2g1F80");
    }

    public boolean isRole(String accessToken, String role) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(oAuthClient.getRoles("Bearer " + accessToken, "with_roles"));
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
