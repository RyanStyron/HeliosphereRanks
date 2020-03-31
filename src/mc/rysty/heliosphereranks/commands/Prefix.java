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
					MessageUtils.configStringMessage(sender, "prefix.argument-error");
					return false;
				} else if (args.length == 1) {
					if (sender instanceof Player) {
						target = (Player) sender;
						prefix = args[0];
					} else {
						MessageUtils.configStringMessage(sender, "prefix.argument-error");
					}
				} else if (args.length == 2) {
					target = Bukkit.getPlayer(args[0]);
					prefix = args[1];

					if (!sender.hasPermission("hs.prefix.other")) {
						if (target != null) {
							if (target.getName() != senderName) {
								target = null;
								prefix = "";
								MessageUtils.configStringMessage(sender, "prefix.permission-error");
								return false;
							}
						} else {
							MessageUtils.invalidPlayerMessage(sender);
							return false;
						}
					}
				} else if (args.length > 2) {
					MessageUtils.configStringMessage(sender, "prefix.argument-error");
					return false;
				}

				if (target == null) {
					MessageUtils.invalidPlayerMessage(sender);
				} else {
					UUID targetId = target.getUniqueId();
					String targetName = target.getName();

					if (!prefix.equalsIgnoreCase("reset")) {
						playersFile.set("Players." + targetId + ".prefix", prefix);
						playersFileManager.saveData();
					} else {
						MessageUtils.configStringMessage(sender, "prefix.invalid-prefix-error");
						return false;
					}

					if (targetName != senderName) {
						MessageUtils.configStringMessage(target, "prefix.prefix-set-target-message");
					}
					MessageUtils.configStringMessage(sender, "prefix.prefix-set-message", "<player>", targetName);
				}
			} else {
				MessageUtils.noPermissionMessage(sender);
			}
		}
		return false;
	}
}
