package mc.rysty.heliosphereranks.player;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class SetDefaultGroup implements Listener {

	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID playerId = player.getUniqueId();

		if (playersFile.getString("Players." + playerId + ".group") == null) {
			playersFile.set("Players." + playerId + ".group", "user");
			playersFileManager.saveData();
		}
	}
}
