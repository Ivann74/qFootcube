package me.qajic.plugins.qfootcube.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class PlayerDataManager {
    private final Plugin plugin;
    private File file;
    private FileConfiguration data;

    public PlayerDataManager(final Plugin plugin, final UUID playerID) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder() + File.separator + "playerdata", playerID + ".yml");
        savePlayerData(playerID);
        this.data=YamlConfiguration.loadConfiguration(this.file);
    }

    public void reloadPlayerData(final UUID playerID) {
        if (this.file == null)
            this.file = new File(this.plugin.getDataFolder(), playerID + ".yml");
        this.data=YamlConfiguration.loadConfiguration(this.file);
    }

    public int getInt(final String path) {
        return this.data.getInt(path, 0);
    }

    public void setInt(final String path, final int value) {
        this.data.set(path, value);
    }

    public void riseInt(final String path) {
        this.data.set(path, getInt(path) + 1);
    }

    public String getString(final String path) {
        return this.data.getString(path, "");
    }

    public void setString(final String path, final String value) {
        this.data.set(path, value);
    }

    public FileConfiguration getPlayerData(final UUID playerID) {
        if (this.data == null) reloadPlayerData(playerID);
        return this.data;
    }

    public void savePlayerData(final UUID playerID) {
        try {
            getPlayerData(playerID).save(this.file);
        } catch (final IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save file to " + this.file, exception);
        }
    }
}
