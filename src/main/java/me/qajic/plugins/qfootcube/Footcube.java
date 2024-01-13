package me.qajic.plugins.qfootcube;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jdk.nashorn.internal.objects.annotations.Getter;
import me.qajic.plugins.qfootcube.commands.FootcubeCommand;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.core.Organization;
import me.qajic.plugins.qfootcube.features.Glove;
import me.qajic.plugins.qfootcube.features.GoalExplosions;
import me.qajic.plugins.qfootcube.features.PapiExpansion;
import me.qajic.plugins.qfootcube.features.Particles;
import me.qajic.plugins.qfootcube.utils.LuckPermsHelper;
import me.qajic.plugins.qfootcube.utils.Particle;
import net.luckperms.api.LuckPerms;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings("ALL")
public final class Footcube extends JavaPlugin implements Listener
{
    private Logger logger;
    public HashSet<Slime> cubes;
    public HashSet<Slime> pickedCubes;
    private HashMap<UUID, Vector> velocities;
    public HashMap<String, Long> kicked;
    public LuckPerms luckPermsAPI = null;
    public LuckPermsHelper lpHelper;
    private HashMap<String, Double> speed;
    private HashMap<String, Double> charges;
    public Organization organization;
    private Glove gloves;
    private Sound sound;
    private ArrayList<Player> immune;
    private HashMap<Player, BukkitTask> immuneMap;
    public List<String> autohitter;
    public HashSet<Slime> macubes;
    public HashMap<String, Boolean> shooting;
    public Boolean state;
    Particles particles;
    GoalExplosions explosions;
    Particle particle;
    public ScoreboardLibrary scoreboardLibrary;
    public MongoDatabase database;
    public MongoClient mongoClient;

    public Footcube() {
        this.logger = Logger.getLogger("Minecraft");
        this.cubes = new HashSet<Slime>();
        this.pickedCubes = new HashSet<Slime>();
        this.macubes = new HashSet<Slime>();
        this.velocities = new HashMap<UUID, Vector>();
        this.kicked = new HashMap<String, Long>();
        this.speed = new HashMap<String, Double>();
        this.charges = new HashMap<String, Double>();
        this.shooting = new HashMap<String, Boolean>();
        this.autohitter = new ArrayList<String>();
        this.sound = Sound.SLIME_WALK;
        this.immune = new ArrayList<Player>();
        this.immuneMap = new HashMap<Player, BukkitTask>();
        this.state = true;
    }

    public void onDisable() {
        final PluginDescriptionFile pdfFile = this.getDescription();
        this.logger.info(String.valueOf(pdfFile.getName()) + " successfully disabled.");
    }

