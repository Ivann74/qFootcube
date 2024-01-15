package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.utils.Particle;
import me.qajic.plugins.qfootcube.utils.PlayerDataManager;
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
        PlayerDataManager playerData = new PlayerDataManager(this.plugin, this.plugin.organization.uuidConverter.get(p.getName()));
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
                    playerData.setString("particle", "Hearts");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedParticle").replace("{effect}", effect)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("particleNoPermission").replace("{effect}", effect)));
                    p.closeInventory();
                }
                break;
            }
            case " §cDisable": {
                playerData.setString("particle", "Disable");
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("disabledParticles").replace("{effect}", effect)));
                p.closeInventory();

                break;
            }
            case " §aGreen": {
                if(p.hasPermission("footcube.particles.green") || p.hasPermission("footcube.particles.all")) {
                    playerData.setString("particle", "Green");
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
                    playerData.setString("particle", "Flames");
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
                    playerData.setString("particle", "Flakes");
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
                    playerData.setString("particle", "Sparky");
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
                    playerData.setString("particle", "Red");
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
                    playerData.setString("particle", "Portal");
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
                    playerData.setString("particle", "Spell");
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
                    playerData.setString("particle", "Cloud");
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
                    playerData.setString("particle", "Angry");
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
                    playerData.setString("particle", "Notes");
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
                    playerData.setString("particle", "Magic");
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
                    playerData.setString("particle", "Dizzy");
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
        playerData.savePlayerData(this.plugin.organization.uuidConverter.get(p.getName()));
    }

    public void cubeEffect() {
        Collection<? extends Player> onlinePlayers;
        for (int length = (onlinePlayers = this.plugin.getServer().getOnlinePlayers()).size(), k = 0; k < length; ++k) {
            final Player p = (Player) onlinePlayers.toArray()[k];
            if (this.plugin.organization.uuidConverter.has(p.getName())) {
                PlayerDataManager playerData = new PlayerDataManager(this.plugin, this.plugin.organization.uuidConverter.get(p.getName()));
                final String effect = playerData.getString("particle");
                for (final Slime cube : this.plugin.cubes) {
                    if (cube != null) {
                        if (!cube.isDead()) {
                            final Location loc = cube.getLocation();
                            switch (effect) {
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
}
