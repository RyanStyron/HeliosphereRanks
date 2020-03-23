package mc.rysty.heliosphereranks.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class GroupsFileManager {

    private GroupsFileManager() {
    }

    static GroupsFileManager instance = new GroupsFileManager();

    public static GroupsFileManager getInstance() {
        return instance;
    }

    private Plugin plugin;

    private FileConfiguration config;
    private File configFile;

    private FileConfiguration data;
    private File dataFile;

    private Logger serverLogger = Bukkit.getServer().getLogger();

	public void setup(Plugin plugin) {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		config = plugin.getConfig();

		if (!plugin.getDataFolder().exists()) 
			plugin.getDataFolder().mkdir();

		dataFile = new File(plugin.getDataFolder(), "groups.yml");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				serverLogger.severe(ChatColor.RED + "Could not create groups.yml!");
			}
		}
		data = YamlConfiguration.loadConfiguration(dataFile);
	}

	public FileConfiguration getData() {
		return data;
	}

	public void saveData() {
		try {
			data.save(dataFile);
		} catch (IOException e) {
			serverLogger.severe(ChatColor.RED + "Could not save groups.yml!");
		}
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dataFile);
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			serverLogger.severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public PluginDescriptionFile getDesc() {
		return plugin.getDescription();
	}
}