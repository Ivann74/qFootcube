package me.qajic.plugins.qfootcube.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;

@SuppressWarnings("ALL")
public class PapiConfig
{
    private static File fajl;
    private static FileConfiguration kurcina;

    public static void create() {
        PapiConfig.fajl = new File("plugins" + File.separator + "qFootcube" + File.separator + "papiConfig.yml");
        if (!PapiConfig.fajl.exists()) {
            try {
                PapiConfig.fajl.createNewFile();
            }
            catch (IOException ex) {}
        }
        PapiConfig.kurcina = (FileConfiguration)YamlConfiguration.loadConfiguration(PapiConfig.fajl);
    }

    public static FileConfiguration get() {
        return PapiConfig.kurcina;
    }

    public static void save() {
        try {
            PapiConfig.kurcina.save(PapiConfig.fajl);
        }
        catch (IOException e) {
            System.out.println("Kurcina bato (34. linija papiConfig klase)");
        }
    }

    public static void reload() {
        PapiConfig.kurcina = (FileConfiguration)YamlConfiguration.loadConfiguration(PapiConfig.fajl);
    }
}
