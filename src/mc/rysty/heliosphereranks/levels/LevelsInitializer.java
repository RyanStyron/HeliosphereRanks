package mc.rysty.heliosphereranks.levels;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.filemanagers.LevelsFileManager;

public class LevelsInitializer {

    private static HelioSphereRanks plugin = HelioSphereRanks.getInstance();
    private static FileConfiguration config = plugin.getConfig();
    private static LevelsFileManager levelsFileManager = HelioSphereRanks.getLevelsFile();
    private static FileConfiguration levelsFile = levelsFileManager.getData();

    private static boolean updateLevelDisplay = true;

    public static void enableScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    calculateLevel(player);
                    setLevelDisplay(player);
                }
            }
        }, 0, 20);
    }

    private static void calculateLevel(Player player) {
        UUID playerId = player.getUniqueId();

        if (levelsFile.getString("users." + playerId + ".xp") == null) {
            levelsFile.set("users." + playerId + ".xp", 25);
            levelsFile.set("users." + playerId + ".level", 0);
            levelsFileManager.saveData();
        }
        int level = levelsFile.getInt("users." + playerId + ".level");
        int currentXp = levelsFile.getInt("users." + playerId + ".xp");

        if (currentXp >= getXpRequirementForLevel(level + 1)) {
            levelsFile.set("users." + playerId + ".level", level + 1);
            levelsFileManager.saveData();

            MessageUtils.configStringMessage(player, "leveling.level-up-message", "<level>", "" + (level + 1));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        } else if (currentXp < getXpRequirementForLevel(level)) {
            levelsFile.set("users." + playerId + ".level", level - 1);
            levelsFileManager.saveData();

            MessageUtils.configStringMessage(player, "leveling.level-down-message", "<level>", "" + (level - 1));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1, 1);
        }
    }

    private static void setLevelDisplay(Player player) {
        if (player.getWorld().equals(Bukkit.getWorld("Hub"))) {
            UUID playerId = player.getUniqueId();
            int level = levelsFile.getInt("users." + playerId + ".level");
            int currentXp = levelsFile.getInt("users." + playerId + ".xp");
            int totalXpNextLevel = getXpRequirementForLevel(level + 1);
            double levelProgression = (double) (currentXp - getXpRequirementForLevel(level))
                    / (double) (totalXpNextLevel - getXpRequirementForLevel(level));

            if (levelProgression >= 1)
                updateLevelDisplay = false;

            if (level == 50)
                levelProgression = 0.999;

            if (updateLevelDisplay) {
                player.setLevel(level);
                player.setExp((float) levelProgression);
            }
            updateLevelDisplay = true;
        }
    }

    public static int getXpRequirementForLevel(int level) {
        if (config.getString("level-requirement." + level) != null)
            return config.getInt("level-requirement." + level);
        return 1000000000;
    }
}
