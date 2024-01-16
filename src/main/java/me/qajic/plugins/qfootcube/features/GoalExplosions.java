package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.utils.PlayerDataManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GoalExplosions implements Listener {

    private final Footcube plugin;
    public String pluginString;

    public GoalExplosions(final Footcube instance) {
        this.plugin = instance;
        this.pluginString = ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("prefix"));
    }

    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        PlayerDataManager playerData = new PlayerDataManager(this.plugin, this.plugin.organization.uuidConverter.get(p.getName()));
        String explosion = "";
        if (e.getCurrentItem().hasItemMeta()) {
            explosion = e.getCurrentItem().getItemMeta().getDisplayName();
        }
        if (!e.getInventory().getName().equalsIgnoreCase("§6§lGoal §e§lExplosions §fMenu")) {
            return;
        }
        e.setCancelled(true);
        switch (explosion) {
            case " §aDefault": {
                playerData.setString("goal_explosion", "Default");
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                p.closeInventory();

                break;
            }
            case " §7Helix": {
                if(p.hasPermission("footcube.goalexplosions.helix") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "Helix");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §cMeteor": {
                if(p.hasPermission("footcube.goalexplosions.meteor") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "Meteor");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §6Poo": {
                if(p.hasPermission("footcube.goalexplosions.poo") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "Poo");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §cSerbia": {
                if(p.hasPermission("footcube.goalexplosions.serbia") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "Serbia");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §6Spain": {
                if(p.hasPermission("footcube.goalexplosions.spain") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "Spain");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §fK§1O§fK": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "kok") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "kok");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §bS§eK§cL": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "skl") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "skl");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §eK§0E§eL": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "kel") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "kel");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §9F§fL§9S": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "fls") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "fls");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §4F§1K§4S": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "fks") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "fks");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §1F§fK§6K": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "fkk") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "fkk");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §6M§8K§6U": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "mku") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "mku");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §9S§cP§fA": {
                if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "spa") || p.hasPermission("footcube.goalexplosions.all")) {
                    playerData.setString("goal_explosion", "spa");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §cDisable": {
                playerData.setString("goal_explosion", "Disable");
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("deactivatedExplosion")));
                p.closeInventory();

                break;
            }
            default:
                break;
        }
        playerData.savePlayerData(this.plugin.organization.uuidConverter.get(p.getName()));
    }

}