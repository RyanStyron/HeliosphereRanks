package mc.rysty.heliosphereranks.levels;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.filemanagers.LevelsFileManager;

public class CommandLevelInfo implements CommandExecutor {

    private static LevelsFileManager levelsFileManager = HelioSphereRanks.getLevelsFile();
    private static FileConfiguration levelsFile = levelsFileManager.getData();

    public CommandLevelInfo(HelioSphereRanks plugin) {
        plugin.getCommand("levelinfo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("levelinfo")) {
            if (sender.hasPermission("hs.levelinfo")) {
                Player target = null;

                if (args.length > 0)
                    target = Bukkit.getPlayer(args[0]);
                else
                    target = (Player) sender;

                if (target != null) {
                    UUID playerId = target.getUniqueId();
                    int level = levelsFile.getInt("users." + playerId + ".level");
                    int experience = levelsFile.getInt("users." + playerId + ".xp");
                    int totalXpNextLevel = LevelsInitializer.getXpRequirementForLevel(level + 1);
                    int experienceLeft = totalXpNextLevel - experience;
                    double levelProgression = (double) (experience - LevelsInitializer.getXpRequirementForLevel(level))
                            / (double) (totalXpNextLevel - LevelsInitializer.getXpRequirementForLevel(level));

                    MessageUtils.message(sender, "&3==&6Level Info:&f " + target.getDisplayName() + "&3==");
                    MessageUtils.message(sender, "&6Level:&e " + level);
                    MessageUtils.message(sender, "&6Experience:&e " + experience);
                    MessageUtils.message(sender, "&6Level&e " + (level + 1) + " &6Experience:&e " + totalXpNextLevel);
                    MessageUtils.message(sender, "&6Experience Remaining:&e " + experienceLeft);
                    MessageUtils.message(sender,
                            "&6Level Progression:&e " + (Math.round(levelProgression * 10) / 10.0) * 100 + "%");
                } else
                    MessageUtils.invalidPlayerMessage(sender);
            }
        }
        return false;
    }
}
