package mc.rysty.heliosphereranks.player;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereranks.HelioSphereRanks;

public class SetDefaultGroup implements Listener {

	private HelioSphereRanks plugin = HelioSphereRanks.getInstance();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();
		FileConfiguration config = plugin.getConfig();

		if (config.getString("Players." + playerId + ".group") == null) {
			config.createSection("Players." + playerId + ".group");
			config.set("Players." + playerId + ".group", "user");
			plugin.saveConfig();
		}
	}
}
