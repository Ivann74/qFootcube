package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.core.Organization;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import org.bukkit.event.Listener;

@SuppressWarnings("ALL")
public class DisableCommands implements Listener
{
    private Footcube plugin;
    private Organization organization;
    public ArrayList<String> commands;
    
    public DisableCommands(final Footcube pl, final Organization org) {
        this.commands = new ArrayList<String>();
        this.plugin = pl;
        this.organization = org;
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        final FileConfiguration cfg = this.plugin.getConfig();
        cfg.addDefault("enabledCommands", (Object)"");
        this.plugin.saveConfig();
        String[] split;
        for (int length = (split = cfg.getString("enabledCommands").split(" ")).length, i = 0; i < length; ++i) {
            final String s = split[i];
            this.commands.add(s);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreprocess(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        if (!p.hasPermission("footcube.admin") && (this.organization.playingPlayers.contains(p.getName()) || this.organization.waitingPlayers.containsKey(p.getName()))) {
            final String cmd = e.getMessage().substring(1).split(" ")[0];
            boolean allowed = false;
            for (final String command : this.commands) {
                if (cmd.equalsIgnoreCase(command)) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                p.sendMessage(this.organization.pluginString + "You can't use that command in the match.");
                e.setCancelled(true);
            }
        }
    }
}
