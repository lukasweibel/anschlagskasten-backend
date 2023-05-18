package ch.lukasweibel.anschlagkasten.db;

import org.bson.Document;
import org.slf4j.LoggerFactory;

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

    public DbAccessor() {
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

    public ArrayList<Document> getAnschlaege() {
        ArrayList<Document> anschlaegeList = new ArrayList<>();

        List<Document> pipeline = Arrays.asList();

        AggregateIterable<Document> result = anschlaegeCol.aggregate(pipeline);

        for (Document document : result) {
            anschlaegeList.add(document);
        }

        return anschlaegeList;
    }

}
