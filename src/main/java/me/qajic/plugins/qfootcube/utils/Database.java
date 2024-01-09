package me.qajic.plugins.qfootcube.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.qajic.plugins.qfootcube.Footcube;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;

public class Database {
    private final MongoDatabase db;
    private final Footcube plugin;
    public Database(Footcube pl, MongoDatabase db) {
        this.plugin = pl;
        this.db = db;
    }
    public boolean checkForConnection() {
        try {
            this.plugin.mongoClient.getClusterDescription();
            this.plugin.mongoClient.listDatabases();
            return true;
        } catch (Exception e) {
            System.out.println("Database unavailable!");
            this.plugin.mongoClient.close();
            return false;
        }
    }
    public void createPlayerDocument(Player player) {
        if(this.db == null && checkForConnection())
            return;
        try {
            this.db.createCollection("players");
        } catch (MongoCommandException ignored) { }
        String username = player.getName();
        MongoCollection<Document> players = this.db.getCollection("players");
        if(!userExists(username)) {
            Document document = new Document();
            document.append("username", username);
            document.append("matches", 0);
            document.append("wins", 0);
            document.append("ties", 0);
            document.append("goals", 0);
            document.append("assists", 0);
            document.append("win_streak", 0);
            document.append("best_win_streak", 0);
            document.append("goal_explosion", "Disable");
            document.append("particle", "Disable");
            document.append("custom-score-message", "");
            players.insertOne(document);
        }
    }
    public List<Document> getDocs(String key) {
        MongoCollection<Document> players = this.db.getCollection(key);
        List<Document> documents = new ArrayList<>();
        players.find().sort(descending("username")).into(documents);
        return documents;
    }
    public void updateString(String collection, String id, String key, String value) {
        MongoCollection<Document> players = this.db.getCollection(collection);
        if(!userExists(id))
            return;

        Document filter = new Document();
        filter.append("username", id);

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.put(key, value);

        BasicDBObject updates = new BasicDBObject();
        updates.put("$set", updateDocument);

        players.findOneAndUpdate(filter, updates);
    }

    public String getString(String collection, String id, String key) {
        MongoCollection<Document> players = this.db.getCollection(collection);
        if(!userExists(id))
            return null;
        Document data = players.find(new Document("username", id)).first();
        assert data != null;
        return data.getString(key);
    }

    public int getInt(String collection, String id, String key) {
        MongoCollection<Document> players = this.db.getCollection(collection);
        if(!userExists(id))
            return -1;
        Document data = players.find(new Document("username", id)).first();
        assert data != null;
        return data.getInteger(key);
    }

    public void updateInt(String collection, String id, String key, Integer value) {
        MongoCollection<Document> players = this.db.getCollection(collection);
        if(!userExists(id))
            return;

        Document filter = new Document();
        filter.append("username", id);

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.put(key, value);

        BasicDBObject updates = new BasicDBObject();
        updates.put("$set", updateDocument);

        players.findOneAndUpdate(filter, updates);
    }

    public void updateDouble(String collection, String id, String key, Double value) {
        MongoCollection<Document> players = this.db.getCollection(collection);
        if(!userExists(id))
            return;

        Document filter = new Document();
        filter.append("username", id);

        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.put(key, value);

        BasicDBObject updates = new BasicDBObject();
        updates.put("$set", updateDocument);

        players.findOneAndUpdate(filter, updates);
    }

    public boolean userExists(String username) {
        FindIterable<Document> iterable = this.db.getCollection("players").find(new Document("username", username));
        return iterable.first() != null;
    }

}
