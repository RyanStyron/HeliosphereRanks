package mc.rysty.heliosphereranks.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.setup.Setup;

public class SetGroup implements CommandExecutor {

	private HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public SetGroup(HelioSphereRanks plugin) {
		plugin.getCommand("setgroup").setExecutor(this);
	}

	private ChatColor aqua = ChatColor.AQUA;
	private ChatColor darkAqua = ChatColor.DARK_AQUA;
	private ChatColor gray = ChatColor.GRAY;
	private ChatColor red = ChatColor.RED;
	private ChatColor bold = ChatColor.BOLD;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("setgroup")) {
			if (sender.hasPermission("hs.setgroups")) {
				if (args.length == 1) {
					sender.sendMessage(aqua + "" + bold + ">> " + red
							+ "Not enough arguments were provided! Correct format: /setgroup <player> <group>");
					return false;
				} else if (args.length == 2) {
					Player player = Bukkit.getPlayer(args[0]);

					if (player == null) {
						sender.sendMessage(aqua + "" + bold + ">> " + red + "Could not find the specified player");
						return false;
					}
					UUID playerId = player.getUniqueId();
					String rank = args[1];

					if (config.getString("Groups." + rank) != null) {
						if (config.getString("Players." + playerId + ".group") != rank) {
							config.set("Players." + playerId + ".group", rank);
							sender.sendMessage(aqua + "" + bold + ">> " + gray + player.getDisplayName()
									+ "'s group has been set to " + darkAqua + rank);
							plugin.saveConfig();
							
							Setup.setupPermissions(player);
						} else {
							sender.sendMessage(
									aqua + "" + bold + ">> " + red + "The player is already a member of this group!");
						}
					} else {
						sender.sendMessage(aqua + "" + bold + "�� " + red
								+ "The group provided does not exist (case sensitivity is required)");
					}
				} else if (args.length == 0) {
					sender.sendMessage(aqua + "" + bold + "�� " + red
							+ "Not enough arguments were provided! Correct format: /setgroup <player> <group>");
				} else if (args.length > 2) {
					sender.sendMessage(aqua + "" + bold + ">> " + red
							+ "Too many arguments were provided! Correct format: /setgroup <player> <group>");
				}
			} else {
				sender.sendMessage(aqua + "" + bold + ">> " + red + "You do not have permission to do this");
			}
		}
		return false;
	}
}