package me.qajic.plugins.qfootcube.managers;

import lombok.Getter;
import me.qajic.plugins.qfootcube.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

@Getter
public class UtilManager {
  private final Plugin plugin;
  private Logger logger;

  public UtilManager(final Plugin plugin) {
    this.plugin = plugin;
    this.logger = new Logger(plugin);
  }

  public void reload() {
    this.logger = new Logger(plugin);
  }

  public String color(final String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }
}
