package mc.rysty.heliosphereranks.utils.filemanagers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class PlayersFileManager {

	private static PlayersFileManager instance = new PlayersFileManager();

	public static PlayersFileManager getInstance() {
		return instance;
	}

	private FileConfiguration data;
    private File dataFile;
    
    private Logger serverLogger = Bukkit.getServer().getLogger();

	public void setup(Plugin plugin) {
		if (!plugin.getDataFolder().exists()) 
			plugin.getDataFolder().mkdir();
		dataFile = new File(plugin.getDataFolder(), "players.yml");

		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				serverLogger.severe(ChatColor.RED + "Could not create players.yml!");
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
			serverLogger.severe(ChatColor.RED + "Could not save players.yml!");
		}
	}

	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
}