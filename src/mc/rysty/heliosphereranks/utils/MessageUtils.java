package mc.rysty.heliosphereranks.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageUtils {

	public static String convertChatColors(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(convertChatColors(message));
    }
}