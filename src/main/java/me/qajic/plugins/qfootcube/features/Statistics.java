package me.qajic.plugins.qfootcube.features;

import com.mongodb.client.MongoCollection;
import me.qajic.plugins.qfootcube.Footcube;
import org.bson.Document;


import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;

public class Statistics {
    private final Footcube plugin;
    public Statistics(Footcube pl) {
        this.plugin = pl;
    }
    public void riseStats(String username, String key) {
        this.plugin.organization.db.updateInt("players", username, key, this.plugin.organization.db.getInt("players", username, key)+1);
    }
    public List<Document> getHighscore(String key) {
        MongoCollection<Document> players = this.plugin.database.getCollection("players");
        List<Document> documents = new ArrayList<>();
        players.find().sort(descending(key)).into(documents);
        if(documents.size() < 3) {
            documents.add(documents.get(0));
            documents.add(documents.get(0));
        }

        return documents;
    }

}
