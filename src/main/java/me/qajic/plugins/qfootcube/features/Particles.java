package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.utils.Particle;
import me.qajic.plugins.qfootcube.utils.PlayerDataManager;
import org.bukkit.Location;

import java.util.*;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
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
        Map<String, EnumParticle> particleMap = createParticleMap();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            PlayerDataManager playerData = new PlayerDataManager(plugin, uuid);
            String effect = playerData.getString("particle");

            if (!effect.isEmpty() && particleMap.containsKey(effect)) {
                EnumParticle particleEnum = particleMap.get(effect);

                Iterator<Slime> iterator = plugin.cubes.iterator();
                while (iterator.hasNext()) {
                    Slime cube = iterator.next();
                    if (!cube.isDead()) {
                        Location loc = cube.getLocation();
                        Particle particle = new Particle(particleEnum, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
                        particle.toPlayer(player);
                    }
                }
            }
        }
    }

    private Map<String, EnumParticle> createParticleMap() {
        Map<String, EnumParticle> particleMap = new HashMap<>();

        particleMap.put("Hearts", EnumParticle.HEART);
        particleMap.put("Sparky", EnumParticle.FIREWORKS_SPARK);
        particleMap.put("Red", EnumParticle.REDSTONE);
        particleMap.put("Green", EnumParticle.VILLAGER_HAPPY);
        particleMap.put("Flames", EnumParticle.FLAME);
        particleMap.put("Portal", EnumParticle.PORTAL);
        particleMap.put("Spell", EnumParticle.CLOUD);
        particleMap.put("Cloud", EnumParticle.FIREWORKS_SPARK);
        particleMap.put("Flakes", EnumParticle.SNOWBALL);
        particleMap.put("Angry", EnumParticle.VILLAGER_ANGRY);
        particleMap.put("Notes", EnumParticle.NOTE);
        particleMap.put("Magic", EnumParticle.SPELL_WITCH);
        particleMap.put("Dizzy", EnumParticle.SPELL_MOB);

        return particleMap;
    }
}
