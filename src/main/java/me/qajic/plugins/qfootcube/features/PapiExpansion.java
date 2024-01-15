package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.Footcube;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.qajic.plugins.qfootcube.utils.Footballer;
import me.qajic.plugins.qfootcube.utils.Leaderboard;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

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
    static boolean isInt(String s)
    {
        try
        { int i = Integer.parseInt(s); return true; }

        catch(NumberFormatException er)
        { return false; }
    }
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        String[] args = params.split("\\.");

        if(params.equalsIgnoreCase("team")){
            CachedMetaData metaData = this.plugin.luckPermsAPI.getPlayerAdapter(Player.class).getMetaData(player);
            return metaData.getMetaValue("team", String::toString).orElse("Not Found");
        }

        if (args[0].equalsIgnoreCase("matches_in_progress")) {
            if (args.length > 2) {
                if (args[1].equalsIgnoreCase("1v1")) {
                    return ""+this.plugin.organization.matchesInProgress1v1;
                } else if (args[1].equalsIgnoreCase("2v2")) {
                    return ""+this.plugin.organization.matchesInProgress2v2;
                } else if (args[1].equalsIgnoreCase("3v3")) {
                    return ""+this.plugin.organization.matchesInProgress3v3;
                } else if (args[1].equalsIgnoreCase("4v4")) {
                    return ""+this.plugin.organization.matchesInProgress4v4;
                } else if (args[1].equalsIgnoreCase("5v5")) {
                    return ""+this.plugin.organization.matchesInProgress5v5;
                } else {
                    return null;
                }
            }
        }

        int id=0;
        Leaderboard leaderboard = new Leaderboard(this.plugin,"plugins" + File.separator + "qFootcube" + File.separator + "playerdata");
        if (args.length > 2 && isInt(args[2])) {
            id = parseInt(args[2]) - 1;
            if (id < 0)
                id = 0;
        }
        if (args[0].equalsIgnoreCase("most_wins")) {
            if (args.length > 2) {
                List<Footballer> wins = leaderboard.getLeaderboard("wins");
                if (args[1].equalsIgnoreCase("username")) {
                    return wins.get(id).getUsername();
                } else if (args[1].equalsIgnoreCase("value")) {
                    return ""+wins.get(id).getWins();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (args[0].equalsIgnoreCase("most_goals")) {
            if (args.length > 2) {
                List<Footballer> goals = leaderboard.getLeaderboard("goals");
                if (args[1].equalsIgnoreCase("username")) {
                    return goals.get(id).getUsername();
                } else if (args[1].equalsIgnoreCase("value")) {
                    return ""+goals.get(id).getGoals();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (args[0].equalsIgnoreCase("longest_win_streak")) {
            if (args.length > 2) {
                List<Footballer> winstreak = leaderboard.getLeaderboard("best_win_streak");
                if (args[1].equalsIgnoreCase("username")) {
                    return winstreak.get(id).getUsername();
                } else if (args[1].equalsIgnoreCase("value")) {
                    return ""+winstreak.get(id).getBestWinStreak();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        if (args[0].equalsIgnoreCase("most_assists")) {
            if (args.length > 2) {
                List<Footballer> assists = leaderboard.getLeaderboard("assists");
                if (args[1].equalsIgnoreCase("username")) {
                    return assists.get(id).getUsername();
                } else if (args[1].equalsIgnoreCase("value")) {
                    return ""+assists.get(id).getAssists();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }
}
