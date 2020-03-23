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

public class Prefix implements CommandExecutor {

	HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	FileConfiguration config = plugin.getConfig();

	public Prefix(HelioSphereRanks plugin) {
		plugin.getCommand("prefix").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("prefix")) {
			if (sender.hasPermission("hs.prefix")) {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "�� " + ChatColor.RED
							+ "Not enough arguments were provided! Correct format: /prefix <player> <prefix>");
					return false;
				} else if (args.length == 2) {
					Player p = Bukkit.getPlayer(args[0]);

					if (p == null) {
						sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "�� " + ChatColor.RED
								+ "Could not find the specified player");
						return false;
					}
					UUID playerId = p.getUniqueId();

					if (config.getString("Players." + playerId + ".prefix") == null) {
						config.createSection("Players." + playerId + ".prefix");
					}
					config.set("Players." + playerId + ".prefix", args[1]);
					plugin.saveConfig();
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "�� " + ChatColor.GRAY + p.getName()
							+ "'s prefix has been set to " + args[1].replaceAll("&", "�"));
				} else if (args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "�� " + ChatColor.RED
							+ "Not enough arguments were provided! Correct format: /prefix <player> <prefix>");
				} else if (args.length > 2) {
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "�� " + ChatColor.RED
							+ "Too many arguments were provided! Correct format: /prefix <player> <prefix>");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "�� " + ChatColor.RED
						+ "You do not have permission to do this");
			}
		}
		return false;
	}

}
