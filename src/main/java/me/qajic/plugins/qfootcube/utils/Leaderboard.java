package me.qajic.plugins.qfootcube.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import me.qajic.plugins.qfootcube.Footcube;

//    public static List<Footballer> getLeaderboard(List<Footballer> footballers, String type) {
//        switch (type.toLowerCase()) {
//            case "goals":
//                footballers.sort(Comparator.comparingInt(Footballer::getGoals).reversed());
//                break;
//            case "assists":
//                footballers.sort(Comparator.comparingInt(Footballer::getAssists).reversed());
//                break;
//            case "wins":
//                footballers.sort(Comparator.comparingInt(Footballer::getWins).reversed());
//                break;
//            case "ties":
//                footballers.sort(Comparator.comparingInt(Footballer::getTies).reversed());
//                break;
//            case "matches":
//                footballers.sort(Comparator.comparingInt(Footballer::getMatches).reversed());
//                break;
//            case "best_win_streak":
//                footballers.sort(Comparator.comparingInt(Footballer::getBestWinStreak).reversed());
//            default:
//                footballers.sort(Comparator.comparingInt(Footballer::getGoals).reversed());
//        }
//
//        return footballers;
//    }
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.util.*;

public class Leaderboard {
    private final Footcube plugin;
    private List<Footballer> footballers;

    public Leaderboard(Footcube plugin, String folderPath) {
        this.footballers = new ArrayList<>();
        this.plugin = plugin;
        loadFootballers(folderPath);
    }

    private void loadFootballers(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".yml")) {
                    UUID uuid = UUID.fromString(file.getName().split("\\.")[0]);
                    if(this.plugin.organization.uuidConverter.hasValue(uuid)) {
                        PlayerDataManager playerData = new PlayerDataManager(this.plugin, uuid);
                        Footballer footballer = new Footballer();
                        footballer.setUsername(this.plugin.organization.uuidConverter.getKey(uuid));
                        footballer.setGoals(playerData.getInt("goals"));
                        footballer.setAssists(playerData.getInt("assists"));
                        footballer.setTies(playerData.getInt("ties"));
                        footballer.setMatches(playerData.getInt("matches"));
                        footballer.setWins(playerData.getInt("wins"));
                        footballer.setBestWinStreak(playerData.getInt("best_win_streak"));
                        footballers.add(footballer);
                    }
                }
            }
        }
    }

    public List<Footballer> getLeaderboard(String type) {
        switch (type.toLowerCase()) {
            case "goals":
                footballers.sort(Comparator.comparingInt(Footballer::getGoals).reversed());
                break;
            case "assists":
                footballers.sort(Comparator.comparingInt(Footballer::getAssists).reversed());
                break;
            case "wins":
                footballers.sort(Comparator.comparingInt(Footballer::getWins).reversed());
                break;
            case "ties":
                footballers.sort(Comparator.comparingInt(Footballer::getTies).reversed());
                break;
            case "matches":
                footballers.sort(Comparator.comparingInt(Footballer::getMatches).reversed());
                break;
            case "best_win_streak":
                footballers.sort(Comparator.comparingInt(Footballer::getBestWinStreak).reversed());
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        if(footballers.size() < 3) {
            footballers.add(footballers.get(0));
            footballers.add(footballers.get(0));
        }
        return new ArrayList<>(footballers);
    }
}
