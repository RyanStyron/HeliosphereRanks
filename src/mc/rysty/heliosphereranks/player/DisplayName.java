package mc.rysty.heliosphereranks.player;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.MessageUtils;
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
		String displayName = playerName;

		// Set the player's display name to only their nickname.
		if (playersFile.getString("Players." + playerId + ".nickname") != null) {
			displayName = playersFile.getString("Players." + playerId + ".nickname");
		}

		// Check for if the player is a member of a group.
		if (playersFile.getString("Players." + playerId + ".group") != null) {
			String playerGroup = playersFile.getString("Players." + playerId + ".group");

			// Set the player's display name to only their group's prefix if it has one.
			if (groupsFile.getString("Groups." + playerGroup + ".prefix") != null) {
				String groupPrefix = groupsFile.getString("Groups." + playerGroup + ".prefix");

				displayName = groupPrefix + playerName;

				// Set the player's display name to their group's prefix and nickname.
				if (playersFile.getString("Players." + playerId + ".nickname") != null) {
					String nickname = playersFile.getString("Players." + playerId + ".nickname");

					displayName = groupPrefix + nickname;
				}
			}
		}

		// Set the player's display name to their personal prefix if they have one.
		if (playersFile.getString("Players." + playerId + ".prefix") != null) {
			String personalPrefix = playersFile.getString("Players." + playerId + ".prefix");

			displayName = personalPrefix + playerName;

			// Set the player's display name to their personal prefix and nickname.
			if (playersFile.getString("Players." + playerId + ".nickname") != null) {
				String nickname = playersFile.getString("Players." + playerId + ".nickname");

				displayName = personalPrefix + nickname;
			}
		}

		if (displayName != playerName)
			player.setDisplayName(MessageUtils.convertChatColors(displayName));
	}
}
