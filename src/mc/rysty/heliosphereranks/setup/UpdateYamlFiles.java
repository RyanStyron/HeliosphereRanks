package mc.rysty.heliosphereranks.setup;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class UpdateYamlFiles implements Listener {

	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		updateUsername(player);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		updateUsername(player);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		updateUsername(player);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		updateUsername(player);
	}

	/**
	 * This function updates a player's username and displayname in data.yml if they
	 * join for the first time, or after changing it.
	 */
	private void updateUsername(Player player) {
		String playerName = player.getName();
		String displayName = player.getDisplayName();
		UUID playerId = player.getUniqueId();

		if (playersFile.getString("Players." + playerId + ".username") != playerName) {
			playersFile.set("Players." + playerId + ".username", playerName);
			playersFileManager.saveData();
		}

		if (playersFile.getString("Players." + playerId + ".displayname") != displayName) {
			playersFile.set("Players." + playerId + ".displayname", displayName);
			playersFileManager.saveData();
		}
	}
}