    public void onEnable() {
        final PluginDescriptionFile pdfFile = this.getDescription();
        try {
            this.registerConfigs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.logger.info(String.valueOf(pdfFile.getName()) + " V" + pdfFile.getVersion() + " is successfully enabled.");
        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this);
        } catch (NoPacketAdapterAvailableException e) {
            // If no packet adapter was found, you can fallback to the no-op implementation:
            scoreboardLibrary = new NoopScoreboardLibrary();
        }
        this.scoreboardLibrary = scoreboardLibrary;
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.connectToDatabase();
        this.organization = new Organization(this);
        this.setupLuckPermsAPI();
        this.lpHelper = new LuckPermsHelper(this);
        this.gloves = new Glove(this);
        this.getServer().getPluginManager().registerEvents((Listener)this.gloves, (Plugin)this);
        this.particles = new Particles(this);
        this.explosions = new GoalExplosions(this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                Footcube.this.update();
                Footcube.this.particles.cubeEffect();
            }
        }, 20L, 1L);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiExpansion(this).register();
        }
        this.registerCommands();
        this.registerListeners();
    }

    private boolean setupLuckPermsAPI() {
        RegisteredServiceProvider<LuckPerms> lpp = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (lpp != null)
            this.luckPermsAPI = lpp.getProvider();
        return this.luckPermsAPI != null;
    }

    private void registerConfigs() throws IOException {
        this.saveResource("messages.yml", false);
        this.saveResource("config.yml", false);
        FileConfiguration cfg = this.getConfig();
        this.saveConfig();
        this.saveImages();
        MessagesConfig.create();
        MessagesConfig.save();
    }
    private void saveImages() throws IOException {
        FileUtils.copyInputStreamToFile(this.getResource("poo.png"), new File("plugins/qFootcube/images/poo.png"));
        FileUtils.copyInputStreamToFile(this.getResource("serbia.png"), new File("plugins/qFootcube/images/serbia.png"));
        FileUtils.copyInputStreamToFile(this.getResource("spain.png"), new File("plugins/qFootcube/images/spain.png"));
    }
    private void registerListeners() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new Particles(this), (Plugin)this);
        pm.registerEvents((Listener)new GoalExplosions(this), (Plugin)this);
    }

    private void registerCommands() {
        this.getCommand("footcube").setExecutor((CommandExecutor)new FootcubeCommand(this));
        this.getCommand("tc").setExecutor((CommandExecutor)new FootcubeCommand(this));
    }

    private void connectToDatabase() {
        String connectionString = this.getConfig().getString("mongo-secret");
        this.mongoClient = MongoClients.create(connectionString);
        try {
            this.database = this.mongoClient.getDatabase(this.getConfig().getString("mongo-database"));
            this.getLogger().info("Successfully connected to database!");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String c, final String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("cube") && p.getWorld().getDifficulty() != Difficulty.PEACEFUL && p.hasPermission("footcube.spawncube")) {
            final Location loc = p.getLocation().add(0.0, 1.0, 0.0);
            for (final Entity entity : loc.getWorld().getNearbyEntities(loc, 50, 40, 50)) {
                if (entity instanceof Slime) {
                    List<Slime> cubes = new ArrayList<>();
                    cubes.add((Slime) entity);
                }
            }
            if (cubes.size() >= 5) {
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("toomanycubes")));
                return false;
            }
            if (this.immuneMap.containsKey(p)) {
                Bukkit.getScheduler().cancelTask(this.immuneMap.get(p).getTaskId());
                this.immuneMap.remove(p);
                this.immune.remove(p);
            }
            this.immune.add(p);
            final BukkitTask taskID = new BukkitRunnable() {
                public void run() {
                    Footcube.this.immune.remove(p);
                    Footcube.this.immuneMap.remove(p);
                }
            }.runTaskLater((Plugin)this, 60L);
            this.immuneMap.put(p, taskID);
            this.spawnCube(loc);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("cubespawn")));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("clearCubes") && p.hasPermission("footcube.clearcubes")) {
            int i = 0;
            for (final Slime cube : this.cubes) {
                cube.setHealth(0.0);
                ++i;
            }
            this.cubes.clear();
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("clearcubes")));
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("clearCube") && p.hasPermission("footcube.clearcube")) {
            final double distance = 20.0;
            for (final Slime cube2 : this.cubes) {
                if (this.getDistance(cube2.getLocation(), p.getLocation()) < 20.0) {
                    cube2.setHealth(0.0);
                    this.cubes.remove(cube2);
                    break;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Location to = e.getTo();
        final Location from = e.getFrom();
        final double x = Math.abs(to.getX() - from.getX());
        final double y = Math.abs(to.getY() - from.getY()) / 2.0;
        final double z = Math.abs(to.getZ() - from.getZ());
        this.speed.put(e.getPlayer().getName(), Math.sqrt(x * x + y * y + z * z));
    }

    @EventHandler
    public void onForm(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if (fallingBlock.getMaterial() != Material.SAND) {
                fallingBlock.getLocation().getBlock().setType(Material.AIR);
                fallingBlock.setDropItem(false);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        this.speed.remove(e.getPlayer().getName());
        this.charges.remove(e.getPlayer().getName());
    }

    @EventHandler
    public void onUnloadChunk(final ChunkUnloadEvent e) {
        Entity[] entities;
        for (int length = (entities = e.getChunk().getEntities()).length, i = 0; i < length; ++i) {
            final Entity entity = entities[i];
            if (entity instanceof Slime && this.cubes.contains(entity)) {
                this.cubes.remove(entity);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Slime && this.cubes.contains(e.getEntity())) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
            e.setDamage(0.0);
        }
    }

    @EventHandler
    public void onRightClick(final PlayerInteractEntityEvent e) {
        final Entity entity = e.getRightClicked();
        if (entity instanceof Slime && this.cubes.contains(entity) && !this.kicked.containsKey(e.getPlayer().getName()) && e.getPlayer().getGameMode() != GameMode.SPECTATOR && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            final Slime cube = (Slime)entity;
            cube.setVelocity(cube.getVelocity().add(new Vector(0.0D, 0.7D, 0.0D))); // RAJT KLIK
            cube.getWorld().playSound(cube.getLocation(), this.sound, 1.0f, 1.0f);
            this.kicked.put(e.getPlayer().getName(), System.currentTimeMillis());
            this.organization.ballTouch(e.getPlayer());
        }
    }

    @EventHandler
    public void onSneak(final PlayerToggleSneakEvent e) {
        final Player p = e.getPlayer();
        if (e.isSneaking()) {
            this.charges.put(p.getName(), 0.0);
        }
        else {
            p.setExp(0.0f);
            this.charges.remove(p.getName());
        }
    }
    public void waitForShot(Player p) {
        this.shooting.put(p.getName(), false);
        new BukkitRunnable() {
            @Override
            public void run() {
                Footcube.this.shooting.put(p.getName(), true);
            }
        }.runTaskLater(this, 4);
    }
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }
    @EventHandler
    public void onSlamSlime(final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Slime)) return;
        if (!(this.cubes.contains((Slime) e.getEntity()))) {
            ((Slime) e.getEntity()).setHealth(0.0);
            return;
        }
        if (!(e.getDamager() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        final Player p = (Player)e.getDamager();
        final int ping = ((CraftPlayer)p).getHandle().ping;
        final FileConfiguration cfg = this.getConfig();
        double charge = 1.0D;
        boolean next;
        Vector kick = new Vector();
        if(!this.shooting.containsKey(p.getName()))
            next = true;
        else
            next=this.shooting.get(p.getName());

        if (this.charges.containsKey(p.getName())) {
            charge += this.charges.get(p.getName()) * 7.0D;
        }
        else if (e.getEntity() instanceof Slime && !this.pickedCubes.contains(e.getEntity()) && next==true && this.cubes.contains(e.getEntity()) && e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            final Slime cube = (Slime)e.getEntity();
            if (p.getGameMode() == GameMode.CREATIVE) {
                e.setCancelled(true);
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("creativeBlock")));
            }
            else if (p.getGameMode() != GameMode.SPECTATOR && p.getGameMode() != GameMode.CREATIVE){
                Collection<? extends Player> onlinePlayers2;
                for (int length2 = (onlinePlayers2 = (Collection<? extends Player>)this.getServer().getOnlinePlayers()).size(), j = 0; j < length2; ++j) {
                    final Player p2 = (Player)onlinePlayers2.toArray()[j];
                    if (p2.hasPermission("footcube.admin")) {
                        final String timestamp = this.getCurrentTimeStamp();
                        if (charge > 1.0 && this.autohitter.contains(p2.getName())) {
                            final double radius = 100.0;
                            if (p2.getLocation().distance(p.getLocation()) <= 100.0) {
                                p2.sendMessage(" " + ChatColor.DARK_RED + timestamp + ChatColor.DARK_GRAY + " ❘ " + ChatColor.WHITE + p.getName() + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Ping: " + ChatColor.GOLD + ping + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Kickpower: " + ChatColor.YELLOW + (100 + 5 * cfg.getInt("kickpower")) + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Charge: " + ChatColor.RED + charge);
                            }
                        }
                    }
                    final String timestamp = this.getCurrentTimeStamp();
                    if (charge == 1.0 && this.autohitter.contains(p2.getName())) {
                        final double radius = 100.0;
                        if (p2.getLocation().distance(p.getLocation()) <= 100.0) {
                            p2.sendMessage(" " + ChatColor.DARK_RED + timestamp + ChatColor.DARK_GRAY + " ❘ " + ChatColor.WHITE + p.getName() + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Ping: " + ChatColor.GOLD + ping + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Kickpower: " + ChatColor.YELLOW + (100 + 5 * cfg.getInt("kickpower")) + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Charge: " + ChatColor.RED + charge);
                        }
                    }
                }
                double power = this.speed.get(p.getName()) * 2.0D + 0.4D;
                if (charge <= 1.0) {
                    kick = p.getLocation().getDirection().normalize().multiply(power * charge * (1.0D + 0.05D * cfg.getInt("uc_kickpower"))).setY(0.2D);
                    waitForShot(p);
                }
                else if (charge > 1.0) {
                    kick = p.getLocation().getDirection().normalize().multiply(power * charge * (1.0D + 0.05D * cfg.getInt("kickpower"))).setY(0.2D);
                    waitForShot(p);
                }
                Vector newVelocity = cube.getVelocity().add(kick);
                cube.setVelocity(newVelocity);
                cube.getWorld().playSound(cube.getLocation(), Sound.SLIME_WALK, 1.0f, 1.0f);
                this.organization.ballTouch(p);
                this.organization.tackle(p);
                this.organization.assisttouch(p);
                e.setCancelled(true);
            }
        }
        if (e.getEntity() instanceof Slime && this.macubes.contains(e.getEntity()) && e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && p.getGameMode() != GameMode.SPECTATOR && p.getGameMode() != GameMode.CREATIVE) {
            Collection<? extends Player> onlinePlayers3;
            for (int length3 = (onlinePlayers3 = (Collection<? extends Player>)this.getServer().getOnlinePlayers()).size(), i = 0; i < length3; ++i) {
                final Player p3 = (Player)onlinePlayers3.toArray()[i];
                if (p3.hasPermission("footcube.autohitter") && this.autohitter.contains(p3.getName())) {
                    final String timestamp2 = this.getCurrentTimeStamp();
                    final double radius2 = 100.0;
                    if (p3.getLocation().distance(p.getLocation()) <= 100.0) {
                        p3.sendMessage(" " + ChatColor.DARK_RED + timestamp2 + ChatColor.DARK_GRAY + " ❘ " + ChatColor.WHITE + p.getName() + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Ping: " + ChatColor.GOLD + ping + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Kickpower: " + ChatColor.YELLOW + (100 + 5 * cfg.getInt("kickpower")) + ChatColor.DARK_GRAY + " ❘ " + ChatColor.GRAY + "Charge: " + ChatColor.RED + charge);
                    }
                }
            }
        }
        if (e.getEntity() instanceof Endermite && e.getDamager() instanceof Player) {
            e.getCause();
            final EntityDamageEvent.DamageCause entity_ATTACK = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            e.getCause();
            final EntityDamageEvent.DamageCause entity_ATTACK2 = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
        }
    }

    public Slime spawnCube(final Location loc) {
        final Slime cube = (Slime)loc.getWorld().spawnEntity(loc, EntityType.SLIME);
        cube.setRemoveWhenFarAway(false);
        cube.setSize(1);
        this.cubes.add(cube);
        return cube;
    }

    private double getDistance(final Location locA, final Location locB) {
        locA.add(0.0D, -1.0D, 0.0D);
        final double dx = Math.abs(locA.getX() - locB.getX());
        double dy = Math.abs(locA.getY() - locB.getY() - 0.25D) - 1.25D;
        if (dy < 0.0D) {
            dy = 0.0D;
        }
        final double dz = Math.abs(locA.getZ() - locB.getZ());
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private void update() {
        if (this.kicked.size() > 0)
            this.kicked.entrySet().removeIf(entry -> System.currentTimeMillis() > this.kicked.get(entry.getKey()) + 1000L);
        Collection<? extends Player> onlinePlayers;
        final FileConfiguration cfg = this.getConfig();
        for (int length = (onlinePlayers = (Collection<? extends Player>) this.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
            final Player p = (Player) onlinePlayers.toArray()[i];
            p.setHealth(20.0);
            p.setSaturation(100.0F);
            p.setExhaustion(20.0F);
        }
        for (final String s2 : this.charges.keySet()) {
            final Player p2 = this.getServer().getPlayer(s2);
            double charge = !this.charges.isEmpty() ? this.charges.get(s2) : 0.0D;
            double nextCharge = 1.0D - (1.0D - charge) * (0.95D - cfg.getInt("charge") * 0.005D);
            this.charges.put(s2, nextCharge);
            p2.setExp((float) nextCharge);
        }
        if (this.cubes.isEmpty()) return;
        Iterator<Slime> iterator = this.cubes.iterator();
        while (iterator.hasNext()) {
            Slime cube = iterator.next();
            if (cube == null) return;
            final UUID id = cube.getUniqueId();
            Vector oldV = cube.getVelocity();
            if (this.velocities.containsKey(id)) oldV = this.velocities.get(id);
            if (cube.isDead()) {
                // ako ne radi ovo promeniti u iterator.remove(cube);
                this.cubes.removeIf(key -> this.cubes.contains(key));
                this.organization.practiceBalls.removeIf(key -> this.organization.practiceBalls.contains(key));
                return;
            }
            boolean sound = false;
            boolean kicked = false;
            final Vector newV = cube.getVelocity();
            Collection<? extends Player> onlinePlayers2;
            for (int length2 = (onlinePlayers2 = (Collection<? extends Player>) this.getServer().getOnlinePlayers()).size(), j = 0; j < length2; ++j) {
                final Player p3 = (Player) onlinePlayers2.toArray()[j];
                if (!this.immune.contains(p3) && p3.getGameMode() != GameMode.SPECTATOR && p3.getGameMode() != GameMode.CREATIVE) {
                    final double delta = this.getDistance(cube.getLocation(), p3.getLocation());
                    if (delta < 1.2D) {
                        if (delta < 0.8D && newV.length() > 0.5D) {
                            newV.multiply(0.5D / newV.length());
                        }
                        final double power = this.speed.get(p3.getName()) / 3.0D + oldV.length() / 6.0D;
                        newV.add(p3.getLocation().getDirection().setY(0).normalize().multiply(power));
                        this.organization.ballTouch(p3);
                        kicked = true;
                        if (power > 0.15D) {
                            sound = true;
                        }
                    }
                }
            }
            if (newV.getX() == 0.0D) {
                newV.setX(-oldV.getX() * 0.8D);
                if (Math.abs(oldV.getX()) > 0.3D) {
                    sound = true;
                }
            } else if (!kicked && Math.abs(oldV.getX() - newV.getX()) < 0.1D) {
                newV.setX(oldV.getX() * 0.98D);
            }
            if (newV.getZ() == 0.0D) {
                newV.setZ(-oldV.getZ() * 0.8D);
                if (Math.abs(oldV.getZ()) > 0.3D) {
                    sound = true;
                }
            } else if (!kicked && Math.abs(oldV.getZ() - newV.getZ()) < 0.1D) {
                newV.setZ(oldV.getZ() * 0.98D);
            }
            if (newV.getY() < 0.0D && oldV.getY() < 0.0D && oldV.getY() < newV.getY() - 0.05D) {
                newV.setY(-oldV.getY() * 0.8D);
                if (Math.abs(oldV.getY()) > 0.3D) {
                    sound = true;
                }
            }
            if (sound) {
                cube.getWorld().playSound(cube.getLocation(), this.sound, 1.0F, 1.0F);
            }
            Collection<? extends Player> onlinePlayers3;
            for (int length3 = (onlinePlayers3 = (Collection<? extends Player>) this.getServer().getOnlinePlayers()).size(), k = 0; k < length3; ++k) {
                final Player p4 = (Player) onlinePlayers3.toArray()[k];
                final double delta2 = this.getDistance(cube.getLocation(), p4.getLocation());
                if (delta2 < newV.length() * 1.3D) {
                    final Vector loc = cube.getLocation().toVector();
                    final Vector nextLoc = new Vector(loc.getX(), loc.getY(), loc.getZ()).add(newV);
                    boolean rightDirection = true;
                    final Vector pDir = new Vector(p4.getLocation().getX() - loc.getX(), 0.0D, p4.getLocation().getZ() - loc.getZ());
                    final Vector cDir = new Vector(newV.getX(), 0.0D, newV.getZ()).normalize();
                    int px = 1;
                    if (pDir.getX() < 0.0D) {
                        px = -1;
                    }
                    int pz = 1;
                    if (pDir.getZ() < 0.0D) {
                        pz = -1;
                    }
                    int cx = 1;
                    if (cDir.getX() < 0.0D) {
                        cx = -1;
                    }
                    int cz = 1;
                    if (cDir.getZ() < 0.0D) {
                        cz = -1;
                    }
                    if ((px != cx && pz != cz) || ((px != cx || pz != cz) && (cx * pDir.getX() <= cx * cz * px * cDir.getZ() || cz * pDir.getZ() <= cz * cx * pz * cDir.getX()))) {
                        rightDirection = false;
                    }
                    if (rightDirection && loc.getY() < p4.getLocation().getY() + 2.0D && loc.getY() > p4.getLocation().getY() - 1.0D && nextLoc.getY() < p4.getLocation().getY() + 2.0D && nextLoc.getY() > p4.getLocation().getY() - 1.0D) {
                        final double a = newV.getZ() / newV.getX();
                        final double b = loc.getZ() - a * loc.getX();
                        final double c = p4.getLocation().getX();
                        final double d = p4.getLocation().getZ();
                        final double D = Math.abs(a * c - d + b) / Math.sqrt(a * a + 1.0D);
                        if (D < 0.8D) {
                            newV.multiply(delta2 / newV.length());
                        }
                    }
                }
            }
            cube.setMaxHealth(20.0);
            cube.setHealth(20.0);
            cube.setVelocity(newV);
            cube.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, -3, true), true);
            cube.playEffect(EntityEffect.HURT);
            this.velocities.put(id, newV);
        }
    }
}
