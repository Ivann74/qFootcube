package me.qajic.plugins.qfootcube.core;

import me.qajic.plugins.qfootcube.*;
import me.qajic.plugins.qfootcube.features.DisableCommands;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.statistics.Highscores;
import me.qajic.plugins.qfootcube.statistics.Stats;
import me.qajic.plugins.qfootcube.statistics.UUIDConverter;
import org.bukkit.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.Collection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import java.io.File;
import org.bukkit.plugin.Plugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.entity.Slime;
import java.util.ArrayList;

import org.bukkit.event.Listener;

@SuppressWarnings("ALL")
public class Organization implements Listener
{
    private Footcube plugin;
    public DisableCommands disableCommands;
    public Highscores highscores;
    public String pluginString;
    public String adminString;
    public String or;
    public String setupGuy;
    public int setupType;
    public Location setupLoc;
    public Match[] matches1v1;
    public Match[] matches2v2;
    public Match[] matches3v3;
    public Match[] matches4v4;
    public int lobby1v1;
    public int lobby2v2;
    public int lobby3v3;
    public int lobby4v4;
    public ArrayList<Slime> practiceBalls;
    public HashMap<String, Integer> waitingPlayers;
    public ArrayList<String> playingPlayers;
    public HashMap<Player, Player> team;
    public HashMap<Player, Player> teamReverse;
    public HashMap<Player, Integer> teamType;
    public Player[][] waitingTeams;
    public ArrayList<Player> waitingTeamPlayers;
    public Match[] leftMatches;
    public boolean[] leftPlayerIsRed;
    public long announcementTime;
    public Stats wins;
    public Stats matches;
    public Stats ties;
    public Stats goals;
    public Stats assists;

    public Stats winStreak;
    public Stats bestWinStreak;
    public UUIDConverter uuidConverter;
    public Economy economy;

