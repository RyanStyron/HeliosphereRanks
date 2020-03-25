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

public class Nickname implements CommandExecutor {

	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();

	public Nickname(HelioSphereRanks plugin) {
		plugin.getCommand("nickname").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("nickname")) {
			if (sender.hasPermission("hs.nickname")) {
				String senderName = sender.getName();
				Player target = null;
				String nickname = "";

				if (args.length == 0) {
					MessageUtils.message(sender,
							"&4&l(!)&c Not enough arguments were provided. Usage: /nickname [player] <nickname>");
					return false;
				} else if (args.length == 1) {
					if (sender instanceof Player) {
						target = (Player) sender;
						nickname = args[0];
					} else {
						MessageUtils.message(sender,
								"&4&l(!)&c Not enough arguments were provided. Usage: /nickname [player] <nickname>");
						return false;
					}
				} else if (args.length == 2) {
					target = Bukkit.getPlayer(args[0]);
					nickname = args[1];

					if (!sender.hasPermission("hs.nickname.other")) {
						if (target != null) {
							if (target.getName() != senderName) {
								target = null;
								nickname = "";
								MessageUtils.message(sender,
										"&cYou do not have permission to nickname other players. Usage: /nickname <nickname>");
								return false;
							}
						} else {
							MessageUtils.message(sender, "&4&l(!)&c You need to provide a valid player.");
							return false;
						}
					}
				} else {
					MessageUtils.message(sender, "&4&l(!)&c Too many arguments were provided.");
					return false;
				}

				if (target == null) {
					MessageUtils.message(sender, "&4&l(!)&c You need to provide a valid player.");
				} else {
					UUID targetId = target.getUniqueId();
					String targetName = target.getName();

					if (nickname.equalsIgnoreCase("reset")) {
						playersFile.set("Players." + targetId + ".nickname", null);
						MessageUtils.message(sender, "&6&l(!)&e The nickname of " + targetName + " &ehas been reset.");
					} else {
						playersFile.set("Players." + targetId + ".nickname", nickname);
						MessageUtils.message(sender, "&6&l(!)&e The nickname of " + targetName
								+ " &ehas been to set to " + nickname + "&e.");
					}
					playersFileManager.saveData();

					if (targetName != senderName) {
						if (playersFile.getString("Players." + targetId + ".nickname") != null)
							MessageUtils.message(target, "&6&l(!) &eYour nickname has been set to " + nickname + "&e.");
						else
							MessageUtils.message(target, "&6&l(!) &eYour nickname has been reset.");
					}
				}
			} else {
				MessageUtils.message(sender, "&4&l(!)&c You do not have permission to do this.");
			}
		}
		return false;
	}
}
