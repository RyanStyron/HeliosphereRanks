package mc.rysty.heliosphereranks.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereranks.HelioSphereRanks;

public class MessageUtils {

    private static FileConfiguration config = HelioSphereRanks.getInstance().getConfig();

    public static String convertChatColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void message(CommandSender sender, String message) {
        sender.sendMessage(convertChatColors(message));
    }

    public static void configStringMessage(CommandSender sender, String configString) {
        message(sender, config.getString(configString));
    }

    public static void configStringMessage(CommandSender sender, String configString, String regex,
            String replacement) {
        message(sender, config.getString(configString).replaceAll(regex, replacement));
    }

    public static void configStringMessage(CommandSender sender, String configString, String regex, String replacement,
            String regex2, String replacement2) {
        message(sender, config.getString(configString).replaceAll(regex, replacement).replaceAll(regex2, replacement2));
    }

    public static void noPermissionMessage(CommandSender sender) {
        configStringMessage(sender, "no-perm-message");
    }

    public static void consoleErrorMessage(CommandSender sender) {
        configStringMessage(sender, "console-error-message");
    }

    public static void invalidPlayerMessage(CommandSender sender) {
        configStringMessage(sender, "player-offline-message");
    }
}