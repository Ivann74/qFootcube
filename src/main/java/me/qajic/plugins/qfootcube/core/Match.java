package me.qajic.plugins.qfootcube.core;

import com.connorlinfoot.titleapi.TitleAPI;
import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.utils.GoalExplosion;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Match implements Listener {
    public int matchID;
    public int type;
    public int phase;
    private int countdown;
    private int _countdown;
    public int tickToSec;
    private int teams;
    private final boolean x;
    public int ticksLived;
    private long startTime;
    private boolean prematchSidebarSet;
    private int arena;
    private boolean redAboveBlue;
    public int scoreRed;
    public int scoreBlue;
    public int time;
    private int redGoals;
    private int blueGoals;
    private Location blueLocation;
    private Location redLocation;
    private Location middleLocation;
    private Organization organization;
    private Footcube plugin;
    private Player[] redPlayers;
    private Player[] bluePlayers;
    public ArrayList<Player> redAssist;
    public ArrayList<Player> blueAssist;
    private ArrayList<Player> teamers;
    private ArrayList<Player> takePlace;
    public HashMap<Player, Boolean> isRed;
    private Player lastKick;
    private Player lastKickRed;
    private Player lastKickBlue;
    private HashMap<Player, Integer> goals;
    private HashMap<Player, Integer> assists;
    private ItemStack redChestPlate;
    private ItemStack redLeggings;
    private ItemStack blueChestPlate;
    private ItemStack blueLeggings;
    private ScoreboardManager sbm;
    private Slime cube;
    private Sidebar sidebar;

    public Match(final Organization org, final Footcube pl, final int type, final Location blueLocation, final Location redLocation, final Location middleLocation, final int id) {
        this.redPlayers = new Player[0];
        this.bluePlayers = new Player[0];
        this.redAssist = new ArrayList<Player>();
        this.blueAssist = new ArrayList<Player>();
        this.assists = new HashMap<Player, Integer>();
        this.teamers = new ArrayList<Player>();
        this.takePlace = new ArrayList<Player>();
        this.isRed = new HashMap<Player, Boolean>();
        this.lastKick = null;
        this.lastKickRed = null;
        this.lastKickBlue = null;
        this.goals = new HashMap<Player, Integer>();
        this.matchID = id;
        this.organization = org;
        this._countdown = 0;
        this.plugin = pl;
        this.prematchSidebarSet = false;
        this.type = type;
        this.sidebar = this.plugin.scoreboardLibrary.createSidebar();
        this.blueLocation = blueLocation;
        this.redLocation = redLocation;
        this.middleLocation = middleLocation;
        this.ticksLived = 0;
        this.phase = 1;
        this.scoreRed = 0;
        this.scoreBlue = 0;
        this.startTime = 0L;
        this.redChestPlate = this.createColoredArmour(Material.LEATHER_CHESTPLATE, Color.RED);
        this.redLeggings = this.createColoredArmour(Material.LEATHER_LEGGINGS, Color.RED);
        this.blueChestPlate = this.createColoredArmour(Material.LEATHER_CHESTPLATE, Color.BLUE);
        this.blueLeggings = this.createColoredArmour(Material.LEATHER_LEGGINGS, Color.BLUE);
        this.sbm = Bukkit.getScoreboardManager();
        this.arena = 0;
        this.time = 0;
        this.redGoals = 0;
        this.blueGoals = 0;
        this.x = (Math.abs(this.blueLocation.getX() - this.redLocation.getX()) > Math.abs(this.blueLocation.getZ() - this.redLocation.getZ()));
        if (this.x) {
            if (this.redLocation.getX() > this.blueLocation.getX()) {
                this.redAboveBlue = true;
            } else {
                this.redAboveBlue = false;
            }
        } else if (this.redLocation.getZ() > this.blueLocation.getZ()) {
            this.redAboveBlue = true;
        } else {
            this.redAboveBlue = false;
        }
        this.plugin.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.plugin);
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

    private void enableSidebar(String type) {
        if (type.equalsIgnoreCase("ingame")) {
            this.ingameLayout().apply(this.sidebar);
        } else if (type.equalsIgnoreCase("prematch")) {
            this.prematchLayout().apply(this.sidebar);
        }
        for (Player p : this.isRed.keySet()) {
            if (!this.sidebar.players().contains(p))
                this.sidebar.addPlayer(p);
        }
    }

    private ComponentSidebarLayout prematchLayout() {
        Configuration cfg = this.plugin.getConfig();
        Component title = Component.text("FOOTBALL").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true);
        String name = cfg.getString("arenas." + this.type + "v" + this.type + "." + (this.arena + 1) + ".name");
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
                Component.text(name + " [" + ((int) this.arena + 1) + "]")
                    .color(NamedTextColor.GREEN)
            );

        SidebarComponent.Builder lines = SidebarComponent.builder()
            .addComponent(SidebarComponent.staticLine(Component.empty()))
            .addComponent(SidebarComponent.staticLine(arena))
            .addComponent(SidebarComponent.staticLine(Component.empty()));

        if (this.type != 5) {
            lines.addComponent(SidebarComponent.staticLine(blue));
            for (Player p : this.isRed.keySet()) {
                if (!this.isRed.get(p)) {
                    lines.addComponent(SidebarComponent.staticLine(
                        Component.text(" ▪ ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(
                                Component.text(p.getName())
                                    .color(NamedTextColor.AQUA)
                            )
                    ));
                }
            }
            lines.addComponent(SidebarComponent.staticLine(Component.empty()));
            lines.addComponent(SidebarComponent.staticLine(red));
            for (Player p : this.isRed.keySet()) {
                if (this.isRed.get(p)) {
                    lines.addComponent(SidebarComponent.staticLine(
                        Component.text(" ▪ ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(
                                Component.text(p.getName())
                                    .color(NamedTextColor.RED)
                            )
                    ));
                }
            }
            lines.addComponent(SidebarComponent.staticLine(Component.empty()));
        }
        SidebarComponent finalLines = lines.build();
        return new ComponentSidebarLayout(SidebarComponent.staticLine(title), finalLines);
    }

    private ComponentSidebarLayout ingameLayout() {
        Configuration cfg = this.plugin.getConfig();
        String name = cfg.getString("arenas." + this.type + "v" + this.type + "." + (this.arena + 1) + ".name");
        Component title = Component.text("FOOTBALL").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true);
        Component arena = Component.text(" ▪ ")
            .color(NamedTextColor.DARK_GRAY)
            .append(
                Component.text("Arena: ")
                    .color(NamedTextColor.WHITE)
            ).append(
                Component.text(name + " [" + ((int) this.arena + 1) + "]")
                    .color(NamedTextColor.GREEN)
            );

        SidebarComponent.Builder lines = SidebarComponent.builder()
                .addComponent(SidebarComponent.staticLine(Component.empty()))
                .addStaticLine(arena)
                .addComponent(SidebarComponent.staticLine(Component.empty()))
                .addDynamicLine(() -> Component.text("   " + this.blueGoals + " Blue")
                        .color(NamedTextColor.AQUA)
                        .append(
                                Component.text(" - ")
                                        .color(NamedTextColor.GRAY)
                        ).append(
                                Component.text("Red " + this.redGoals)
                                        .color(NamedTextColor.RED)
                        ))
                .addDynamicLine(Component::empty)
                .addDynamicLine(() -> Component.text(" ▪ ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(
                                Component.text("Time left: ")
                                        .color(NamedTextColor.WHITE)
                        ).append(
                                Component.text(this.time + "")
                                        .color(NamedTextColor.GREEN)
                        ))
                .addDynamicLine(Component::empty);

        SidebarComponent finalLines = lines.build();
        return new ComponentSidebarLayout(SidebarComponent.staticLine(title), finalLines);
    }

    private void removeSidebar() {
        if (!this.sidebar.players().isEmpty()) {
            this.sidebar.removePlayers(this.isRed.keySet());
            this.sidebar.removePlayers(this.takePlace);
            this.sidebar.clearLines();
        }
    }

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

    public Vector explosionVector(Player player, Location source, double power) {
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

    public boolean equals(final Match m) {
        return m.matchID == this.matchID;
    }

    private ItemStack createColoredArmour(final Material material, final Color color) {
        final ItemStack itemStack = new ItemStack(material);
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            final LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            meta.setColor(color);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
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


    public void join(final Player p) {
        if (this.redPlayers.length < this.type) {
            this.redPlayers = this.extendArray(this.redPlayers, p);
            this.isRed.put(p, true);
            p.teleport(this.redLocation);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinRedTeam")));
        } else if (this.bluePlayers.length < this.type) {
            this.bluePlayers = this.extendArray(this.bluePlayers, p);
            this.isRed.put(p, false);
            p.teleport(this.blueLocation);
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
                    this.organization.stats.riseStats(player.getName(), "matches");
                }
                if (this.isRed.get(player)) {
                    player.getInventory().setChestplate(this.redChestPlate);
                    player.getInventory().setLeggings(this.redLeggings);
                } else {
                    player.getInventory().setChestplate(this.blueChestPlate);
                    player.getInventory().setLeggings(this.blueLeggings);
                }
                player.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("matchStart")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tip")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamchat")));
            }
        } else {
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("leaveMatch")));
        }
    }

    public void leave(final Player p) {
        if (this.isRed.get(p)) {
            this.redPlayers = this.reduceArray(this.redPlayers, p);
        } else {
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
            p.teleport(this.redLocation);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinRedTeam")));
        } else if (this.bluePlayers.length < this.type) {
            this.bluePlayers = this.extendArray(this.bluePlayers, p);
            this.isRed.put(p, false);
            p.teleport(this.blueLocation);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("joinBlueTeam")));
        }
        if (this.isRed.get(p)) {
            p.getInventory().setChestplate(this.redChestPlate);
            p.getInventory().setLeggings(this.redLeggings);
        } else {
            p.getInventory().setChestplate(this.blueChestPlate);
            p.getInventory().setLeggings(this.blueLeggings);
        }
        if (this.phase > 2) {
            this.sidebar.addPlayer(p);
        }
    }

    public void kick(final Player p) {
        if (this.isRed.containsKey(p)) {
            if (this.isRed.get(p))
                this.lastKickRed = p;
            else
                this.lastKickBlue = p;
            this.lastKick = p;
        }
    }

    public void tackle(final Player p) {
        if (this.isRed.containsKey(p)) {
            if (this.isRed.get(p)) {
                this.blueAssist.add(Bukkit.getPlayer("nobody"));
            } else {
                this.blueAssist.add(Bukkit.getPlayer("nobody"));
            }
        }
    }

    public void assist(final Player p) {
        if (this.isRed.containsKey(p)) {
            if (this.isRed.get(p)) {
                this.redAssist.add(p);
            } else {
                this.blueAssist.add(p);
            }
        }
    }

    public void teamchat(final Player p, final String message) {
        if (this.isRed.containsKey(p)) {
            for (Player p1 : this.isRed.keySet()) {
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
            this.join(p0);
            this.join(p1);
        } else if (this.type - this.bluePlayers.length >= 2) {
            this.join(p0);
            this.join(p1);
        } else {
            boolean rare = true;
            Player[] bluePlayers;
            for (int length = (bluePlayers = this.bluePlayers).length, i = 0; i < length; ++i) {
                final Player p2 = bluePlayers[i];
                if (!this.teamers.contains(p2)) {
                    this.bluePlayers = this.reduceArray(this.bluePlayers, p2);
                    this.redPlayers = this.extendArray(this.redPlayers, p2);
                    this.isRed.put(p2, true);
                    p2.teleport(this.redLocation);
                    p2.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("changeTeamWith").replace("{player1}", p0.getName()).replace("{player2}", p1.getName())));
                    this.join(p0);
                    this.join(p1);
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
                        p3.teleport(this.blueLocation);
                        p3.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("changeTeamWith").replace("{player1}", p0.getName()).replace("{player2}", p1.getName())));
                        this.join(p0);
                        this.join(p1);
                        break;
                    }
                }
            }
        }
        return true;
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

        if (red) {
            if (this.lastKickRed != null)
                scorer = this.lastKickRed;
            else
                scorer = this.lastKickBlue;
            if (this.type != 1)
                assister = this.redAssist.get(0);
            team = "red";
            ++this.scoreRed;
            ++this.redGoals;
        } else {
            if (this.lastKickBlue != null)
                scorer = this.lastKickBlue;
            else
                scorer = this.lastKickRed;
            if (this.type != 1)
                assister = this.blueAssist.get(1);
            team = "blue";
            ++this.scoreBlue;
            ++this.blueGoals;
        }
        if (this.type != 1) {
            this.blueAssist.clear();
            this.redAssist.clear();
            this.blueAssist.add(Bukkit.getPlayer("nobody"));
            this.redAssist.add(Bukkit.getPlayer("nobody"));
        }
        if (!this.takePlace.contains(scorer) && this.type != 1) {
            final Double scoreReward = this.plugin.getConfig().getDouble("scoreReward");
            final Double hattyReward = this.plugin.getConfig().getDouble("hattyReward");
            this.organization.stats.riseStats(scorer.getName(), "goals");
            if (this.goals.containsKey(scorer)) {
                this.goals.put(scorer, this.goals.get(scorer) + 1);
            } else {
                this.goals.put(scorer, 1);
            }
            this.organization.economy.depositPlayer(scorer.getName(), scoreReward);
            scorer.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoreReward")).replace("{reward}", "" + scoreReward));
            if (this.goals.get(scorer) == 3) {
                scorer.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("hattyReward")).replace("{reward}", "" + hattyReward));
                for (final Player p : this.isRed.keySet()) {
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("hatty").replace("{player}", scorer.getName())));
                }
                this.organization.economy.depositPlayer(scorer.getName(), hattyReward);
            }
        }

        if (!this.takePlace.contains(assister) && scorer != assister && assister != null) {
            final Double assistReward = this.plugin.getConfig().getDouble("assistReward");
            this.organization.stats.riseStats(assister.getName(), "assists");
            this.organization.economy.depositPlayer(assister.getName(), assistReward);
            if (this.assists.containsKey(assister)) {
                this.assists.put(assister, this.assists.get(assister) + 1);
            } else {
                this.assists.put(assister, 1);
            }
            assister.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("assistReward")).replace("{reward}", "" + assistReward));
        }

        if (team.equalsIgnoreCase("red")) {
            Location blueish = this.blueLocation.clone();
            double yaw = blueish.getYaw();
            if (yaw > 45 && yaw < 135)
                blueish.setX(blueish.getX() - 3);
            else if (yaw > 135 && yaw < 225)
                blueish.setZ(blueish.getZ() - 3);
            else if (yaw > 235 && yaw < 325)
                blueish.setX(blueish.getX() + 3);
            else
                blueish.setZ(blueish.getZ() + 3);
            try {
                (new GoalExplosion()).init(blueish, this.organization.db.getString("players", scorer.getName(), "goal_explosion"), 1, this.plugin);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Location redish = this.redLocation.clone();
            double yaw = redish.getYaw();
            if (yaw > 45 && yaw < 135)
                redish.setX(redish.getX() - 3);
            else if (yaw > 135 && yaw < 225)
                redish.setZ(redish.getZ() - 3);
            else if (yaw > 235 && yaw < 325)
                redish.setX(redish.getX() + 3);
            else
                redish.setZ(redish.getZ() + 3);
            try {
                (new GoalExplosion()).init(redish, this.organization.db.getString("players", scorer.getName(), "goal_explosion"), 0, this.plugin);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (final Player p : this.isRed.keySet()) {
            final double number = this.cube.getLocation().distance(scorer.getLocation());
            Math.round(number);
            String message = ChatColor.translateAlternateColorCodes('&', this.organization.db.getString("players", scorer.getName(), "custom-score-message"));
            String tgoal = "O";
            //new title(p, scorer, message).start();
            if (!message.isEmpty()) {
                TitleAPI.sendTitle(p, 5, 20, 5, ChatColor.translateAlternateColorCodes('&',"&6&lGOAL!"), ChatColor.WHITE + message);
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("customScoreMessage")).replace("{player}", scorer.getName()).replace("{message}", message));
            } else {
                for (int i = 0; i < 7; i++) {
                    TitleAPI.sendTitle(p, 5, 20, 5, ChatColor.translateAlternateColorCodes('&', "&lG" + tgoal + "AL!"), ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoreGoal").replace("{player}", scorer.getName())));
                    tgoal = tgoal.concat("O");
                }
                if (assister != scorer && assister != null) {
                    if (number > 20.0) {
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredIncAss").replace("{player}", scorer.getName()).replace("{assist}", "" + assister.getName()).replace("{distance}", "" + Math.round(number))));
                    } else {
                        System.out.println("Assist: " + assister.getName());
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredNormAss").replace("{player}", scorer.getName()).replace("{assist}", "" + assister.getName()).replace("{distance}", "" + Math.round(number))));
                    }
                } else {
                    if (number > 20.0) {
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredInc").replace("{player}", scorer.getName()).replace("{distance}", "" + Math.round(number))));
                    } else {
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoredNorm").replace("{player}", scorer.getName()).replace("{distance}", "" + Math.round(number))));
                    }
                }
            }
            Location goalLocation;
            World world = this.blueLocation.getWorld();
            if (red)
                goalLocation = this.redLocation;
            else
                goalLocation = this.blueLocation;
            if (this.plugin.getConfig().getBoolean("goalThrow"))
                p.setVelocity(explosionVector(p, goalLocation, 3));
            for (Entity nearby : world.getNearbyEntities(goalLocation, 6, 2, 6)) {
                if (nearby instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) nearby;
                    entity.damage(0);
                }
            }
            if (this.time <= 0) {
                this.organization.endMatch(p);
                FileConfiguration cfg = this.plugin.getConfig();
                Double x = cfg.getDouble("afterMatchRespawn.1");
                Double y = cfg.getDouble("afterMatchRespawn.2");
                Double z = cfg.getDouble("afterMatchRespawn.3");
                final Double winReward = cfg.getDouble("winReward");
                this.removeSidebar();
                if (this.isRed.get(p) == red && !this.takePlace.contains(p) && this.type != 1) {
                    this.organization.stats.riseStats(p.getName(), "wins");
                    this.organization.stats.riseStats(p.getName(), "win_streak");
                    int ws = this.organization.db.getInt("players", p.getName(), "win_streak");
                    int bws = this.organization.db.getInt("players", p.getName(), "best_win_streak");
                    if (ws > bws) {
                        this.organization.db.updateInt("players", p.getName(), "best_win_streak", ws);
                    }
                    this.organization.economy.depositPlayer(p.getName(), winReward);
                    p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("winReward")).replace("{reward}", "" + winReward));
                } else if (!this.takePlace.contains(p) && this.type != 1) {
                    this.organization.db.updateInt("players", p.getName(), "win_streak", 0);
                }
                if (x == 0.0 && y == 0.0 && z == 0.0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                } else {
                    Location loc = new Location(p.getWorld(), x, y, z);
                    p.teleport(loc);
                }
                this.organization.clearInventory(p);
            } else {
                p.setLevel(10);
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("result").replace("{red}", "" + this.scoreRed).replace("{blue}", "" + this.scoreBlue)));
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("continue5sec")));
            }
        }
    }

    public void update() {
        --this.tickToSec;
        if (this.phase == 1)
            return;

        if (this.phase == 3) {
            final Location l = this.cube.getLocation();
            if (this.x) {
                if (((this.redAboveBlue && l.getBlockX() >= this.redLocation.getBlockX()) || (!this.redAboveBlue && this.redLocation.getBlockX() >= l.getBlockX())) && l.getY() < this.redLocation.getY() + 3.0 && l.getZ() < this.redLocation.getZ() + 4.0 && l.getZ() > this.redLocation.getZ() - 4.0) {
                    this.score(false);
                } else if (((this.redAboveBlue && l.getBlockX() <= this.blueLocation.getBlockX()) || (!this.redAboveBlue && this.blueLocation.getBlockX() <= l.getBlockX())) && l.getY() < this.blueLocation.getY() + 3.0 && l.getZ() < this.blueLocation.getZ() + 4.0 && l.getZ() > this.blueLocation.getZ() - 4.0) {
                    this.score(true);
                }
            } else if (((this.redAboveBlue && l.getBlockZ() >= this.redLocation.getBlockZ()) || (!this.redAboveBlue && this.redLocation.getBlockZ() >= l.getBlockZ())) && l.getY() < this.redLocation.getY() + 3.0 && l.getX() < this.redLocation.getX() + 4.0 && l.getX() > this.redLocation.getX() - 4.0) {
                this.score(false);
            } else if (((this.redAboveBlue && l.getBlockZ() <= this.blueLocation.getBlockZ()) || (!this.redAboveBlue && this.blueLocation.getBlockZ() <= l.getBlockZ())) && l.getY() < this.blueLocation.getY() + 3.0 && l.getX() < this.blueLocation.getX() + 4.0 && l.getX() > this.blueLocation.getX() - 4.0) {
                this.score(true);
            }
        }

        if ((this.phase == 2 || this.phase == 4) && this.tickToSec == 0) {
            --this.countdown;
            this.tickToSec = 20;
            for (final Player p : this.isRed.keySet()) {
                p.setLevel(this.countdown);
            }
            if (!prematchSidebarSet) {
                this.enableSidebar("prematch");
                this.prematchSidebarSet = true;
            }
            if (this.countdown <= 0) {
                String message;
                if (this.phase == 2) {
                    message = this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("startingMatch"));
                    this.startTime = System.currentTimeMillis();
                    this.redGoals = 0;
                    this.blueGoals = 0;
                    this.blueAssist.add(Bukkit.getPlayer("nobody"));
                    this.blueAssist.add(Bukkit.getPlayer("nobody"));
                    this.enableSidebar("ingame");
                    for (final Player p : this.isRed.keySet()) {
                        this.arena = this.organization.findArena(p);
                        this.organization.playerStarts(p);
                    }
                } else {
                    message = this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("continueMatch"));
                }
                this.phase = 3;
                this.cube = this.plugin.spawnCube(this.middleLocation);
                for (final Player p : this.isRed.keySet()) {
                    p.sendMessage(message);
                    if (this.isRed.get(p)) {
                        p.teleport(this.redLocation);
                    } else {
                        p.teleport(this.blueLocation);
                    }
                    p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                }
            } else if (this.countdown <= 3) {
                for (final Player p : this.isRed.keySet()) {
                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
                }
            }
        }

        if (this.phase != 2 && this.phase != 4) {
            if (this.type == 2 || this.type == 1)
                this.time = 180 + this._countdown - (int) (System.currentTimeMillis() - this.startTime) / 1000;
            else if (this.type == 3)
                this.time = 240 + this._countdown - (int) (System.currentTimeMillis() - this.startTime) / 1000;
            else if (this.type == 4)
                this.time = 300 + this._countdown - (int) (System.currentTimeMillis() - this.startTime) / 1000;
            else if (this.type == 5)
                this.time = 300 + this._countdown - (int) (System.currentTimeMillis() - this.startTime) / 1000;

            this.enableSidebar("ingame");

            if (this.ticksLived == this.cube.getTicksLived() && this.ticksLived != 0 && this.time > 0) {
                this.cube.setHealth(0.0);
                this.cube = this.plugin.spawnCube(this.middleLocation);
            }

            this.ticksLived = this.cube.getTicksLived();

            if (this.time <= 0 && this.phase > 2) {
                for (final Player p : this.isRed.keySet()) {
                    this.organization.endMatch(p);
                    FileConfiguration cfg = this.plugin.getConfig();
                    final Double winReward = cfg.getDouble("winReward");
                    final Double tiedReward = cfg.getDouble("tiedReward");
                    Double x = cfg.getDouble("afterMatchRespawn.1");
                    Double y = cfg.getDouble("afterMatchRespawn.2");
                    Double z = cfg.getDouble("afterMatchRespawn.3");
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
                                this.organization.stats.riseStats(p.getName(), "wins");
                                this.organization.stats.riseStats(p.getName(), "win_streak");
                                int ws = this.organization.db.getInt("players", p.getName(), "win_streak");
                                int bws = this.organization.db.getInt("players", p.getName(), "best_win_streak");
                                if (ws > bws) {
                                    this.organization.db.updateInt("players", p.getName(), "best_win_streak", ws);
                                }
                                this.organization.economy.depositPlayer(p.getName(), winReward);
                                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("winReward")).replace("{reward}", "" + winReward));
                            } else {
                                this.organization.db.updateInt("players", p.getName(), "win_streak", 0);
                            }
                        } else {
                            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("1v1noStats")));
                        }
                    } else if (this.scoreRed < this.scoreBlue) {
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("timesUpBlue").replace("{blue}", "" + this.scoreBlue).replace("{red}", "" + this.scoreRed)));
                        if (this.type != 1) {
                            if (!this.isRed.get(p) && !this.takePlace.contains(p)) {
                                this.organization.stats.riseStats(p.getName(), "wins");
                                this.organization.stats.riseStats(p.getName(), "win_streak");
                                int ws = this.organization.db.getInt("players", p.getName(), "win_streak");
                                int bws = this.organization.db.getInt("players", p.getName(), "best_win_streak");
                                if (ws > bws) {
                                    this.organization.db.updateInt("players", p.getName(), "best_win_streak", ws);
                                }
                                this.organization.economy.depositPlayer(p.getName(), winReward);
                                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("winReward")).replace("{reward}", "" + winReward));
                            } else {
                                this.organization.db.updateInt("players", p.getName(), "win_streak", 0);
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
                        this.organization.stats.riseStats(p.getName(), "ties");
                        this.organization.db.updateInt("players", p.getName(), "win_streak", 0);
                        this.organization.economy.depositPlayer(p.getName(), tiedReward);
                        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tiedReward")).replace("{reward}", "" + tiedReward));
                    }
                }
                this.removeSidebar();
                this.phase = 1;
                this.cube.setHealth(0.0);
                this.prematchSidebarSet = false;
                this.organization.undoTakePlace(this);
                this.scoreRed = 0;
                this.scoreBlue = 0;
                this.teams = 0;
                this.redPlayers = new Player[0];
                this.bluePlayers = new Player[0];
                this.teamers = new ArrayList<Player>();
                this.isRed = new HashMap<Player, Boolean>();
                this.takePlace.clear();
                this.redAssist.clear();
                this.blueAssist.clear();
                this.goals.clear();
                this.assists.clear();
                this._countdown = 0;
            }
        }
    }
}
//
// sta je ovo ivane crni sine
//
//class title extends Thread {
//    private Player p;
//    private Player scorer;
//    private String message;
//    public title(Player p, Player scorer, String message) {
//        this.p = p;
//        this.scorer=scorer;
//        this.message = message;
//    }
//    public void run() {
//        String tgoal = "O";
//        if(this.message.length()>0 && this.message != null) {
//            this.p.sendTitle(ChatColor.translateAlternateColorCodes('&',"&6&lGOAL!"), ChatColor.WHITE + message);
//            this.suspend();
//        }
//        for (int j = 0; j < 7; j++) {
//            this.p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&lG" + tgoal + "AL!"), ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("scoreGoal").replace("{player}", this.scorer.getName())));
//            tgoal = tgoal.concat("O");
//            try {
//                Thread.sleep(350);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        this.suspend();
//    }
//}