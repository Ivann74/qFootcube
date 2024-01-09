package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
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
                this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Default");
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                p.closeInventory();

                break;
            }
            case " §7Helix": {
                if(p.hasPermission("footcube.goalexplosions.helix") || p.hasPermission("footcube.goalexplosions.all")) {
                    this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Helix");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §cMeteor": {
                if(p.hasPermission("footcube.goalexplosions.meteor") || p.hasPermission("footcube.goalexplosions.all")) {
                    this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Meteor");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §6Poo": {
                if(p.hasPermission("footcube.goalexplosions.poo") || p.hasPermission("footcube.goalexplosions.all")) {
                    this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Poo");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §cSerbia": {
                if(p.hasPermission("footcube.goalexplosions.serbia") || p.hasPermission("footcube.goalexplosions.all")) {
                    this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Serbia");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §6Spain": {
                if(p.hasPermission("footcube.goalexplosions.spain") || p.hasPermission("footcube.goalexplosions.all")) {
                    this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Spain");
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("activatedExplosion").replace("{explosion}", explosion)));
                    p.closeInventory();
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("explosionNoPermission").replace("{explosion}", explosion)));
                }
                break;
            }
            case " §cDisable": {
                this.plugin.organization.db.updateString("players", p.getName(), "goal_explosion", "Disable");
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("deactivatedExplosion")));
                p.closeInventory();

                break;
            }
            default:
                break;
        }
    }

}