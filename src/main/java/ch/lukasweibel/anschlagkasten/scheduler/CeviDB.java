package ch.lukasweibel.anschlagkasten.scheduler;

import java.util.ArrayList;
import java.util.HashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import ch.lukasweibel.anschlagkasten.db.CeviDbAccessor;
import ch.lukasweibel.anschlagkasten.db.DbAccessor;

@ApplicationScoped
public class CeviDB {

    @Inject
    @RestClient
    CeviDbAccessor ceviDbAccessor;

    @Inject
    @ConfigProperty(name = "cevi.db.email")
    String email;

    @Inject
    @ConfigProperty(name = "cevi.db.token")
    String token;

    @Inject
    DbAccessor dbAccessor;

    public void loadPeopleIntoDB() {
        HashMap<String, String> stufen = new HashMap<>();
        stufen.put("Hel[i]os", "430");
        stufen.put("Genesis", "429");
        stufen.put("Simson", "433");
        for (String stufe : stufen.keySet()) {
            String json = ceviDbAccessor.getPersons(stufen.get(stufe), email, token);
            Document doc = Document.parse(json);
            Object people = doc.get("people");
            if (people instanceof ArrayList) {
                ArrayList<Document> persons = (ArrayList<Document>) people;
                for (Document person : persons) {
                    dbAccessor.addPerson(person, stufe);
                }
            }
        }
    }

}
