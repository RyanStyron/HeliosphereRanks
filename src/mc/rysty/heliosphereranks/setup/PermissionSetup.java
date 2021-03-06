package mc.rysty.heliosphereranks.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.ListUtils;

public class PermissionSetup {

	private static HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	private static FileConfiguration playersFile = HelioSphereRanks.getPlayersFile().getData();
	private static FileConfiguration groupsFile = HelioSphereRanks.getGroupsFile().getData();

	private static HashMap<UUID, PermissionAttachment> playerPermissions = HelioSphereRanks.permissionsMap;

	public static void setupPermissions(Player player) {
		UUID playerId = player.getUniqueId();

		if (playerPermissions.get(playerId) != null)
			playerPermissions.remove(playerId);
		PermissionAttachment attachment = player.addAttachment(plugin);

		playerPermissions.put(playerId, attachment);

		permissionsSetter(playerId);
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

				List<String> permissionsList = new ArrayList<>();

				for (String playerPermissions : groupsFile.getStringList("Groups." + playerGroup + ".permissions")) {
					if (playerPermissions != null) {
						attachment.setPermission(playerPermissions, true);
						permissionsList.add(playerPermissions);
					} else
						System.out.println("HS-Ranks: " + playerName + "'s group has no permissions.");
				}
				System.out.println(playerName + "'s Permissions: " + ListUtils.fromList(permissionsList, false, false));
			} else
				System.out.println("HS-Ranks: " + playerName + "'s group is not valid.");
		} else
			System.out.println("HS-Ranks: " + playerName + " has no group.");
	}
}
