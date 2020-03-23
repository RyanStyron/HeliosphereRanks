package mc.rysty.heliosphereranks.player;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class DisplayName implements Listener {

	private GroupsFileManager groupsFileManager = GroupsFileManager.getInstance();
	private FileConfiguration groupsFile = groupsFileManager.getData();
	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		setDisplayName(player);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		setDisplayName(player);
	}

	private void setDisplayName(Player player) {
		UUID playerId = player.getUniqueId();
		String playerName = player.getName();

		// Set the player's display name to only their nickname.
		if (playersFile.getString("Players." + playerId + ".nickname") != null) {
			player.setDisplayName(playersFile.getString("Players." + playerId + ".nickname"));
		}

		// Check for if the player is a member of a group.
		if (playersFile.getString("Players." + playerId + ".group") != null) {
			String playerGroup = playersFile.getString("Players." + playerId + ".group");

			// Set the player's display name to only their group's prefix if it has one.
			if (groupsFile.getString("Groups." + playerGroup + ".prefix") != null) {
				String groupPrefix = groupsFile.getString("Groups." + playerGroup + ".prefix").replaceAll("&", "§");

				player.setDisplayName(groupPrefix + playerName);

				// Set the player's display name to their group's prefix and nickname if they.
				if (playersFile.getString("Players." + playerId + ".nickname") != null) {
					String nickname = playersFile.getString("Players." + playerId + ".nickname");

					player.setDisplayName(groupPrefix + nickname);
				}
			}
		}

		// Set the player's display name to their personal prefix if they have one.
		if (playersFile.getString("Players." + playerId + ".prefix") != null) {
			String personalPrefix = playersFile.getString("Players." + playerId + ".prefix").replaceAll("&", "§");

			player.setDisplayName(personalPrefix + playerName);
			// Set the player's display name to their personal prefix and nickname.
			if (playersFile.getString("Players." + playerId + ".nickname") != null) {
				String nickname = playersFile.getString("Players." + playerId + ".nickname");

				player.setDisplayName(personalPrefix + nickname);
			}
		}
	}
}
