package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.utils.Particle;
import org.bukkit.Location;

import java.util.*;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import me.qajic.plugins.qfootcube.Footcube;
import org.bukkit.event.Listener;

public class Particles implements Listener {
    private Footcube plugin;
    public String pluginString;

    public Particles(final Footcube instance) {
        this.plugin = instance;
        this.pluginString = ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("prefix"));
    }
    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        final UUID uuid = e.getWhoClicked().getUniqueId();
        final File userFile = new File("plugins" + File.separator + "qFootcube" + File.separator + "/users/" + uuid + ".yml");
        final FileConfiguration path = (FileConfiguration)YamlConfiguration.loadConfiguration(userFile);
        String effect = "";
        if (e.getCurrentItem().hasItemMeta()) {
            effect = e.getCurrentItem().getItemMeta().getDisplayName();
        }
        if (!e.getInventory().getName().equalsIgnoreCase("§3§lPar§b§ltic§f§lles §fMenu")) {
            return;
        }
        e.setCancelled(true);
        final String s;
        final String s4;
        switch (s4 = (s = effect)) {
            case " §cHearts": {
                if(p.hasPermission("footcube.particles.hearts") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", (Object) "Hearts");
                    try {
                        path.save(userFile);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §cDisable": {
                path.set("particles", "Disable");
                try {
                    path.save(userFile);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("disabledParticles").replace("{effect}", effect)));
                p.closeInventory();

                break;
            }
            case " §aGreen": {
                if(p.hasPermission("footcube.particles.green") || p.hasPermission("footcube.particles.all")) {

                    path.set("particles", "Green");
                    try {
                        path.save(userFile);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §6Flames": {
                if(p.hasPermission("footcube.particles.flames") || p.hasPermission("footcube.particles.all")) {

                    path.set("particles", "Flames");
                    try {
                        path.save(userFile);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §7Flakes": {
                if(p.hasPermission("footcube.particles.flakes") || p.hasPermission("footcube.particles.all")) {

                    path.set("particles", "Flakes");
                    try {
                        path.save(userFile);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §fSparky": {
                if(p.hasPermission("footcube.particles.sparky") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Sparky");
                    try {
                        path.save(userFile);
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §4Red": {
                if(p.hasPermission("footcube.particles.red") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Red");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §5Portal": {
                if(p.hasPermission("footcube.particles.portal") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Portal");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §dSpell": {
                if(p.hasPermission("footcube.particles.spell") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Spell");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §7Cloud": {
                if(p.hasPermission("footcube.particles.cloud") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Cloud");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §8Angry": {
                if(p.hasPermission("footcube.particles.angry") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Angry");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §dNotes": {
                if(p.hasPermission("footcube.particles.notes") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Notes");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §5Magic": {
                if(p.hasPermission("footcube.particles.magic") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Magic");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §eDizzy": {
                if(p.hasPermission("footcube.particles.dizzy") || p.hasPermission("footcube.particles.all")) {
                    path.set("particles", "Dizzy");
                    try {
                        path.save(userFile);
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            default:
                break;
        }
    }

    public void cubeEffect() {
        Collection<? extends Player> onlinePlayers;
        for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), k = 0; k < length; ++k) {
            final Player p = (Player)onlinePlayers.toArray()[k];
            final UUID uuid = p.getUniqueId();
            for (final Slime cube : this.plugin.cubes) {
                if (!cube.isDead()) {
                    final Location loc = cube.getLocation();
                    final File userFile = new File("plugins" + File.separator + "qFootcube" + File.separator + "/users/" + uuid + ".yml");
                    final FileConfiguration path = (FileConfiguration) YamlConfiguration.loadConfiguration(userFile);
                    final String effect = path.getString("particles");
                    final String s;
                    final String s2;
                    if (userFile.exists()) {
                        switch (s2 = (s = effect)) {
                            case "Hearts": {
                                final Particle hearts = new Particle(EnumParticle.HEART, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                hearts.toPlayer(p);
                                continue;
                            }
                            case "Sparky": {
                                final Particle sparky = new Particle(EnumParticle.FIREWORKS_SPARK, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                sparky.toPlayer(p);
                                continue;
                            }
                            case "Red": {
                                final Particle red = new Particle(EnumParticle.REDSTONE, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                red.toPlayer(p);
                                continue;
                            }
                            case "Disable": {
                                continue;
                            }
                            case "Green": {
                                final Particle green = new Particle(EnumParticle.VILLAGER_HAPPY, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                green.toPlayer(p);
                                continue;
                            }
                            case "Flames": {
                                final Particle flames = new Particle(EnumParticle.FLAME, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                flames.toPlayer(p);
                                continue;
                            }
                            case "Flakes": {
                                final Particle flakes = new Particle(EnumParticle.SNOWBALL, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                flakes.toPlayer(p);
                                continue;
                            }
                            case "Portal": {
                                final Particle _new = new Particle(EnumParticle.PORTAL, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            case "Spell": {
                                final Particle _new = new Particle(EnumParticle.SPELL, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            case "Cloud": {
                                final Particle _new = new Particle(EnumParticle.CLOUD, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            case "Angry": {
                                final Particle _new = new Particle(EnumParticle.VILLAGER_ANGRY, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            case "Notes": {
                                final Particle _new = new Particle(EnumParticle.NOTE, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            case "Magic": {
                                final Particle _new = new Particle(EnumParticle.SPELL_WITCH, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            case "Dizzy": {
                                final Particle _new = new Particle(EnumParticle.SPELL_MOB, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                                _new.toPlayer(p);
                                continue;
                            }
                            default:
                                break;
                        }
                        return;
                    }
                }
            }
        }
    }
}
