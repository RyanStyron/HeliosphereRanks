package mc.rysty.heliosphereranks.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class Prefix implements CommandExecutor {

	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();

	public Prefix(HelioSphereRanks plugin) {
		plugin.getCommand("prefix").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("prefix")) {
			if (sender.hasPermission("hs.prefix")) {
				String senderName = sender.getName();
				Player target = null;
				String prefix = "";

				if (args.length == 0) {
					MessageUtils.message(sender,
							"&4&l(!)&c Not enough arguments were provided! Correct format: /prefix [player] <prefix>");
					return false;
				} else if (args.length == 1) {
					if (sender instanceof Player) {
						target = (Player) sender;
						prefix = args[0];
					} else {
						MessageUtils.message(sender,
								"&4&l(!)&c Not enough arguments were provided! Correct format: /prefix [player] <prefix>");
					}
				} else if (args.length == 2) {
					target = Bukkit.getPlayer(args[0]);
					prefix = args[1];

					if (!sender.hasPermission("hs.prefix.other")) {
						if (target != null) {
							if (target.getName() != senderName) {
								target = null;
								prefix = "";
								MessageUtils.message(sender,
										"&cYou do not have permission to edit the prefix of other players. Usage: /prefix <prefix>");
								return false;
							}
						} else {
							MessageUtils.message(sender, "&4&l(!)&c You need to provide a valid player.");
							return false;
						}
					}
				} else if (args.length > 2) {
					MessageUtils.message(sender,
							"&4&l(!)&c Too many arguments were provided! Correct format: /prefix [player] <prefix>");
					return false;
				}

				if (target == null) {
					MessageUtils.message(sender, "&4&l(!)&c You need to provide a valid player.");
				} else {
					prefix = prefix.replaceAll("&", "§");
					UUID targetId = target.getUniqueId();
					String targetName = target.getName();

					if (prefix != "reset") {
						playersFile.set("Players." + targetId + ".prefix", prefix);
						playersFileManager.saveData();
					} else {
						MessageUtils.message(sender, "&4&l(!)&c Please provide a valid prefix!");
						return false;
					}

					if (targetName != senderName) {
						MessageUtils.message(target, "&6&l(!) &eYour prefix has been set to " + prefix + "&e.");
					}
					MessageUtils.message(sender,
							"&6&l(!)&e " + target.getName() + "&e's prefix has been set to " + prefix + "&e.");
				}
			} else {
				MessageUtils.message(sender, "&4&l(!)&c You do not have permission to execute this command.");
			}
		}
		return false;
	}
}
