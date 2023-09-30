package ch.lukasweibel.anschlagkasten.db;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.oracle.svm.core.annotate.Inject;

import ch.lukasweibel.anschlagkasten.model.Anschlag;
import ch.lukasweibel.anschlagkasten.model.Comment;
import javax.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.client.MongoCollection;

@ApplicationScoped
public class DbAccessor {

    ConnectionString connectionString;
    MongoClientSettings settings;
    MongoClient mongoClient;
    MongoDatabase database;
    Gson gson;
    MongoCollection<Document> personsCol;
    MongoCollection<Document> anschlaegeCol;
    ObjectMapper objectMapper;

    public DbAccessor(@ConfigProperty(name = "mongodb.connection-string") String connectionStr) {
        objectMapper = new ObjectMapper();

        connectionString = new ConnectionString(connectionStr);

        settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();

        mongoClient = MongoClients.create(settings);

        database = mongoClient.getDatabase("anschlagskasten");

        gson = new GsonBuilder().create();

        personsCol = database.getCollection("persons");

        anschlaegeCol = database.getCollection("anschlaege");
    }

    public ArrayList<Document> getPersons() {
        ArrayList<Document> personsList = new ArrayList<>();

        List<Document> pipeline = Arrays.asList();

        AggregateIterable<Document> result = personsCol.aggregate(pipeline);

        for (Document document : result) {
            personsList.add(document);
        }

        return personsList;
    }

    public void addPerson(Document newPerson, String stufe) {
        Document person = new Document();

        String id = newPerson.getString("id");

        person.append("id", id);
        person.append("firstName", newPerson.get("first_name"));
        person.append("lastName", newPerson.get("last_name"));
        person.append("vulgo", newPerson.get("nickname"));
        person.append("stufe", stufe);
        person.append("programs", new ArrayList<>());

        boolean isPersonExists = personsCol.countDocuments(Filters.eq("id", id)) > 0;

        if (!isPersonExists) {
            personsCol.insertOne(person);
            System.out.println("Inserted new person: " + id);
        }
        System.out.println(personsCol.find().into(new ArrayList<>()));
    }

    public ArrayList<Document> getPersonsByStufe() {
        ArrayList<Document> stufenListe = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(new Document("$group",
                new Document("_id", "$stufe")
                        .append("people",
                                new Document("$push",
                                        new Document("_id",
                                                new Document("$toString", "$_id"))
                                                .append("firstname", "$firstname")
                                                .append("lastname", "$lastname")
                                                .append("vulgo", "$vulgo")
                                                .append("stufe", "$stufe")
                                                .append("programs",
                                                        new Document("$map",
                                                                new Document("input", "$programs")
                                                                        .append("as", "program")
                                                                        .append("in",
                                                                                new Document("anschlagId",
                                                                                        new Document("$toString",
                                                                                                "$$program.anschlagId")))))))));

        AggregateIterable<Document> result = personsCol.aggregate(pipeline);

        for (Document document : result) {
            stufenListe.add(document);
        }

        return stufenListe;
    }

    public ArrayList<Anschlag> getActiveAnschlaege() {
        ArrayList<Anschlag> anschlaegeList = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(new Document("$match",
                new Document("status", 1L)));

        AggregateIterable<Document> result = anschlaegeCol.aggregate(pipeline);

        for (Document document : result) {
            Anschlag anschlag = null;
            try {
                anschlag = objectMapper.readValue(document.toJson(), Anschlag.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            anschlaegeList.add(anschlag);
        }
        return anschlaegeList;
    }

    public ArrayList<Document> getOrderedAnschlaege() {
        ArrayList<Document> anschlaegeList = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(new Document("$group",
                new Document("_id",
                        new Document("title", "$title")
                                .append("year",
                                        new Document("$year",
                                                new Document("$toDate", "$date"))))
                        .append("documents",
                                new Document("$push",
                                        new Document("_id",
                                                new Document("$toString", "$_id"))
                                                .append("creationDate", "$creationDate")
                                                .append("comments", "$comments")
                                                .append("date", "$date")
                                                .append("endPlace", "$endPlace")
                                                .append("endTime", "$endTime")
                                                .append("finalWord", "$finalWord")
                                                .append("introducing", "$introducing")
                                                .append("itemsToBring", "$itemsToBring")
                                                .append("name", "$name")
                                                .append("startPlace", "$startPlace")
                                                .append("startTime", "$startTime")
                                                .append("title", "$title")
                                                .append("status", "$status")))),
                new Document("$sort",
                        new Document("_id.year", -1L)),
                new Document("$group",
                        new Document("_id", "$_id.title")
                                .append("years",
                                        new Document("$push",
                                                new Document("year", "$_id.year")
                                                        .append("documents", "$documents")))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("title", "$_id")
                                .append("years", 1L)));

        AggregateIterable<Document> result = anschlaegeCol.aggregate(pipeline);

        for (Document document : result) {
            System.out.println(document);
            anschlaegeList.add(document);
        }
        return anschlaegeList;
    }

    public String saveAnschlag(Anschlag anschlag) {
        anschlag.setStatus(1);
        anschlag.setCreationDate(Instant.now().toEpochMilli());
        anschlag.setComments(new ArrayList<>());
        ObjectId objectId = new ObjectId();
        String id = objectId.toString();
        try {
            String anschlagJson = objectMapper.writeValueAsString(anschlag);
            Document anschlagDoc = Document.parse(anschlagJson);
            anschlagDoc.remove("_id");

            anschlagDoc.append("_id", objectId);
            anschlaegeCol.insertOne(anschlagDoc);
            System.out.println("Anschlag saved successfully with ID: " + id);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error saving anschlag.");
        }
        return id;
    }

    public void updateAnschlag(Anschlag anschlag) {
        try {
            String anschlagJson = objectMapper.writeValueAsString(anschlag);
            Document anschlagDoc = Document.parse(anschlagJson);
            Bson filter = Filters.eq("_id", new ObjectId(anschlagDoc.getString("_id")));

            anschlagDoc.remove("_id");

            Bson update = new Document("$set", anschlagDoc);

            anschlaegeCol.updateOne(filter, update);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void addComment(String _id, Comment comment) {
        Bson filter = Filters.eq("_id", new ObjectId(_id));

        Document commentDoc = new Document();
        commentDoc.append("name", comment.getName());
        commentDoc.append("text", comment.getText());
        commentDoc.append("timestamp", comment.getTimestamp());

        Bson update = Updates.addToSet("comments", commentDoc);

        anschlaegeCol.updateOne(filter, update);
    }

    public void addAnschlagToPerson(String personId, String anschlagId) {
        Bson filter = Filters.eq("_id", new ObjectId(personId));

        Document doc = new Document();
        doc.append("anschlagId", anschlagId);

        Bson update = Updates.addToSet("programs", doc);

        personsCol.updateOne(filter, update);
    }

    public void removeAnschlagFromPerson(String personId, String anschlagId) {
        Bson filter = Filters.eq("_id", new ObjectId(personId));

        Document doc = new Document();
        doc.append("anschlagId", anschlagId);

        Bson update = Updates.pull("programs", doc);

        personsCol.updateOne(filter, update);
    }
}
