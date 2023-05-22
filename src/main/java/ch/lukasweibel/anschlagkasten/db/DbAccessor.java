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

import ch.lukasweibel.anschlagkasten.model.Anschlag;
import ch.lukasweibel.anschlagkasten.model.Comment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.client.MongoCollection;

public class DbAccessor {

    ConnectionString connectionString;
    MongoClientSettings settings;
    MongoClient mongoClient;
    MongoDatabase database;
    Gson gson;
    MongoCollection<Document> personsCol;
    MongoCollection<Document> anschlaegeCol;
    ObjectMapper objectMapper;

    public DbAccessor() {
        objectMapper = new ObjectMapper();

        connectionString = new ConnectionString(
                "mongodb+srv://lukasweibel:z17zzGXMLkjrhMZ6@cluster0.5pogeh5.mongodb.net/?retryWrites=true&w=majority");

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

    public ArrayList<Document> getPersonsByStufe() {
        ArrayList<Document> stufenListe = new ArrayList<>();

        List<Document> pipeline = Arrays.asList(new Document("$group",
                new Document("_id", "$stufe")
                        .append("people",
                                new Document("$push",
                                        new Document("_id", "$_id")
                                                .append("firstname", "$firstname")
                                                .append("lastname", "$lastname")
                                                .append("vulgo", "$vulgo")
                                                .append("programs", "$programs")))));

        AggregateIterable<Document> result = personsCol.aggregate(pipeline);

        for (Document document : result) {
            stufenListe.add(document);
        }

        return stufenListe;
    }

    public ArrayList<Anschlag> getAnschlaege() {
        ArrayList<Anschlag> anschlaegeList = new ArrayList<>();

        List<Document> pipeline = Arrays.asList();

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

    public void saveAnschlag(Anschlag anschlag) {
        anschlag.setStatus(1);
        anschlag.setCreationDate(Instant.now().toEpochMilli());
        anschlag.setComments(new ArrayList<>());
        try {
            String anschlagJson = objectMapper.writeValueAsString(anschlag);
            Document anschlagDoc = Document.parse(anschlagJson);
            anschlagDoc.remove("_id");
            anschlagDoc.append("_id", new ObjectId());
            anschlaegeCol.insertOne(anschlagDoc);
            System.out.println("Anschlag saved successfully.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error saving anschlag.");
        }
    }

    public void updateAnschlag(Anschlag anschlag) {
        System.out.println(anschlag.get_id());

        try {
            String anschlagJson = objectMapper.writeValueAsString(anschlag);
            Document anschlagDoc = Document.parse(anschlagJson);
            Bson filter = Filters.eq("_id", new ObjectId(anschlagDoc.getString("_id")));

            anschlagDoc.remove("_id");

            Bson update = Updates.combine(Updates.set("title", "TEST"));

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

}