    @SuppressWarnings("ALL")
    public Organization(final Footcube pl) {
        this.highscores = null;
        this.pluginString = ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("prefix"));
        this.adminString = ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("adminPrefix"));
        this.or = ChatColor.YELLOW + "|" + ChatColor.AQUA;
        this.setupGuy = null;
        this.setupType = 0;
        this.setupLoc = null;
        this.matches1v1 = new Match[0];
        this.matches2v2 = new Match[0];
        this.matches3v3 = new Match[0];
        this.matches4v4 = new Match[0];
        this.lobby1v1 = 0;
        this.lobby2v2 = 0;
        this.lobby3v3 = 0;
        this.lobby4v4 = 0;
        this.practiceBalls = new ArrayList<Slime>();
        this.waitingPlayers = new HashMap<String, Integer>();
        this.playingPlayers = new ArrayList<String>();
        this.team = new HashMap<Player, Player>();
        this.teamReverse = new HashMap<Player, Player>();
        this.teamType = new HashMap<Player, Integer>();
        this.waitingTeams = new Player[0][0];
        this.waitingTeamPlayers = new ArrayList<Player>();
        this.leftMatches = new Match[0];
        this.leftPlayerIsRed = new boolean[0];
        this.wins = new Stats();
        this.matches = new Stats();
        this.ties = new Stats();
        this.goals = new Stats();
        this.assists = new Stats();
        this.winStreak = new Stats();
        this.bestWinStreak = new Stats();
        this.uuidConverter = new UUIDConverter();
        this.economy = null;
        this.plugin = pl;
        this.disableCommands = new DisableCommands(this.plugin, this);
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        final FileConfiguration cfg = this.plugin.getConfig();
        cfg.addDefault("arenas.1v1.amount", (Object)0);
        cfg.addDefault("arenas.2v2.amount", (Object)0);
        cfg.addDefault("arenas.3v3.amount", (Object)0);
        cfg.addDefault("arenas.4v4.amount", (Object)0);
        cfg.addDefault("kickpower", (Object)2.0);
        cfg.addDefault("uc_kickpower", (Object)1.6);
        cfg.addDefault("charge", (Object)5);
        cfg.addDefault("afterMatchRespawn", (Object)0);
        cfg.options().copyDefaults(true);
        this.plugin.saveConfig();
        this.loadArenas(cfg);
        this.wins.setup("plugins" + File.separator + "qFootcube" + File.separator + "wins.stats");
        this.matches.setup("plugins" + File.separator + "qFootcube" + File.separator + "matches.stats");
        this.ties.setup("plugins" + File.separator + "qFootcube" + File.separator + "ties.stats");
        this.goals.setup("plugins" + File.separator + "qFootcube" + File.separator + "goals.stats");
        this.assists.setup("plugins" + File.separator + "qFootcube" + File.separator + "assists.stats");
        this.winStreak.setup("plugins" + File.separator + "qFootcube" + File.separator + "winStreak.stats");
        this.bestWinStreak.setup("plugins" + File.separator + "qFootcube" + File.separator + "bestWinStreak.stats");
        this.uuidConverter.setup("plugins" + File.separator + "qFootcube" + File.separator + "UUID.data");
        this.wins.load();
        this.matches.load();
        this.ties.load();
        this.goals.load();
        this.assists.load();
        this.winStreak.load();
        this.bestWinStreak.load();
        this.uuidConverter.load();
        Collection<? extends Player> onlinePlayers;
        for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
            final Player p = (Player)onlinePlayers.toArray()[i];
            if (!this.uuidConverter.has(p.getUniqueId().toString())) {
                this.uuidConverter.put(p.getUniqueId().toString(), p.getName());
            }
        }
        this.setupEconomy();
        if (cfg.contains("arenas.world")) {
            for (final Entity e : this.plugin.getServer().getWorld(cfg.getString("arenas.world")).getEntities()) {
                if (e instanceof Slime) {
                    ((Slime)e).setHealth(0.0);
                }
            }
        }
        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                Organization.this.update();
            }
        }, 1L, 1L);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final String uuid = p.getUniqueId().toString();
        this.clearInventory(p);

        if (!this.winStreak.has(uuid)) {
            this.winStreak.put(uuid, 0);
        }
        if (!this.bestWinStreak.has(uuid)) {
            this.bestWinStreak.put(uuid, 0);
        }
        this.uuidConverter.put(p.getUniqueId().toString(), p.getName());
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (this.waitingPlayers.containsKey(p.getName())) {
            this.waitingPlayers.remove(p.getName());
        }
        if (this.playingPlayers.contains(p.getName())) {
            this.playingPlayers.remove(p.getName());
        }
        if (this.team.containsKey(p)) {
            final Player player = this.team.get(p);
            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("requestDeny").replace("{player}", p.getName())));
            this.teamType.remove(player);
            this.teamReverse.remove(player);
            this.team.remove(p);
        }
        else if (this.teamReverse.containsKey(p)) {
            final Player player = this.teamReverse.get(p);
            this.teamType.remove(p);
            this.teamReverse.remove(p);
            this.team.remove(player);
        }
        else if (this.waitingTeamPlayers.contains(p)) {
            for (int i = 0; i < this.waitingTeams.length; ++i) {
                if (this.waitingTeams[i][0] == p) {
                    final Player player2 = this.waitingTeams[i][1];
                    player2.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teammateLeave")));
                    this.waitingTeams = this.reduceArray(this.waitingTeams, p);
                    this.waitingTeamPlayers.remove(p);
                    this.waitingTeamPlayers.remove(player2);
                }
                else if (this.waitingTeams[i][1] == p) {
                    final Player player2 = this.waitingTeams[i][0];
                    player2.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teammateLeave")));
                    this.waitingTeams = this.reduceArray(this.waitingTeams, p);
                    this.waitingTeamPlayers.remove(p);
                    this.waitingTeamPlayers.remove(player2);
                }
            }
        }
    }

    public int findArena(final Player p) {
        for (int i = 0; i < this.matches1v1.length; ++i) {
            if (this.matches1v1[i].isRed.containsKey(p)) {
                return i;
            }
        }
        for (int i = 0; i < this.matches2v2.length; ++i) {
            if (this.matches2v2[i].isRed.containsKey(p)) {
                return i;
            }
        }
        for (int i = 0; i < this.matches3v3.length; ++i) {
            if (this.matches3v3[i].isRed.containsKey(p)) {
                return i;
            }
        }
        for (int i = 0; i < this.matches4v4.length; ++i) {
            if (this.matches4v4[i].isRed.containsKey(p)) {
                return i;
            }
        }
        return 69;
    }
    public void matchStart(final int type) {
        if (type == 1) {
            for (int i = 0; i < this.matches1v1.length; ++i) {
                if (this.matches1v1[i].phase == 1) {
                    this.lobby1v1 = i;
                    break;
                }
            }
            for (int i = 0; i < this.waitingTeams.length; ++i) {
                if (this.waitingTeams[i].length > 0 && this.matches1v1[this.lobby1v1].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
                    this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
                }
            }
        }
        else if (type == 2) {
            for (int i = 0; i < this.matches2v2.length; ++i) {
                if (this.matches2v2[i].phase == 1) {
                    this.lobby2v2 = i;
                    break;
                }
            }
            for (int i = 0; i < this.waitingTeams.length; ++i) {
                if (this.waitingTeams[i].length > 1 && this.matches2v2[this.lobby2v2].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
                    this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
                }
            }
        }
        else if (type == 3) {
            for (int i = 0; i < this.matches3v3.length; ++i) {
                if (this.matches3v3[i].phase == 1) {
                    this.lobby3v3 = i;
                    break;
                }
            }
            for (int i = 0; i < this.waitingTeams.length; ++i) {
                if (this.waitingTeams[i].length > 2 && this.matches3v3[this.lobby3v3].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
                    this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
                }
            }
        }
        else {
            for (int i = 0; i < this.matches4v4.length; ++i) {
                if (this.matches4v4[i].phase == 1) {
                    this.lobby4v4 = i;
                    break;
                }
            }
            for (int i = 0; i < this.waitingTeams.length; ++i) {
                if (this.waitingTeams[i].length < 3 && this.matches4v4[this.lobby4v4].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
                    this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
                    this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
                }
            }
        }
    }
    
    public void playerLeaves(final Match m, final boolean red) {
        this.leftMatches = this.extendArray(this.leftMatches, m);
        this.leftPlayerIsRed = this.extendArray(this.leftPlayerIsRed, red);
        if (this.leftMatches.length < 2) {
            this.announcementTime = System.currentTimeMillis();
            Collection<? extends Player> onlinePlayers;
            for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
                final Player p = (Player)onlinePlayers.toArray()[i];
                if (!this.playingPlayers.contains(p.getName()) && !this.waitingPlayers.containsKey(p.getName())) {
                    if (m.time.getScore() < 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerLeft").replace("{matchType}", m.type + "v" + m.type)));
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerLeft").replace("{matchType}", m.type + "v" + m.type) + MessagesConfig.get().getString("playerLeftSeconds").replace("{seconds}", ""+m.time.getScore())));
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tkp")));
                }
            }
        }
    }
    
    public void undoTakePlace(final Match m) {
        int matches = 0;
        Match[] leftMatches;
        for (int length = (leftMatches = this.leftMatches).length, k = 0; k < length; ++k) {
            final Match match = leftMatches[k];
            if (m.equals(match)) {
                ++matches;
            }
        }
        final Match[] newL = new Match[this.leftMatches.length - matches];
        final boolean[] newB = new boolean[this.leftMatches.length - matches];
        int i = 0;
        int j = 0;
        while (i < this.leftMatches.length) {
            if (!this.leftMatches[i].equals(m)) {
                newL[j] = this.leftMatches[i];
                newB[j] = this.leftPlayerIsRed[i];
                ++j;
            }
            ++i;
        }
        this.leftMatches = newL;
        this.leftPlayerIsRed = newB;
    }
    
    public void endMatch(final Player p) {
            this.playingPlayers.remove(p.getName());
    }
    
    public void playerStarts(final Player p) {
            this.waitingPlayers.remove(p.getName());
            this.playingPlayers.add(p.getName());
    }
    
    public void ballTouch(final Player p) {
        Match[] matches1v1;
        for (int length = (matches1v1 = this.matches1v1).length, i = 0; i < length; ++i) {
            final Match match = matches1v1[i];
            match.kick(p);
        }
        Match[] matches2v2;
        for (int k = (matches2v2 = this.matches2v2).length, n = 0; n < k; ++n) {
            final Match match2 = matches2v2[n];
            match2.kick(p);
        }
        Match[] matches3v3;
        for (int m = (matches3v3 = this.matches3v3).length, i2 = 0; i2 < m; ++i2) {
            final Match match3 = matches3v3[i2];
            match3.kick(p);
        }
        Match[] matches4v4;
        for (int length2 = (matches4v4 = this.matches4v4).length, j = 0; j < length2; ++j) {
            final Match match4 = matches4v4[j];
            match4.kick(p);
        }
    }

    public void tackle(final Player p) {
        Match[] matches2v2 = this.matches2v2;
        for (int length = (matches2v2 = this.matches2v2).length, i = 0; i < length; ++i) {
            final Match m = matches2v2[i];
            m.tackle(p);
        }
        Match[] matches3v3 = this.matches3v3;
        for (int length2 = (matches3v3 = this.matches3v3).length, j = 0; j < length2; ++j) {
            final Match k = matches3v3[j];
            k.tackle(p);
        }
        Match[] matches4v4 = this.matches4v4;
        for (int length3 = (matches4v4 = this.matches4v4).length, l = 0; l < length3; ++l) {
            final Match m2 = matches4v4[l];
            m2.tackle(p);
        }
    }

    public void assisttouch(final Player p) {
        Match[] matches2v2 = this.matches2v2;
        for (int length = (matches2v2 = this.matches2v2).length, i = 0; i < length; ++i) {
            final Match m = matches2v2[i];
            m.assist(p);
        }
        Match[] matches3v3 = this.matches3v3;
        for (int length2 = (matches3v3 = this.matches3v3).length, j = 0; j < length2; ++j) {
            final Match k = matches3v3[j];
            k.assist(p);
        }
        Match[] matches4v4 = this.matches4v4;
        for (int length3 = (matches4v4 = this.matches4v4).length, l = 0; l < length3; ++l) {
            final Match m2 = matches4v4[l];
            m2.assist(p);
        }
    }

    public ItemStack createComplexItem(final Material material, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        final ArrayList<String> loreArray = new ArrayList<String>();
        final String[] arrayOfString = lore;
        final int i = lore.length;
        for (byte b = 0; b < i; ++b) {
            final String s = arrayOfString[b];
            loreArray.add(s);
        }
        meta.setLore((List)loreArray);
        item.setItemMeta(meta);
        return item;
    }
    
    public void clearInventory(final Player p) {
        final PlayerInventory inv = p.getInventory();
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        for (int i = 0; i < inv.getContents().length; ++i) {
            final ItemStack is = inv.getContents()[i];
            if (is != null && is.getType() != Material.DIAMOND) {
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }
    
    public void removeTeam(final Player p) {
        if (this.team.containsKey(p)) {
            final Player player = this.team.get(p);
            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("requestDeny").replace("{player}", p.getName())));
            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinRequestDeny")));
            this.teamType.remove(player);
            this.teamReverse.remove(player);
            this.team.remove(p);
        }
        if (this.teamReverse.containsKey(p)) {
            final Player player = this.teamReverse.get(p);
            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("requestDeny").replace("{player}", p.getName())));
            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinRequestDeny")));
            this.teamType.remove(p);
            this.teamReverse.remove(p);
            this.team.remove(player);
        }
    }

    public Player[][] extendArray(final Player[][] oldL, final Player[] add) {
        final Player[][] newL = new Player[0][oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }

    public Player[][] reduceArray(final Player[][] oldL, final Player remove) {
        final Player[][] newL = new Player[0][oldL.length - 1];
        int i = 0;
        int j = 0;
        while (i < newL.length) {
            if (oldL[i][0] != remove && oldL[i][1] != remove) {
                newL[j] = oldL[i];
                ++j;
            }
            ++i;
        }
        return newL;
    }

    public Match[] extendArray(final Match[] oldL, final Match add) {
        final Match[] newL = new Match[oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }

    public boolean[] extendArray(final boolean[] oldL, final boolean add) {
        final boolean[] newL = new boolean[oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }

    public boolean isOnlinePlayer(final String s) {
        Collection<? extends Player> onlinePlayers;
        for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
            final Player p = (Player)onlinePlayers.toArray()[i];
            if (p.getName().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public void addArena(final int type, final Location b, final Location r) {
        final Location m = new Location(b.getWorld(), (b.getX() + r.getX()) / 2.0, (b.getY() + r.getY()) / 2.0 + 2.0, (b.getZ() + r.getZ()) / 2.0);
        if (type == 1) {
            this.matches1v1 = this.extendArray(this.matches1v1, new Match(this, this.plugin, 1, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
        else if (type == 2) {
            this.matches2v2 = this.extendArray(this.matches2v2, new Match(this, this.plugin, 2, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
        else if (type == 3) {
            this.matches3v3 = this.extendArray(this.matches3v3, new Match(this, this.plugin, 3, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
        else {
            this.matches4v4 = this.extendArray(this.matches4v4, new Match(this, this.plugin, 4, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
    }

    public void loadArenas(final FileConfiguration cfg) {
        for (int i = 1; i <= cfg.getInt("arenas.1v1.amount"); ++i) {
            final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
            final String blue = "arenas.1v1." + i + ".blue.";
            final String red = "arenas.1v1." + i + ".red.";
            final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
            b.setPitch((float)cfg.getDouble(blue + "pitch"));
            b.setYaw((float)cfg.getDouble(blue + "yaw"));
            final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
            r.setPitch((float)cfg.getDouble(red + "pitch"));
            r.setYaw((float)cfg.getDouble(red + "yaw"));
            this.addArena(1, b, r);
        }
        for (int i = 1; i <= cfg.getInt("arenas.2v2.amount"); ++i) {
            final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
            final String blue = "arenas.2v2." + i + ".blue.";
            final String red = "arenas.2v2." + i + ".red.";
            final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
            b.setPitch((float)cfg.getDouble(blue + "pitch"));
            b.setYaw((float)cfg.getDouble(blue + "yaw"));
            final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
            r.setPitch((float)cfg.getDouble(red + "pitch"));
            r.setYaw((float)cfg.getDouble(red + "yaw"));
            this.addArena(2, b, r);
        }
        for (int i = 1; i <= cfg.getInt("arenas.3v3.amount"); ++i) {
            final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
            final String blue = "arenas.3v3." + i + ".blue.";
            final String red = "arenas.3v3." + i + ".red.";
            final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
            b.setPitch((float)cfg.getDouble(blue + "pitch"));
            b.setYaw((float)cfg.getDouble(blue + "yaw"));
            final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
            r.setPitch((float)cfg.getDouble(red + "pitch"));
            r.setYaw((float)cfg.getDouble(red + "yaw"));
            this.addArena(3, b, r);
        }
        for (int i = 1; i <= cfg.getInt("arenas.4v4.amount"); ++i) {
            final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
            final String blue = "arenas.4v4." + i + ".blue.";
            final String red = "arenas.4v4." + i + ".red.";
            final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
            b.setPitch((float)cfg.getDouble(blue + "pitch"));
            b.setYaw((float)cfg.getDouble(blue + "yaw"));
            final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
            r.setPitch((float)cfg.getDouble(red + "pitch"));
            r.setYaw((float)cfg.getDouble(red + "yaw"));
            this.addArena(4, b, r);
        }
    }

    public void checkStats(final String uuid, final Player asker) {
        uuid.equals(asker.getUniqueId().toString());
        if (this.matches.has(uuid)) {
            final int m = this.matches.get(uuid);
            final int w = this.wins.get(uuid);
            final int t = this.ties.get(uuid);
            final int s = this.bestWinStreak.get(uuid);
            final int a = this.assists.get(uuid);
            final int l = m - w - t;
            double mw = m;
            if (w > 0) {
                mw = 100 * m / w / 100.0;
            }
            final int g = this.goals.get(uuid);
            double gm = 0.0;
            if (m > 0) {
                gm = 100 * g / m / 100.0;
            }
            final double multiplier = 1.0 - Math.pow(0.9, m);
            double goalBonus = 0.5;
            if (m > 0) {
                goalBonus = 1.0 - multiplier * Math.pow(0.2, g / m) - 0.5 / Math.pow(1.1111111111111112, m);
            }
            double addition = 0.0;
            if (m > 0 && w + t > 0) {
                addition = 8.0 * (1.0 / (100 * m / (w + 0.5 * t) / 100.0)) - 4.0;
            }
            else if (m > 0) {
                addition = -4.0;
            }
            final double skillLevel = (int)(100.0 * (5.0 + goalBonus + addition * multiplier)) / 100.0;
            final int rank = (int)(skillLevel * 2.0 - 0.5);
            String rang = null;
            switch (rank) {
                case 1: {
                    rang = "Noob";
                    break;
                }
                case 2: {
                    rang = "Loser";
                    break;
                }
                case 3: {
                    rang = "Baby";
                    break;
                }
                case 4: {
                    rang = "Pupil";
                    break;
                }
                case 5: {
                    rang = "Bad";
                    break;
                }
                case 6: {
                    rang = "Sadface";
                    break;
                }
                case 7: {
                    rang = "Meh";
                    break;
                }
                case 8: {
                    rang = "Player";
                    break;
                }
                case 9: {
                    rang = "Ok";
                    break;
                }
                case 10: {
                    rang = "Average";
                    break;
                }
                case 11: {
                    rang = "Well";
                    break;
                }
                case 12: {
                    rang = "Good";
                    break;
                }
                case 13: {
                    rang = "King";
                    break;
                }
                case 14: {
                    rang = "Superb";
                    break;
                }
                case 15: {
                    rang = "Pro";
                    break;
                }
                case 16: {
                    rang = "Maradona";
                    break;
                }
                case 17: {
                    rang = "Superman";
                    break;
                }
                case 18: {
                    rang = "God";
                    break;
                }
                case 19: {
                    rang = "Hacker";
                    break;
                }
            }
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats")));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats1").replace("{playedMatches}", ""+m)));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats2").replace("{wins}", ""+w).replace("{losses}", ""+l)).replace("{tied}", ""+t));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats7").replace("{assists}", ""+a)));
            if (w > 0) {
                asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats3").replace("{avg}", ""+mw)));
            }
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats4").replace("{winStreak}", ""+s)));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats5").replace("{goals}", ""+g)));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("stats6").replace("{goals}", ""+gm)));
        }
        else {
            asker.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("statsNoMatchs")));
        }
    }

    public void updateHighscores(final Player p) {
        if (this.highscores == null) {
            this.highscores = new Highscores(this, this.plugin, p);
        }
        else if (this.highscores.needsUpdate()) {
            this.highscores.update(p);
        }
        else if (this.highscores.isUpdating) {
            this.highscores.addWaitingPlayer(p);
        }
        else {
            this.highscores.showHighscores(p);
        }
    }

    public void update() {
        for (int i = 0; i < this.matches1v1.length; ++i) {
            this.matches1v1[i].update();
        }
        for (int i = 0; i < this.matches2v2.length; ++i) {
            this.matches2v2[i].update();
        }
        for (int i = 0; i < this.matches3v3.length; ++i) {
            this.matches3v3[i].update();
        }
        for (int i = 0; i < this.matches4v4.length; ++i) {
            this.matches4v4[i].update();
        }
        if (this.leftMatches.length > 0 && System.currentTimeMillis() - this.announcementTime > 30000L) {
            final Match m = this.leftMatches[0];
            this.announcementTime = System.currentTimeMillis();
            Collection<? extends Player> onlinePlayers;
            for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), j = 0; j < length; ++j) {
                final Player p = (Player)onlinePlayers.toArray()[j];
                if (!this.playingPlayers.contains(p.getName()) && !this.waitingPlayers.containsKey(p.getName())) {
                    if (m.time.getScore() < 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerLeft").replace("{matchType}", m.type + "v" + m.type)));
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerLeft").replace("{matchType}", m.type + "v" + m.type) + MessagesConfig.get().getString("playerLeftSeconds").replace("{seconds}", "" + m.time.getScore())));
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tkp")));
                }
            }
        }
    }

    public boolean setupEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>)this.plugin.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economyProvider != null) {
            this.economy = (Economy)economyProvider.getProvider();
        }
        return this.economy != null;
    }
}
