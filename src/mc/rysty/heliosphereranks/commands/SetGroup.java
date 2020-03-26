package mc.rysty.heliosphereranks.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.setup.Setup;
import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class SetGroup implements CommandExecutor {

	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();
	private GroupsFileManager groupsFileManager = GroupsFileManager.getInstance();
	private FileConfiguration groupsFile = groupsFileManager.getData();

	public SetGroup(HelioSphereRanks plugin) {
		plugin.getCommand("setgroup").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("setgroup")) {
			if (sender.hasPermission("hs.setgroup")) {
				if (args.length == 0 || args.length == 1) {
					MessageUtils.message(sender,
							"&4&l(!)&c Not enough arguments were provided! Correct usage: /setgroup <player> <group>");
				} else if (args.length == 2) {
					Player target = Bukkit.getPlayer(args[0]);

					if (target == null) {
						MessageUtils.invalidPlayerMessage(sender);
					} else {
						UUID targetId = target.getUniqueId();
						String group = args[1].toLowerCase();

						if (groupsFile.getString("Groups." + group) != null) {
							if (playersFile.getString("Players." + targetId + ".group") != group) {
								playersFile.set("Players." + targetId + ".group", group);
								playersFileManager.saveData();

								Setup.setupPermissions(target);

								String groupPrefix = "";

								if (groupsFile.getString("Groups." + group + ".prefix") != null)
									groupPrefix = groupsFile.getString("Groups." + group + ".prefix");

								if (sender.getName() != target.getName()) {
									MessageUtils.message(target,
											"&6&l(!)&e Your group has been set to&b " + groupPrefix + group + "&e.");
								}
								MessageUtils.message(sender, "&6&l(!)&e The group of " + target.getDisplayName()
										+ " &ehas been set to&b " + groupPrefix + group + "&e.");
							} else {
								MessageUtils.message(sender, "&4&l(!)&c The player is already a member of this group.");
							}
						} else {
							MessageUtils.message(sender, "&4&l(!)&c The group provided does not exist.");
						}
					}
				} else if (args.length > 2) {
					MessageUtils.message(sender,
							"&4&l(!)&c Too many arguments were provided! Correct usage: /setgroup <player> <group>");
				}
			} else {
				MessageUtils.noPermissionMessage(sender);
			}
		}
		return false;
	}
}