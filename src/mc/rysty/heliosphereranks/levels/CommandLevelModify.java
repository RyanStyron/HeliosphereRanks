package mc.rysty.heliosphereranks.levels;

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
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.filemanagers.LevelsFileManager;

public class CommandLevelModify implements CommandExecutor, TabCompleter {

    private static LevelsFileManager levelsFileManager = HelioSphereRanks.getLevelsFile();
    private static FileConfiguration levelsFile = levelsFileManager.getData();

    public CommandLevelModify(HelioSphereRanks plugin) {
        plugin.getCommand("levelmodify").setExecutor(this);
        plugin.getCommand("levelmodify").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("levelmodify")) {
            if (sender.hasPermission("hs.levelmodify")) {
                if (args.length == 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    String modificationArgument = args[1].toLowerCase();
                    String amountArgument = args[2].toLowerCase();

                    if (target != null) {
                        UUID playerId = target.getUniqueId();
                        int currentXp = levelsFile.getInt("users." + playerId + ".xp");
                        int modificationValue = 0;

                        if (modificationArgument.equals("add") || modificationArgument.equals("remove")) {
                            try {
                                modificationValue = Integer.parseInt(amountArgument);
                            } catch (NumberFormatException exception) {
                                MessageUtils.configStringMessage(sender, "leveling.argument-error");
                            }

                            if (modificationArgument.equals("add")) {
                                levelsFile.set("users." + playerId + ".xp", currentXp + modificationValue);
                                MessageUtils.configStringMessage(target, "leveling.xp-awarded-target-message", "<xp>",
                                        amountArgument);
                                MessageUtils.configStringMessage(sender, "leveling.xp-awarded-message");
                            } else if (currentXp - modificationValue >= 0) {
                                levelsFile.set("users." + playerId + ".xp", currentXp - modificationValue);
                                MessageUtils.configStringMessage(target, "leveling.xp-removed-target-message", "<xp>",
                                        amountArgument);
                                MessageUtils.configStringMessage(sender, "leveling.xp-removed-message");
                            } else
                                MessageUtils.configStringMessage(sender, "leveling.xp-removed-error");
                            levelsFileManager.saveData();
                        } else
                            MessageUtils.configStringMessage(sender, "leveling.argument-error");
                    } else
                        MessageUtils.invalidPlayerMessage(sender);
                } else
                    MessageUtils.configStringMessage(sender, "leveling.argument-error");
            } else
                MessageUtils.noPermissionMessage(sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("hs.levelmodify")) {
            List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                completions.clear();
                for (Player player : Bukkit.getOnlinePlayers())
                    completions.add(player.getName());
            } else if (args.length == 2) {
                completions.clear();
                completions.add("add");
                completions.add("remove");
            }
            return completions;
        }
        return null;
    }
}
