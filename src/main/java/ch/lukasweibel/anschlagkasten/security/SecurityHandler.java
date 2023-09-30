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

    @ConfigProperty(name = "oauth.client.redirect")
    String redirectUrl;

    String[] editor = { "Stufenleiter/-in", "Gruppenleiter/-in" };

    String[] admin = { "Abteilungsleiter/-in", "Webmaster/-in" };

    public String getAccessToken(String code) {
        return oAuthClient.getToken("authorization_code",
                clientId,
                code, redirectUrl,
                clientSecret);
    }

    public boolean isRole(String accessToken, String role) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String response = oAuthClient.getRoles("Bearer " + accessToken, "with_roles");
            JsonNode userNode = objectMapper.readTree(response);
            JsonNode rolesNode = userNode.get("roles");
            if (userNode.get("ortsgruppe_id").asInt() != 11) {
                return false;
            }
            for (JsonNode roleNode : rolesNode) {
                if (role.equals("editor")) {
                    for (String allowedRole : editor) {
                        if (roleNode.get("role_name").asText().equals(allowedRole)) {
                            return true;
                        }
                    }
                } else if (role.equals("admin")) {
                    for (String allowedRole : admin) {
                        if (roleNode.get("role_name").asText().equals(allowedRole)) {
                            return true;
                        }
                    }
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
