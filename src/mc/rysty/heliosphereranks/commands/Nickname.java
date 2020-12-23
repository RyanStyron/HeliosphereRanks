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
import mc.rysty.heliosphereranks.utils.filemanagers.PlayersFileManager;

public class Nickname implements CommandExecutor {

	private PlayersFileManager playersFileManager = HelioSphereRanks.getPlayersFile();
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
					MessageUtils.configStringMessage(sender, "nickname.argument-error");
					return false;
				} else if (args.length == 1) {
					if (sender instanceof Player) {
						target = (Player) sender;
						nickname = args[0];
					} else {
						MessageUtils.configStringMessage(sender, "nickname.argument-error");
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
								MessageUtils.configStringMessage(sender, "nickname.permission-error");
								return false;
							}
						} else {
							MessageUtils.invalidPlayerMessage(sender);
							return false;
						}
					}
				} else {
					MessageUtils.configStringMessage(sender, "nickname.argument-error");
					return false;
				}

				if (target == null) {
					MessageUtils.invalidPlayerMessage(sender);
				} else {
					UUID targetId = target.getUniqueId();
					String targetName = target.getName();

					if (nickname.equalsIgnoreCase("reset")) {
						playersFile.set("Players." + targetId + ".nickname", null);
						MessageUtils.configStringMessage(sender, "nickname.nickname-reset-message", "<player>",
								targetName);
					} else {
						playersFile.set("Players." + targetId + ".nickname", nickname);
						MessageUtils.configStringMessage(sender, "nickname.nickname-set-message", "<player>",
								targetName, "<nickname>", nickname);
					}
					playersFileManager.saveData();

					if (targetName != senderName) {
						if (playersFile.getString("Players." + targetId + ".nickname") != null)
							MessageUtils.configStringMessage(target, "nickname.nickname-set-target-message",
									"<nickname>", nickname);
						else
							MessageUtils.configStringMessage(target, "nickname.nickname-reset-target-message");
					}
				}
			} else {
				MessageUtils.noPermissionMessage(sender);
			}
		}
		return false;
	}
}
