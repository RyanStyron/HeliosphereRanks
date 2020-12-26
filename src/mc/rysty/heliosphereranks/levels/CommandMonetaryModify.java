package mc.rysty.heliosphereranks.levels;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MessageUtils;

public class CommandMonetaryModify implements CommandExecutor, TabCompleter {

    public CommandMonetaryModify(HelioSphereRanks plugin) {
        plugin.getCommand("monetarymodify").setExecutor(this);
        plugin.getCommand("monetarymodify").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("monetarymodify")) {
            if (sender.hasPermission("hs.monetarymodify")) {
                if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[0]);
                    String modifierArgument = args[1].toLowerCase();
                    String amountArgument = args[2].toLowerCase();
                    int modificationValue = 0;

                    if (player != null) {
                        try {
                            modificationValue = Integer.parseInt(amountArgument);
                        } catch (NumberFormatException exception) {
                            MessageUtils.argumentError(sender,
                                    "/monetarymodify <player> <setemeralds | setdiamonds> <amount>");
                        }

                        if (modifierArgument.equals("setemeralds")) {
                            MonetaryInitializer.setUserEmeraldBalance(player, modificationValue);
                            MessageUtils.configStringMessage(sender, "monetary.emeralds-set-message", "<emeralds>",
                                    amountArgument);
                        } else if (modifierArgument.equals("setdiamonds")) {
                            MonetaryInitializer.setUserDiamondBalance(player, modificationValue);
                            MessageUtils.configStringMessage(sender, "monetary.diamonds-set-message", "<diamonds>",
                                    amountArgument);
                        } else
                            MessageUtils.argumentError(sender,
                                    "/monetarymodify <player> <setemeralds | setdiamonds> <amount>");
                    } else
                        MessageUtils.invalidPlayerMessage(sender);
                } else
                    MessageUtils.argumentError(sender, "/monetarymodify <player> <setemeralds | setdiamonds> <amount>");
            } else
                MessageUtils.noPermissionMessage(sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("hs.monetarymodify")) {
            List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                completions.clear();
                for (Player player : Bukkit.getOnlinePlayers())
                    completions.add(player.getName());
            } else if (args.length == 2) {
                completions.clear();
                completions.add("setemeralds");
                completions.add("setdiamonds");
            }
            return completions;
        }
        return null;
    }
}
