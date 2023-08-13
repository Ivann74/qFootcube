package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.configuration.PapiConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PapiExpansion extends PlaceholderExpansion {


    private final Footcube plugin;

    public PapiExpansion(Footcube plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "qajic";
    }

    @Override
    public String getIdentifier() {
        return "footcube";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("mostwins1")){
            String output = PapiConfig.get().getString("mostWins1");
            return output;
        }
        if(params.equalsIgnoreCase("mostwins2")){
            String output = PapiConfig.get().getString("mostWins2");
            return output;
        }
        if(params.equalsIgnoreCase("mostwins3")){
            String output = PapiConfig.get().getString("mostWins3");
            return output;
        }
        if(params.equalsIgnoreCase("mostgoals1")){
            String output = PapiConfig.get().getString("mostGoals1");
            return output;
        }
        if(params.equalsIgnoreCase("mostgoals2")){
            String output = PapiConfig.get().getString("mostGoals2");
            return output;
        }
        if(params.equalsIgnoreCase("mostgoals3")){
            String output = PapiConfig.get().getString("mostGoals3");
            return output;
        }
        if(params.equalsIgnoreCase("longeststreak1")){
            String output = PapiConfig.get().getString("longestStreak1");
            return output;
        }
        if(params.equalsIgnoreCase("longeststreak2")){
            String output = PapiConfig.get().getString("longestStreak2");
            return output;
        }
        if(params.equalsIgnoreCase("longeststreak3")){
            String output = PapiConfig.get().getString("longestStreak3");
            return output;
        }
        if(params.equalsIgnoreCase("mostAssists1")){
            String output = PapiConfig.get().getString("mostAssists1");
            return output;
        }
        if(params.equalsIgnoreCase("mostAssists2")){
            String output = PapiConfig.get().getString("mostAssists2");
            return output;
        }
        if(params.equalsIgnoreCase("mostAssists3")){
            String output = PapiConfig.get().getString("mostAssists3");
            return output;
        }
        if(params.equalsIgnoreCase("team")){
            CachedMetaData metaData = this.plugin.luckPermsAPI.getPlayerAdapter(Player.class).getMetaData(player);
            return metaData.getMetaValue("team", String::toString).orElse("Not Found");
        }

        return null;
    }
}
