package me.qajic.plugins.qfootcube.configuration;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public enum Lang {
  ACTIVATED_PARTICLE("particles.activated", "&a▎ &fTrag lopte \"{0}\" aktiviran!"),
  DISABLED_PARTICLES("particles.disabled", "&a▎ &fIsključen trag lopte."),
  ACTIVATED_GOALEXPLOSION("goalexplosions.activated", "&a▎ &fGol eksplozija \"{0}\" aktivirana!"),
  DISABLED_GOALEXPLOSIONS("goalexplosions.disabled", "&a▎ &fIsključena gol eksplozija."),
  INSUFFICIENT_PERMISSION("insufficient-permission", "&4▎ &cNedovoljno dozvola.");

  private static FileConfiguration LANG;
  private final String path, def;

  Lang(final String path, final String start) {
    this.path = path;
    this.def = start;
  }

  public static void setFile(final FileConfiguration config) {
    LANG = config;
  }

  public String getDefault() {
    return this.def;
  }

  public String getConfigValue(final String[] args) {
    String value = ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));

    if (args == null) return value;
    else {
      if (args.length == 0) return value;
      for (int i = 0; i < args.length; i++) value = value.replace("{" + i + "}", args[i]);
      value = ChatColor.translateAlternateColorCodes('&', value);
    }

    return value;
  }
}
