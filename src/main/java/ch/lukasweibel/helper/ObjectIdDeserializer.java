package ch.lukasweibel.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ObjectIdDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        String objectIdString;
        try {
            objectIdString = node.get("$oid").asText();
        } catch (Exception e) {
            // TODO
            objectIdString = node.asText();
        }
        return objectIdString;
    }
}
