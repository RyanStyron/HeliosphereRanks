package mc.rysty.heliosphereranks.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.setup.Setup;
import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class SetGroup implements CommandExecutor, TabCompleter {

	private PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private FileConfiguration playersFile = playersFileManager.getData();
	private GroupsFileManager groupsFileManager = GroupsFileManager.getInstance();
	private FileConfiguration groupsFile = groupsFileManager.getData();

	public SetGroup(HelioSphereRanks plugin) {
		plugin.getCommand("setgroup").setExecutor(this);
		plugin.getCommand("setgroup").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("setgroup")) {
			if (sender.hasPermission("hs.setgroup")) {
				if (args.length != 2)
					MessageUtils.configStringMessage(sender, "setgroup.argument-error");
				else {
					Player target = Bukkit.getPlayer(args[0]);

					if (target == null)
						MessageUtils.invalidPlayerMessage(sender);
					else {
						UUID targetId = target.getUniqueId();
						String group = args[1].toLowerCase();

						if (groupsFile.getString("Groups." + group) != null) {
							if (!playersFile.getString("Players." + targetId + ".group").equals(group)) {
								playersFile.set("Players." + targetId + ".group", group);
								playersFileManager.saveData();

								Setup.setupPermissions(target);

								String groupPrefix = "";

								if (groupsFile.getString("Groups." + group + ".prefix") != null)
									groupPrefix = groupsFile.getString("Groups." + group + ".prefix");

								if (sender.getName() != target.getName())
									MessageUtils.configStringMessage(target, "setgroup.group-set-target-message",
											"<group>", groupPrefix + group);
								MessageUtils.configStringMessage(sender, "setgroup.group-set-message", "<player>",
										target.getDisplayName(), "<group>", groupPrefix + group);
							} else
								MessageUtils.configStringMessage(sender, "setgroup.group-already-set-error");
						} else
							MessageUtils.configStringMessage(sender, "setgroup.group-does-not-exist-error");
					}
				}
			} else
				MessageUtils.noPermissionMessage(sender);
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 2) {
			List<String> groups = new ArrayList<>();

			if (groupsFile.getConfigurationSection("Groups") != null) {
				for (String key : groupsFile.getConfigurationSection("Groups").getKeys(false))
					groups.add(key);
				return groups;
			}
		}
		return null;
	}
}