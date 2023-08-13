package me.qajic.plugins.qfootcube.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

@SuppressWarnings("ALL")
public class MessagesConfig
{
    private static File fajl;
    private static FileConfiguration kurcina;
    
    public static void create() {
        MessagesConfig.fajl = new File("plugins" + File.separator + "qFootcube" + File.separator + "messages.yml");
        if (!MessagesConfig.fajl.exists()) {
            try {
                MessagesConfig.fajl.createNewFile();
            }
            catch (IOException ex) {}
        }
        MessagesConfig.kurcina = (FileConfiguration)YamlConfiguration.loadConfiguration(MessagesConfig.fajl);
    }
    
    public static FileConfiguration get() {
        return MessagesConfig.kurcina;
    }
    
    public static void save() {
        try {
            MessagesConfig.kurcina.save(MessagesConfig.fajl);
        }
        catch (IOException e) {
            System.out.println("Kurcina bato (34. linija messagesConfig klase)");
        }
    }
    
    public static void reload() {
        MessagesConfig.kurcina = (FileConfiguration)YamlConfiguration.loadConfiguration(MessagesConfig.fajl);
    }
}
