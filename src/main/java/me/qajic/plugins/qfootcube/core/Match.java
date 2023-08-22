package me.qajic.plugins.qfootcube.core;

import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.utils.GoalExplosion;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Score;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.event.Listener;

@SuppressWarnings("ALL")
public class Match implements Listener
{
    public int matchID;
    public int type;
    public int phase;
    private int countdown;
    private int tickToSec;
    private int teams;
    private int count;
    private long startTime;
    private Location blue;
    private Location red;
    private Location mid;
    private Organization organization;
    private Footcube plugin;
    private boolean x;
    private boolean redAboveBlue;
    private Player[] redPlayers;
    private Player[] bluePlayers;
    private ArrayList<Player> teamers;
    private ArrayList<Player> takePlace;
    public HashMap<Player, Boolean> isRed;
    private Player lastKickRed;
    private Player lastKickBlue;
    public int scoreRed;
    public int scoreBlue;
    private HashMap<Player, Integer> goals;
    private HashMap<Player, Integer> assists;

    private ItemStack redChestPlate;
    private ItemStack redLeggings;
    private ItemStack blueChestPlate;
    private ItemStack blueLeggings;
    public Score time;
    private Score redGoals;
    private Score blueGoals;
    private ScoreboardManager sbm;
    private Scoreboard sb;
    private Objective o;
    private Slime cube;
    public ArrayList<Player> forfeitRed;
    public ArrayList<Player> forfeitBlue;
    public ArrayList<Player> redassist;
    public ArrayList<Player> blueassist;
    Sidebar pSidebar;
    Sidebar igSidebar;
    boolean pgset;
    boolean pgclosed;
    int ar;
    private int _countdown;
    public Boolean overtime;
    private int overtimeEnd;
    public int overtimeCount;
    public void spawnFirework(Location location) {
        Firework f = (Firework) location.getWorld().spawn(location, Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .trail(true)
                .withColor(Color.ORANGE)
                .withColor(Color.BLUE)
                .withFade(Color.BLUE)
                .build());
        fm.setPower(0);
        f.setFireworkMeta(fm);
        f.detonate();
    }
    public Vector explosionVector(Player player, Location source, double power)
    {
        double exposure = 3;

        double x1 = source.getX();
        double x2 = player.getEyeLocation().getX();

        double y1 = source.getY();
        double y2 = player.getEyeLocation().getY();

        double z1 = source.getZ();
        double z2 = player.getEyeLocation().getZ();

        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));

        double multiplier = (1 - (distance / (power * 2))) * exposure;

        Vector vector = player.getLocation().toVector().subtract(source.toVector()).normalize().multiply(multiplier);

        return player.getVelocity().add(vector);
    }

    public Match(final Organization org, final Footcube pl, final int t, final Location b, final Location r, final Location m, final int id) {
        this.count = 4;
        this.overtime = false;
        this.overtimeCount = 0;
        this.redPlayers = new Player[0];
        this.bluePlayers = new Player[0];
        this.redassist = new ArrayList<Player>();
        this.blueassist = new ArrayList<Player>();
        this.forfeitBlue = new ArrayList<Player>();
        this.forfeitRed = new ArrayList<Player>();
        this.assists = new HashMap<Player, Integer>();
        this.teamers = new ArrayList<Player>();
        this.takePlace = new ArrayList<Player>();
        this.isRed = new HashMap<Player, Boolean>();
        this.lastKickRed = null;
        this.lastKickBlue = null;
        this.goals = new HashMap<Player, Integer>();
        this.matchID = id;
        this.organization = org;
        this._countdown=0;
        this.plugin = pl;
        this.pgclosed=false;
        this.pgset=false;
        this.type = t;
        this.pSidebar = this.plugin.scoreboardLibrary.createSidebar();
        this.igSidebar = this.plugin.scoreboardLibrary.createSidebar();
        this.blue = b;
        this.red = r;
        this.mid = m;
        this.overtimeEnd=1;
        this.phase = 1;
        this.scoreRed = 0;
        this.scoreBlue = 0;
        this.startTime = 0L;
        this.redChestPlate = this.createColoredArmour(Material.LEATHER_CHESTPLATE, Color.RED);
        this.redLeggings = this.createColoredArmour(Material.LEATHER_LEGGINGS, Color.RED);
        this.blueChestPlate = this.createColoredArmour(Material.LEATHER_CHESTPLATE, Color.BLUE);
        this.blueLeggings = this.createColoredArmour(Material.LEATHER_LEGGINGS, Color.BLUE);
        this.sbm = Bukkit.getScoreboardManager();
        this.sb = this.sbm.getNewScoreboard();
        this.ar = 0;
        boolean objectiveExists = false;
        for (final Objective ob : this.sb.getObjectives()) {
            if (ob.getName().equalsIgnoreCase("Match")) {
                objectiveExists = true;
                break;
            }
        }
        if (objectiveExists) {
            (this.o = this.sb.getObjective("Utakmica")).setDisplayName(ChatColor.DARK_GRAY + " " + ChatColor.AQUA + ChatColor.BOLD + "MATCH " + ChatColor.WHITE + ChatColor.BOLD + "INFO" + ChatColor.DARK_GRAY);
        }
        else {
            (this.o = this.sb.registerNewObjective("Utakmica", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
            this.o.setDisplayName(ChatColor.DARK_GRAY + " " + ChatColor.AQUA + ChatColor.BOLD + "MATCH " + ChatColor.WHITE + ChatColor.BOLD + "INFO" + ChatColor.DARK_GRAY);
        }
        (this.time = this.o.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE + "Time"))).setScore(180);
        (this.redGoals = this.o.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Red"))).setScore(0);
        (this.blueGoals = this.o.getScore(Bukkit.getOfflinePlayer(ChatColor.BLUE + "Blue"))).setScore(0);
        this.x = (Math.abs(b.getX() - r.getX()) > Math.abs(b.getZ() - r.getZ()));
        if (this.x) {
            if (r.getX() > b.getX()) {
                this.redAboveBlue = true;
            }
            else {
                this.redAboveBlue = false;
            }
        }
        else if (r.getZ() > b.getZ()) {
            this.redAboveBlue = true;
        }
        else {
            this.redAboveBlue = false;
        }
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
    }
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (this.isRed.containsKey(p)) {
            this.organization.clearInventory(p);
            if (this.phase != 1) {
                this.organization.playerLeaves(this, this.isRed.get(p));
            }
            if (this.isRed.get(p)) {
                this.redPlayers = this.reduceArray(this.redPlayers, p);
            }
            else {
                this.bluePlayers = this.reduceArray(this.bluePlayers, p);
            }
            this.isRed.remove(p);
        }
    }

    private void prematch() {
        Component title = Component.text("q").color(NamedTextColor.AQUA).append(Component.text("Footcube").color(NamedTextColor.DARK_AQUA));
        Component blue = Component.text("  ")
                .color(NamedTextColor.DARK_GRAY)
                .append(
                        Component.text("Blue Team:")
                                .color(NamedTextColor.WHITE)
                );

        Component red = Component.text("  ")
                .color(NamedTextColor.DARK_GRAY)
                .append(
                        Component.text("Red Team:")
                                .color(NamedTextColor.WHITE)
                );

        Component arena = Component.text(" ▪ ")
                .color(NamedTextColor.DARK_GRAY)
                .append(
                        Component.text("Arena: ")
                                .color(NamedTextColor.WHITE)
                ).append(
                        Component.text(this.type+"v"+this.type+"spec"+((int)this.ar+1))
                                .color(NamedTextColor.GREEN)
                );
        this.pSidebar.title(title);

        this.pSidebar.line(0, Component.empty());
        this.pSidebar.line(1, arena);
        this.pSidebar.line(2, Component.empty());
        this.pSidebar.line(3, blue);
        int pindex=3;
        for(Player p : this.isRed.keySet()) {
            if(!this.isRed.get(p)) {
                pindex++;
                this.pSidebar.line(pindex, Component.text(" ▪ ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(
                                Component.text(p.getName())
                                        .color(NamedTextColor.AQUA)
                        ));
            }
        }
        pindex++;
        this.pSidebar.line(pindex, Component.empty());
        pindex++;
        this.pSidebar.line(pindex, red);
        for(Player p : this.isRed.keySet()) {
            if (this.isRed.get(p)) {
                pindex++;
                this.pSidebar.line(pindex, Component.text(" ▪ ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(
                                Component.text(p.getName())
                                        .color(NamedTextColor.RED)
                        ));
            }
        }
        pindex++;
        this.pSidebar.line(pindex, Component.empty());
        for(Player p : this.isRed.keySet())
            this.pSidebar.addPlayer(p);
    }

    private ComponentSidebarLayout layout() {
        Component title = Component.text("q").color(NamedTextColor.AQUA).append(Component.text("Footcube").color(NamedTextColor.DARK_AQUA));
        Component arena = Component.text(" ▪ ")
                .color(NamedTextColor.DARK_GRAY)
                .append(
                        Component.text("Arena: ")
                                .color(NamedTextColor.WHITE)
                ).append(
                        Component.text(this.type+"v"+this.type+"spec"+((int)this.ar+1))
                                .color(NamedTextColor.GREEN)
                );

        SidebarComponent lines = SidebarComponent.builder()
                .addComponent(SidebarComponent.staticLine(Component.empty()))
                .addStaticLine(arena)
                .addComponent(SidebarComponent.staticLine(Component.empty()))
                .addDynamicLine(()-> {
                    return Component.text("   " +this.blueGoals.getScore() + " Blue")
                            .color(NamedTextColor.AQUA)
                            .append(
                                    Component.text(" - ")
                                            .color(NamedTextColor.GRAY)
                            ).append(
                                    Component.text("Red " + this.redGoals.getScore())
                                            .color(NamedTextColor.RED)
                            );
                })
                .addComponent(SidebarComponent.staticLine(Component.empty()))
                .addDynamicLine(()-> {
                    return Component.text(" ▪ ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(
                                    Component.text("Time left: ")
                                            .color(NamedTextColor.WHITE)
                            ).append(
                                    Component.text(this.time.getScore()+"")
                                            .color(NamedTextColor.GREEN)
                            );
                })
                .addComponent(SidebarComponent.staticLine(Component.empty()))
                .build();
        return new ComponentSidebarLayout(SidebarComponent.staticLine(title), lines);
    }
    private void ingame() {
        layout().apply(this.igSidebar);
    }

    private void removeSB() {
        this.igSidebar.removePlayers(this.isRed.keySet());
        this.igSidebar.removePlayers(this.takePlace);
    }
    public boolean equals(final Match m) {
        return m.matchID == this.matchID;
    }
    
    private ItemStack createColoredArmour(final Material material, final Color color) {
        final ItemStack is = new ItemStack(material);
        if (is.getItemMeta() instanceof LeatherArmorMeta) {
            final LeatherArmorMeta meta = (LeatherArmorMeta)is.getItemMeta();
            meta.setColor(color);
            is.setItemMeta((ItemMeta)meta);
        }
        return is;
    }
    
    private Player[] extendArray(final Player[] oldL, final Player add) {
        final Player[] newL = new Player[oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }
    
    private Player[] reduceArray(final Player[] oldL, final Player remove) {
        final Player[] newL = new Player[oldL.length - 1];
        int i = 0;
        int j = 0;
        while (i < newL.length) {
            if (oldL[i] != remove) {
                newL[j] = oldL[i];
                ++j;
            }
            ++i;
        }
        return newL;
    }
    
    public void join(final Player p, final boolean b, final boolean r) {
        final String uuid = this.organization.uuidConverter.getKey(p.getName());
        if (!this.organization.matches.has(p.getUniqueId().toString())) {
            this.organization.matches.put(p.getUniqueId().toString(), 0);
            this.organization.wins.put(p.getUniqueId().toString(), 0);
            this.organization.ties.put(p.getUniqueId().toString(), 0);
            this.organization.goals.put(p.getUniqueId().toString(), 0);
            this.organization.assists.put(p.getUniqueId().toString(), 0);
        }
        if (this.redPlayers.length < this.type && !b) {
            this.redPlayers = this.extendArray(this.redPlayers, p);
            this.isRed.put(p, true);
            p.teleport(this.red);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinRedTeam")));
        }
        else {
            this.bluePlayers = this.extendArray(this.bluePlayers, p);
            this.isRed.put(p, false);
            p.teleport(this.blue);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinBlueTeam")));
        }
        if (this.bluePlayers.length >= this.type && this.redPlayers.length >= this.type) {
            this.phase = 2;
            this.countdown = 10;
            this.tickToSec = 20;
            this.organization.matchStart(this.type);
            for (final Player player : this.isRed.keySet()) {
                player.setLevel(10);
                if (this.type != 1) {
                    this.organization.matches.rise(player.getUniqueId().toString());
                }
                if (this.isRed.get(player)) {
                    player.getInventory().setChestplate(this.redChestPlate);
                    player.getInventory().setLeggings(this.redLeggings);
                }
                else {
                    player.getInventory().setChestplate(this.blueChestPlate);
                    player.getInventory().setLeggings(this.blueLeggings);
                }
                if (r) {
                    player.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("matchStart")));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tip")));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamchat")));
                }
            }
        }
        else {
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("leaveMatch")));
        }
    }
    
    public void leave(final Player p) {
        if (this.isRed.get(p)) {
            this.redPlayers = this.reduceArray(this.redPlayers, p);
        }
        else {
            this.bluePlayers = this.reduceArray(this.bluePlayers, p);
        }
        this.isRed.remove(p);
        p.teleport(p.getWorld().getSpawnLocation());
    }
    
    public void takePlace(final Player p) {
        this.takePlace.add(p);
        if (this.redPlayers.length < this.type) {
            this.redPlayers = this.extendArray(this.redPlayers, p);
            this.isRed.put(p, true);
            p.teleport(this.red);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinRedTeam")));
        }
        else {
            this.bluePlayers = this.extendArray(this.bluePlayers, p);
            this.isRed.put(p, false);
            p.teleport(this.blue);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinBlueTeam")));
        }
        if (this.isRed.get(p)) {
            p.getInventory().setChestplate(this.redChestPlate);
            p.getInventory().setLeggings(this.redLeggings);
        }
        else {
            p.getInventory().setChestplate(this.blueChestPlate);
            p.getInventory().setLeggings(this.blueLeggings);
        }
        if (this.phase > 2) {
            this.igSidebar.addPlayer(p);
        }
    }
    
    public void kick(final Player p) {
        if (this.isRed.containsKey(p)) {
            if (this.isRed.get(p)) {
                this.lastKickRed = p;
            }
            else {
                this.lastKickBlue = p;
            }
        }
    }

    public void addForfeit(final Player p) {
        if (this.isRed.containsKey(p) && this.isRed.get(p) && !this.forfeitRed.contains(p)) {
            this.forfeitRed.add(p);
            if (this.forfeitRed.size() < this.type) {
                for (final Player p1 : this.isRed.keySet()) {
                    if(this.isRed.get(p1))
                        p1.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("ff").replace("{a}", ""+this.forfeitRed.size()).replace("{m}", ""+this.type)));
                }
            }
        }
        if (this.isRed.containsKey(p) && !this.isRed.get(p) && !this.forfeitBlue.contains(p)){
            this.forfeitBlue.add(p);
            if (this.forfeitBlue.size() < this.type) {
                for (final Player p1 : this.isRed.keySet()) {
                    if(!this.isRed.get(p1))
                        p1.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("ff").replace("{a}", ""+this.forfeitBlue.size()).replace("{m}", ""+this.type)));
                }
            }
        }
    }
    public void tackle(final Player p) {
        if (this.isRed.containsKey(p)) {
            if (this.isRed.get(p)) {
                this.blueassist.add(Bukkit.getPlayer("nobody"));
            }
            else {
                this.redassist.add(Bukkit.getPlayer("nobody"));
            }
        }
    }

    public void assist(final Player p) {
        if (this.isRed.containsKey(p)) {
            if (this.isRed.get(p)) {
                this.redassist.add(p);
            }
            else {
                this.blueassist.add(p);
            }
        }
    }

    public void teamchat(final Player p, final String message) {
        if (this.isRed.containsKey(p)) {
            for(Player p1 : this.isRed.keySet()) {
                if (this.isRed.get(p1) && this.isRed.get(p)) {
                    p1.sendMessage((ChatColor.RED + "TC " + ChatColor.AQUA + p.getName() + ChatColor.RED + " " + ChatColor.DARK_AQUA + message));
                }
                if (!this.isRed.get(p1) && !this.isRed.get(p)) {
                    p1.sendMessage(ChatColor.BLUE + "TC " + ChatColor.AQUA + p.getName() + ChatColor.BLUE + " " + ChatColor.DARK_AQUA + message);
                }
            }
        }
    }

    public boolean team(final Player p0, final Player p1) {
        if (this.redPlayers.length + this.bluePlayers.length > 2 * this.type - 2 || (this.teams >= 2 && this.type == 3)) {
            return false;
        }
        p0.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamWith").replace("{player}", p1.getName())));
        p1.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamWith").replace("{player}", p0.getName())));
        this.teamers.add(p0);
        this.teamers.add(p1);
        ++this.teams;
        if (this.type - this.redPlayers.length >= 2) {
            this.join(p0, false, false);
            this.join(p1, false, false);
        }
        else if (this.type - this.bluePlayers.length >= 2) {
            this.join(p0, true, false);
            this.join(p1, true, false);
        }
        else {
            boolean rare = true;
            Player[] bluePlayers;
            for (int length = (bluePlayers = this.bluePlayers).length, i = 0; i < length; ++i) {
                final Player p2 = bluePlayers[i];
                if (!this.teamers.contains(p2)) {
                    this.bluePlayers = this.reduceArray(this.bluePlayers, p2);
                    this.redPlayers = this.extendArray(this.redPlayers, p2);
                    this.isRed.put(p2, true);
                    p2.teleport(this.red);
                    p2.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("changeTeamWith").replace("{player1}", p0.getName()).replace("{player2}", p1.getName())));
                    this.join(p0, true, false);
                    this.join(p1, true, false);
                    rare = false;
                    break;
                }
            }
            if (rare) {
                Player[] redPlayers;
                for (int length2 = (redPlayers = this.redPlayers).length, j = 0; j < length2; ++j) {
                    final Player p3 = redPlayers[j];
                    if (!this.teamers.contains(p3)) {
                        this.redPlayers = this.reduceArray(this.redPlayers, p3);
                        this.bluePlayers = this.extendArray(this.bluePlayers, p3);
                        this.isRed.put(p3, true);
                        p3.teleport(this.blue);
                        p3.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("changeTeamWith").replace("{player1}", p0.getName()).replace("{player2}", p1.getName())));
                        this.join(p0, false, false);
                        this.join(p1, false, false);
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void ff(int type) {
        if(this.forfeitBlue.size() == type) {
            for (final Player p : this.isRed.keySet()) {
                final String uuid = p.getUniqueId().toString();
                this.organization.endMatch(p);
                FileConfiguration cfg = this.plugin.getConfig();
                Double x = cfg.getDouble("afterMatchRespawn.1");
                Double y = cfg.getDouble("afterMatchRespawn.2");
                Double z = cfg.getDouble("afterMatchRespawn.3");
                removeSB();
                if (x == 0.0 && y == 0.0 && z == 0.0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                } else {
                    Location loc = new Location(p.getWorld(), x, y, z);
                    p.teleport(loc);
                }

                this.organization.clearInventory(p);
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("timesUpRedFF")));
                if (this.type != 1) {
                    if (this.isRed.get(p) && !this.takePlace.contains(p)) {
                        this.organization.wins.rise(uuid);
                        this.organization.winStreak.rise(uuid);
                        if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                            this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
                        }
                        this.organization.economy.depositPlayer(p.getName(), 200.0);
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward1")));
                        if (this.organization.winStreak.get(uuid) % 5 != 0) {
                            continue;
                        }
                        this.organization.economy.depositPlayer(p.getName(), 100.0);
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("bonus") + this.organization.winStreak.get(uuid) + MessagesConfig.get().getString("bonus1")));
                    } else {
                        this.organization.winStreak.put(uuid.toString(), 0);
                    }
                } else {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("1v1noStats")));
                }
                this.phase = 1;
                this.cube.setHealth(0.0);
                this.organization.undoTakePlace(this);
                this.scoreRed = 0;
                this.pgclosed=false;
                this.pgset=false;
                this.scoreBlue = 0;
                this.teams = 0;
                this.redPlayers = new Player[0];
                this.bluePlayers = new Player[0];
                this.teamers = new ArrayList<Player>();
                this.isRed = new HashMap<Player, Boolean>();
                this.takePlace.clear();
                this.redassist.clear();
                this.blueassist.clear();
                this.goals.clear();
                this.assists.clear();
                this.forfeitRed.clear();
                this.overtimeEnd=1;
                this.forfeitBlue.clear();
                this._countdown=0;
            }
        } else if(this.forfeitRed.size() == type) {
            for (final Player p : this.isRed.keySet()) {
                final String uuid = p.getUniqueId().toString();
                this.organization.endMatch(p);
                FileConfiguration cfg = this.plugin.getConfig();
                Double x = cfg.getDouble("afterMatchRespawn.1");
                Double y = cfg.getDouble("afterMatchRespawn.2");
                Double z = cfg.getDouble("afterMatchRespawn.3");
                removeSB();
                if(x==0.0 && y==0.0 && z==0.0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                } else {
                    Location loc = new Location(p.getWorld(), x, y, z);
                    p.teleport(loc);
                }

                this.organization.clearInventory(p);
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("timesUpBlueFF")));
                if (this.type != 1) {
                    if (!this.isRed.get(p) && !this.takePlace.contains(p)) {
                        this.organization.wins.rise(uuid.toString());
                        this.organization.winStreak.rise(uuid.toString());
                        if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                            this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
                        }
                        this.organization.economy.depositPlayer(p.getName(), 15.0);
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward2")));
                        if (this.organization.winStreak.get(uuid) % 5 != 0) {
                            continue;
                        }
                        this.organization.economy.depositPlayer(p.getName(), 100.0);
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("bonus") + this.organization.winStreak.get(uuid) + MessagesConfig.get().getString("bonus1")));
                    }
                    else {
                        this.organization.winStreak.put(uuid, 0);
                    }
                }
                else {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("1v1noStats")));
                }
            }
            this.phase = 1;
            this.cube.setHealth(0.0);
            this.pgclosed=false;
            this.pgset=false;
            this.organization.undoTakePlace(this);
            this.scoreRed = 0;
            this.scoreBlue = 0;
            this.teams = 0;
            this.redPlayers = new Player[0];
            this.bluePlayers = new Player[0];
            this.teamers = new ArrayList<Player>();
            this.isRed = new HashMap<Player, Boolean>();
            this.takePlace.clear();
            this.redassist.clear();
            this.blueassist.clear();
            this.goals.clear();
            this.assists.clear();
            this.forfeitRed.clear();
            this.overtimeEnd=1;
            this.forfeitBlue.clear();
            this._countdown=0;
        }
    }
    public void update() {
        --this.tickToSec;
        if (this.phase == 3) {
            final Location l = this.cube.getLocation();
            if (this.x) {
                if (((this.redAboveBlue && l.getBlockX() >= this.red.getBlockX()) || (!this.redAboveBlue && this.red.getBlockX() >= l.getBlockX())) && l.getY() < this.red.getY() + 3.0 && l.getZ() < this.red.getZ() + 4.0 && l.getZ() > this.red.getZ() - 4.0) {
                    this.score(false);
                }
                else if (((this.redAboveBlue && l.getBlockX() <= this.blue.getBlockX()) || (!this.redAboveBlue && this.blue.getBlockX() <= l.getBlockX())) && l.getY() < this.blue.getY() + 3.0 && l.getZ() < this.blue.getZ() + 4.0 && l.getZ() > this.blue.getZ() - 4.0) {
                    this.score(true);
                }
            }
            else if (((this.redAboveBlue && l.getBlockZ() >= this.red.getBlockZ()) || (!this.redAboveBlue && this.red.getBlockZ() >= l.getBlockZ())) && l.getY() < this.red.getY() + 3.0 && l.getX() < this.red.getX() + 4.0 && l.getX() > this.red.getX() - 4.0) {
                this.score(false);
            }
            else if (((this.redAboveBlue && l.getBlockZ() <= this.blue.getBlockZ()) || (!this.redAboveBlue && this.blue.getBlockZ() <= l.getBlockZ())) && l.getY() < this.blue.getY() + 3.0 && l.getX() < this.blue.getX() + 4.0 && l.getX() > this.blue.getX() - 4.0) {
                this.score(true);
            }
        }
        if ((this.phase == 2 || this.phase == 4) && this.tickToSec == 0) {
            --this.countdown;
            this.tickToSec = 20;
            for (final Player p : this.isRed.keySet()) {
                p.setLevel(this.countdown);
                this.ar = this.organization.findArena(p);
            }
            if(!pgset) {
                prematch();
                pgset=true;
            }
            if (this.countdown <= 0) {
                String message;
                if (this.phase == 2) {
                    if(!pgclosed) {
                        this.pSidebar.removePlayers(this.isRed.keySet());
                        this.pSidebar.clearLines();
                        this.time.setScore(180);
                        pgclosed=true;
                    }
                    message = this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("startingMatch"));
                    this.startTime = System.currentTimeMillis();
                    this.redGoals.setScore(0);
                    this.blueGoals.setScore(0);
                    this.blueassist.add(Bukkit.getPlayer("nobody"));
                    this.redassist.add(Bukkit.getPlayer("nobody"));
                    for (final Player p2 : this.isRed.keySet()) {
                        this.organization.playerStarts(p2);
                        ingame();
                        if(!this.igSidebar.players().contains(p2))
                            this.igSidebar.addPlayer(p2);
                    }
                }
                else {
                    message = this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("continueMatch"));
                }
                this.phase = 3;
                this.cube = this.plugin.spawnCube(this.mid);
                final Random random = new Random();
                final double vertical = 0.3 * random.nextDouble() + 0.2;
                double horizontal = 0.3 * random.nextDouble() + 0.3;
                if (random.nextBoolean()) {
                    horizontal *= -1.0;
                }
                if (this.x) {
                    this.cube.setVelocity(new Vector(0.0, vertical, horizontal));
                }
                else {
                    this.cube.setVelocity(new Vector(horizontal, vertical, 0.0));
                }
                for (final Player p3 : this.isRed.keySet()) {
                    p3.sendMessage(message);
                    if (this.isRed.get(p3)) {
                        p3.teleport(this.red);
                    }
                    else {
                        p3.teleport(this.blue);
                    }
                    p3.playSound(p3.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                }
            }
            else if (this.countdown <= 3) {
                for (final Player p : this.isRed.keySet()) {
                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
                }
            }
        }
        if(this.phase != 2 && this.phase != 4) {
            this.time.setScore((180 + this.overtimeCount + this._countdown - (int) (System.currentTimeMillis() - this.startTime) / 1000)*this.overtimeEnd);
            layout().apply(this.igSidebar);
            if (this.type == 1) {
                ff(1);
            } else if (this.type == 2) {
                ff(2);
            } else if (this.type == 3) {
                ff(3);
            } else if (this.type == 4) {
                ff(4);
            }
            if (this.time.getScore() <= 0 && this.phase > 2) {
                if(!this.overtime && this.scoreRed == this.scoreBlue) {
                    this.phase = 4;
                    this.tickToSec = 20;
                    this.countdown = 5;
                    this._countdown += 5;
                    this.cube.setHealth(0.0);
                    this.plugin.cubes.remove(this.cube);
                    this.overtime=true;
                    this.overtimeEnd=1;
                    this.overtimeCount=60;
                    for (final Player p : this.isRed.keySet()) {
                        if (this.isRed.get(p)) {
                            p.teleport(this.red);
                        } else {
                            p.teleport(this.blue);
                        }
                        p.sendTitle(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("overtimeTitle")), ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("overtimeMessage")));
                    }
                } else {
                    for (final Player p : this.isRed.keySet()) {
                        final String uuid = p.getUniqueId().toString();
                        this.organization.endMatch(p);
                        FileConfiguration cfg = this.plugin.getConfig();
                        Double x = cfg.getDouble("afterMatchRespawn.1");
                        Double y = cfg.getDouble("afterMatchRespawn.2");
                        Double z = cfg.getDouble("afterMatchRespawn.3");
                        removeSB();
                        if (x == 0.0 && y == 0.0 && z == 0.0) {
                            p.teleport(p.getWorld().getSpawnLocation());
                        } else {
                            Location loc = new Location(p.getWorld(), x, y, z);
                            p.teleport(loc);
                        }

                        this.organization.clearInventory(p);
                        if (this.scoreRed > this.scoreBlue) {
                            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("timesUpRed").replace("{red}", "" + this.scoreRed).replace("{blue}", "" + this.scoreBlue)));
                            if (this.type != 1) {
                                if (this.isRed.get(p) && !this.takePlace.contains(p)) {
                                    this.organization.wins.rise(uuid);
                                    this.organization.winStreak.rise(uuid);
                                    if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                                        this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
                                    }
                                    this.organization.economy.depositPlayer(p.getName(), 200.0);
                                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward1")));
                                    if (this.organization.winStreak.get(uuid) % 5 != 0) {
                                        continue;
                                    }
                                    this.organization.economy.depositPlayer(p.getName(), 100.0);
                                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("bonus") + this.organization.winStreak.get(uuid) + MessagesConfig.get().getString("bonus1")));
                                } else {
                                    this.organization.winStreak.put(uuid.toString(), 0);
                                }
                            } else {
                                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("1v1noStats")));
                            }
                        } else if (this.scoreRed < this.scoreBlue) {
                            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("timesUpBlue").replace("{blue}", "" + this.scoreBlue).replace("{red}", "" + this.scoreRed)));
                            if (this.type != 1) {
                                if (!this.isRed.get(p) && !this.takePlace.contains(p)) {
                                    this.organization.wins.rise(uuid.toString());
                                    this.organization.winStreak.rise(uuid.toString());
                                    if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                                        this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
                                    }
                                    this.organization.economy.depositPlayer(p.getName(), 15.0);
                                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward2")));
                                    if (this.organization.winStreak.get(uuid) % 5 != 0) {
                                        continue;
                                    }
                                    this.organization.economy.depositPlayer(p.getName(), 100.0);
                                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("bonus") + this.organization.winStreak.get(uuid) + MessagesConfig.get().getString("bonus1")));
                                } else {
                                    this.organization.winStreak.put(uuid, 0);
                                }
                            } else {
                                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("1v1noStats")));
                            }
                        } else {
                            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tied")));
                            if (this.takePlace.contains(p)) {
                                continue;
                            }
                            if (this.type == 1) {
                                continue;
                            }
                            this.organization.ties.rise(uuid);
                            this.organization.winStreak.put(uuid, 0);
                            this.organization.economy.depositPlayer(p.getName(), 100.0);
                            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward3")));
                        }
                    }
                    this.phase = 1;
                    this.cube.setHealth(0.0);
                    this.pgclosed = false;
                    this.pgset = false;
                    this.organization.undoTakePlace(this);
                    this.scoreRed = 0;
                    this.scoreBlue = 0;
                    this.teams = 0;
                    this.overtimeCount = 0;
                    this.overtime = false;
                    this.redPlayers = new Player[0];
                    this.bluePlayers = new Player[0];
                    this.teamers = new ArrayList<Player>();
                    this.isRed = new HashMap<Player, Boolean>();
                    this.takePlace.clear();
                    this.redassist.clear();
                    this.blueassist.clear();
                    this.goals.clear();
                    this.assists.clear();
                    this.forfeitRed.clear();
                    this.overtimeEnd=1;
                    this.forfeitBlue.clear();
                    this._countdown = 0;
                }
            }
        }
    }
    
    private void score(final boolean red) {
        this.phase = 4;
        this.tickToSec = 20;
        this.countdown = 5;
        this._countdown += 5;
        this.cube.setHealth(0.0);
        this.plugin.cubes.remove(this.cube);
        Player scorer = null;
        Player assister = null;
        String team = null;
        if(this.overtime)
            this.overtimeEnd=0;
        if (red) {
            if(this.lastKickRed != null)
                scorer = this.lastKickRed;
            else
                scorer = this.lastKickBlue;
            if(this.type!=1)
                assister = this.redassist.get(1);
            team = "red";
            ++this.scoreRed;
            this.redGoals.setScore(this.redGoals.getScore() + 1);
            if(this.type!=1) {
                this.blueassist.clear();
                this.redassist.clear();
                this.blueassist.add(Bukkit.getPlayer("nobody"));
                this.redassist.add(Bukkit.getPlayer("nobody"));
            }
        }
        else {
            if(this.lastKickBlue != null)
                scorer = this.lastKickBlue;
            else
                scorer = this.lastKickRed;
            if(this.type!=1)
                assister = this.blueassist.get(1);
            team = "blue";
            ++this.scoreBlue;
            this.blueGoals.setScore(this.blueGoals.getScore() + 1);
            if(this.type!=1) {
                this.blueassist.clear();
                this.redassist.clear();
                this.blueassist.add(Bukkit.getPlayer("nobody"));
                this.redassist.add(Bukkit.getPlayer("nobody"));
            }
        }
        if (!this.takePlace.contains(scorer) && this.type != 1) {
            this.organization.goals.rise(scorer.getUniqueId().toString());
            if (scorer.hasPermission("2v2")) {
                this.organization.economy.depositPlayer(scorer.getName(), 100.0);
            }
            if (this.goals.containsKey(scorer)) {
                this.goals.put(scorer, this.goals.get(scorer) + 1);
            }
            else {
                this.goals.put(scorer, 1);
            }
            scorer.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward4")));
            if (this.goals.get(scorer) == 3) {
                scorer.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward5")));
                for (final Player p : this.isRed.keySet()) {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("hatty").replace("{player}", scorer.getName())));
                }
                this.organization.economy.depositPlayer(scorer.getName(), 200.0);
            }
        }
        if (!this.takePlace.contains(assister) && scorer != assister && assister != null) {
            this.organization.assists.rise(assister.getUniqueId().toString());
            this.organization.economy.depositPlayer(assister.getName(), 50.0);
            if (this.assists.containsKey(assister)) {
                this.assists.put(assister, this.assists.get(assister) + 1);
            }
            else {
                this.assists.put(assister, 1);
            }
            assister.sendMessage(String.valueOf(String.valueOf(String.valueOf(this.organization.pluginString))) + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("assistReward")));
        }

        FireworkEffect.Builder builder = FireworkEffect.builder();
        UUID id = scorer.getUniqueId();
        final File userFile = new File("plugins" + File.separator + "qFootcube" + File.separator + "/users/" + id + ".yml");
        final FileConfiguration pcfg = (FileConfiguration) YamlConfiguration.loadConfiguration(userFile);

        if(team.equalsIgnoreCase("red")) {
            Location blueish = this.blue.clone();
            double yaw = blueish.getYaw();
            if(yaw > 45 && yaw <135)
                blueish.setX(blueish.getX()-3);
            else if(yaw > 135 && yaw<225)
                blueish.setZ(blueish.getZ()-3);
            else if(yaw > 235 && yaw < 325)
                blueish.setX(blueish.getX()+3);
            else
                blueish.setZ(blueish.getZ()+3);
            try {
                (new GoalExplosion()).init(blueish, pcfg.getString("explosion"), 1, this.plugin);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Location redish = this.red.clone();
            double yaw = redish.getYaw();
            if(yaw > 45 && yaw <135)
                redish.setX(redish.getX()-3);
            else if(yaw > 135 && yaw<225)
                redish.setZ(redish.getZ()-3);
            else if(yaw > 235 && yaw < 325)
                redish.setX(redish.getX()+3);
            else
                redish.setZ(redish.getZ()+3);
            try {
                (new GoalExplosion()).init(redish, pcfg.getString("explosion"), 0, this.plugin);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (final Player p : this.isRed.keySet()) {
            final String uuid = p.getUniqueId().toString();
            String tgoal = "O";
            new titleThread(p, scorer.getName()).start();
            final double number = this.cube.getLocation().distance(scorer.getLocation());
            Math.round(number);
            if (assister != scorer && assister != null) {
                if (number > 20.0) {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredIncAss").replace("{player}", scorer.getName()).replace("{assist}", ""+assister.getName()).replace("{distance}", "" + Math.round(number))));
                } else {
                    System.out.println("Assist: "+assister.getName());
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredNormAss").replace("{player}", scorer.getName()).replace("{assist}", ""+assister.getName()).replace("{distance}", "" + Math.round(number))));
                }
            } else {
                if (number > 20.0) {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredInc").replace("{player}", scorer.getName()).replace("{distance}", "" + Math.round(number))));
                } else {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredNorm").replace("{player}", scorer.getName()).replace("{distance}", "" + Math.round(number))));
                }
            }
            Location gl;
            World world = this.blue.getWorld();
            if (red)
                gl = this.red;
            else
                gl = this.blue;
            p.setVelocity(explosionVector(p, gl, 3));
            for (Entity nearby: world.getNearbyEntities(gl, 6, 2, 6)) {
                if (nearby instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) nearby;
                    entity.damage(0);
                }
            }
            if (this.time.getScore() <= 0) {
                this.organization.endMatch(p);
                FileConfiguration cfg = this.plugin.getConfig();
                Double x = cfg.getDouble("afterMatchRespawn.1");
                Double y = cfg.getDouble("afterMatchRespawn.2");
                Double z = cfg.getDouble("afterMatchRespawn.3");
                removeSB();
                if (this.isRed.get(p) == red && !this.takePlace.contains(p) && this.type != 1) {
                    this.organization.wins.rise(uuid);
                    this.organization.winStreak.rise(uuid);
                    if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                        this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
                    }
                    this.organization.economy.depositPlayer(p.getName(), 100.0);
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reward2")));
                    if (this.organization.winStreak.get(uuid) % 5 == 0) {
                        this.organization.economy.depositPlayer(p.getName(), 100.0);
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("bonus").replace("{wins}", ""+this.organization.winStreak.get(uuid))));
                    }
                }
                else if (!this.takePlace.contains(p) && this.type != 1) {
                    this.organization.winStreak.put(uuid, 0);
                }
                if(x==0.0 && y==0.0 && z==0.0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                } else {
                    Location loc = new Location(p.getWorld(), x, y, z);
                    p.teleport(loc);
                }
                this.organization.clearInventory(p);
            }
            else {
                p.setLevel(10);
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("result").replace("{red}", ""+this.scoreRed).replace("{blue}", ""+this.scoreBlue)));
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("continue5sec")));
            }
        }
    }
}

@SuppressWarnings("ALL")
class titleThread extends Thread {
    private Player p;
    private String scorer;
    public titleThread(Player p, String scorer) {
        this.p = p;
        this.scorer=scorer;
    }
    public void run() {
        String tgoal = "O";
        for(int j=0; j<7; j++) {
            this.p.sendTitle(ChatColor.translateAlternateColorCodes('&',"&lG"+tgoal+"AL!"), ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoreGoal").replace("{player}", this.scorer)));
            tgoal = tgoal.concat("O");
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.suspend();
    }
}
