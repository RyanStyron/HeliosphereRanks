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

public class Nickname implements CommandExecutor {

	private HelioSphereRanks plugin = HelioSphereRanks.getInstance();
	private FileConfiguration config = plugin.getConfig();

	public Nickname(HelioSphereRanks plugin) {
		plugin.getCommand("nickname").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("nickname")) {
			if (sender.hasPermission("hs.nickname")) {
				if (args.length == 1) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.RED + "An online player must be specifed to nickname!");
						return true;
					}
					Player p = (Player) sender;
					String nick = args[0].replaceAll("&", "§");
					UUID playerId = p.getUniqueId();

					if (args[0].equalsIgnoreCase("reset")) {
						if (config.getString("Players." + playerId + ".nickname") != null) {
							config.set("Players." + playerId + ".nickname", null);
							plugin.saveConfig();
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "(!)" + ChatColor.YELLOW
									+ " Your nickname has been reset.");
						} else {
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "(!)" + ChatColor.RED
									+ " You did not have a nickname to reset!");
						}
					} else {
						p.sendMessage(
								"Your nickname has been set to " + ChatColor.YELLOW + nick + ChatColor.WHITE + ".");
						if (config.getString("Players." + playerId + ".nickname") == null) {
							config.createSection("Players." + playerId + ".nickname");
						}
						config.set("Players." + playerId + ".nickname", nick);
						plugin.saveConfig();
					}
				} else if (args.length == 2) {
					if (sender.hasPermission("hs.nickname.other")) {
						String nick = args[1].replaceAll("&", "§");
						Player t = Bukkit.getPlayer(args[0]);

						if (t == null) {
							sender.sendMessage(ChatColor.RED + "The targeted player is not currently online");
						}
						UUID targetId = t.getUniqueId();

						t.sendMessage(
								"Your nickname has been set to " + ChatColor.YELLOW + nick + ChatColor.WHITE + ".");
						sender.sendMessage("The target's nickname has been set to " + ChatColor.YELLOW + nick
								+ ChatColor.WHITE + ".");
						if (config.getString("Players." + targetId + ".nickname") == null) {
							config.createSection("Players." + targetId + ".nickname");
						}
						config.set("Players." + targetId + ".nickname", nick);
						plugin.saveConfig();
					} else {
						sender.sendMessage(ChatColor.RED + "You do not have permission to do this");
					}
				} else if (args.length == 0) {
					sender.sendMessage(ChatColor.RED
							+ "Not enough arguments were provided! Correct format: /nickname [player] <nickname | reset>");
					return false;
				} else if (args.length > 2) {
					sender.sendMessage(ChatColor.RED
							+ "Too many arguments were provided! Correct format: /nickname [player] <nickname | reset>");
					return false;
				}
			}
		} else {
			sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "›› " + ChatColor.RED
					+ "You do not have permission to do this");
		}
		return false;
	}

}
