package mc.rysty.heliosphereranks.setup;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class Setup implements Listener {

	private static HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	private static FileConfiguration playersFile = PlayersFileManager.getInstance().getData();
	private static FileConfiguration groupsFile = GroupsFileManager.getInstance().getData();

	private static HashMap<UUID, PermissionAttachment> playerPermissions = HelioSphereRanks.playerPermissions;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		setupPermissions(player);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		playerPermissions.remove(player.getUniqueId());
	}

	public static void setupPermissions(Player player) {
		if (playerPermissions != null)
			playerPermissions.clear();

		PermissionAttachment attachment = player.addAttachment(plugin);
		playerPermissions.put(player.getUniqueId(), attachment);

		permissionsSetter(player.getUniqueId());
	}

	private static void permissionsSetter(UUID uuid) {
		PermissionAttachment attachment = playerPermissions.get(uuid);
		UUID playerId = Bukkit.getPlayer(uuid).getUniqueId();
		String playerName = Bukkit.getPlayer(uuid).getName();

		if (playersFile.getString("Players." + playerId + ".group") != null) {
			String playerGroup = playersFile.getString("Players." + playerId + ".group");

			if (groupsFile.getString("Groups." + playerGroup) != null) {
				System.out.println("HS-Ranks: " + playerName + " has a group that exists.");
				System.out.println("HS-Ranks: " + playerName + " is a member of group " + playerGroup + ".");

				for (String playerPermissions : groupsFile.getStringList("Groups." + playerGroup + ".permissions")) {
					if (playerPermissions != null) {
						System.out.println(playerName + "'s Permissions: " + playerPermissions);
						attachment.setPermission(playerPermissions, true);
					} else {
						System.out.println("HS-Ranks: " + playerName + "'s group has no permissions.");
					}
				}
			} else {
				System.out.println("HS-Ranks: " + playerName + "'s group is not valid.");
			}
		} else {
			System.out.println("HS-Ranks: " + playerName + " has no group.");
		}
	}
}
