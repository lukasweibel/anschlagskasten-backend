package ch.lukasweibel.anschlagkasten.db;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
        /*
         * try {
         * String anschlagJson = objectMapper.writeValueAsString(anschlag);
         * Document anschlagDoc = Document.parse(anschlagJson);
         * Bson filter = Filters.eq("_id", new ObjectId(anschlagDoc.getString("_id")));
         * anschlagDoc.get("_id");
         * Bson updateOperation = Updates.set("fieldToUpdate",
         * anschlagDoc.get("fieldToUpdate"));
         * anschlaegeCol.updateOne(filter, updateOperation, null);
         * } catch (JsonProcessingException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         */
    }

}
