package mc.rysty.heliosphereranks.player;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereranks.HelioSphereRanks;

public class DisplayName implements Listener {

	private static HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	private static FileConfiguration config = plugin.getConfig();

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		setDisplayName(p);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		setDisplayName(p);
	}

	public static void setDisplayName(Player player) {
		UUID playerId = player.getUniqueId();
		String playerName = player.getName();

		// nickname only
		if (config.getString("Players." + playerId + ".nickname") != null) {
			player.setDisplayName(config.getString("Players." + playerId + ".nickname"));
		}
		// check for group
		if (config.getString("Players." + playerId + ".group") != null) {
			String playerGroup = config.getString("Players." + playerId + ".group");

			// group prefix only
			if (config.getString("Groups." + playerGroup + ".prefix") != null) {
				String groupPrefix = config.getString("Groups." + playerGroup + ".prefix").replaceAll("&", "§") + "";

				player.setDisplayName(groupPrefix + playerName);
				// group prefix and nickname
				if (config.getString("Players." + playerId + ".nickname") != null) {
					String nickname = config.getString("Players." + playerId + ".nickname");

					player.setDisplayName(groupPrefix + nickname);
				}
			}
			// personal prefix only
			if (config.getString("Players." + playerId + ".prefix") != null) {
				String personalPrefix = config.getString("Players." + playerId + ".prefix").replaceAll("&", "§") + "";

				player.setDisplayName(personalPrefix + playerName);
				// personal prefix and nickname
				if (config.getString("Players." + playerId + ".nickname") != null) {
					String nickname = config.getString("Players." + playerId + ".nickname");

					player.setDisplayName(personalPrefix + nickname);
				}
			}
		}
	}
}
