package mc.rysty.heliosphereranks.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import mc.rysty.heliosphereranks.HelioSphereRanks;

public class SetGroup implements CommandExecutor {

	HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	FileConfiguration config = plugin.getConfig();

	public SetGroup(HelioSphereRanks plugin) {
		plugin.getCommand("setgroup").setExecutor(this);
	}

	HashMap<UUID, PermissionAttachment> pP = HelioSphereRanks.playerPermissions;
	ChatColor aqua = ChatColor.AQUA;
	ChatColor darkAqua = ChatColor.DARK_AQUA;
	ChatColor gray = ChatColor.GRAY;
	ChatColor red = ChatColor.RED;
	ChatColor bold = ChatColor.BOLD;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("setgroup")) {
			if (sender.hasPermission("hs.setgroups")) {
				if (args.length == 1) {
					sender.sendMessage(aqua + "" + bold + "›› " + red
							+ "Not enough arguments were provided! Correct format: /setgroup <player> <group>");
					return false;
				} else if (args.length == 2) {
					Player player = Bukkit.getPlayer(args[0]);

					if (player == null) {
						sender.sendMessage(aqua + "" + bold + "›› " + red + "Could not find the specified player");
						return false;
					}
					UUID playerId = player.getUniqueId();
					String rank = args[1];

					if (config.getString("Groups." + rank) != null) {
						if (config.getString("Players." + playerId + ".group") != rank) {
							config.set("Players." + playerId + ".group", rank);
							sender.sendMessage(aqua + "" + bold + "›› " + gray + player.getDisplayName()
									+ "'s group has been set to " + darkAqua + rank);
							plugin.saveConfig();
							setupPermissions(player);
						} else {
							sender.sendMessage(
									aqua + "" + bold + "›› " + red + "The player is already a member of this group!");
						}
					} else {
						sender.sendMessage(aqua + "" + bold + "›› " + red
								+ "The group provided does not exist (case sensitivity is required)");
					}
				} else if (args.length == 0) {
					sender.sendMessage(aqua + "" + bold + "›› " + red
							+ "Not enough arguments were provided! Correct format: /setgroup <player> <group>");
				} else if (args.length > 2) {
					sender.sendMessage(aqua + "" + bold + "›› " + red
							+ "Too many arguments were provided! Correct format: /setgroup <player> <group>");
				}
			} else {
				sender.sendMessage(aqua + "" + bold + "›› " + red + "You do not have permission to do this");
			}
		}
		return false;
	}

	public void setupPermissions(Player player) {
		PermissionAttachment attachment = player.addAttachment(HelioSphereRanks.getInstance());
		pP.put(player.getUniqueId(), attachment);
		permissionsSetter(player.getUniqueId());
	}

	private void permissionsSetter(UUID uuid) {
		PermissionAttachment attachment = pP.get(uuid);
		UUID playerId = Bukkit.getPlayer(uuid).getUniqueId();
		String playerName = Bukkit.getPlayer(uuid).getName();

		if (config.getString("Players." + playerId + ".group") != null) {
			String playerGroup = config.getString("Players." + playerId + ".group");

			if (config.getString("Groups." + playerGroup) != null) {
				System.out.println("HS-Ranks: " + playerName + " has a group that exists.");
				System.out.println("HS-Ranks: " + playerName + " is a member of group " + playerGroup + ".");

				for (String permissions : config.getStringList("Groups." + playerGroup + ".permissions")) {
					if (permissions != null) {
						System.out.println(playerName + "'s Permissions: " + permissions);
						attachment.setPermission(permissions, true);
					} else {
						System.out.println("HS-Ranks: " + playerName + "'s group has no permissions.");
					}
				}

			} else {
				System.out.println("HS-Ranks: " + playerName + "'s group is not valid.");
			}

		} else {
			System.out.println("HS-Ranks: " + playerName + " is not a member of a group.");
		}
	}
}