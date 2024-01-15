package me.qajic.plugins.qfootcube.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

public class Leaderboard {
    public static List<Footballer> getLeaderboard(List<Footballer> footballers, String type) {
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
            default:
                footballers.sort(Comparator.comparingInt(Footballer::getGoals).reversed());
        }

        return footballers;
    }

    public static List<Path> listFiles(String folderPath) {
        List<Path> files = new ArrayList<>();
        try {
            Files.list(Paths.get(folderPath)).forEach(files::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static List<Footballer> readPlayerData(List<Path> files) {
        List<Footballer> players = new ArrayList<>();
        Yaml yaml = new Yaml();

        for (Path file : files) {
            try {
                String fileName = file.getFileName().toString();
                UUID uuid = UUID.fromString(fileName.substring(0, fileName.lastIndexOf('.')));
                System.out.println("UUID: "+ uuid);
                String content = new String(Files.readAllBytes(file));
                Footballer player = yaml.loadAs(content, Footballer.class);
                player.setUuid(uuid);

                players.add(player);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return players;
    }
}
