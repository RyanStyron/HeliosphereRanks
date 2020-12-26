package mc.rysty.heliosphereranks.levels;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.filemanagers.LevelsFileManager;

public class MonetaryInitializer {

    private static HelioSphereRanks plugin = HelioSphereRanks.getInstance();
    private static LevelsFileManager levelsFileManager = HelioSphereRanks.getLevelsFile();
    private static FileConfiguration levelsFile = levelsFileManager.getData();

    public static void enableScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (levelsFile.getString("users." + player.getUniqueId() + ".emeralds") == null)
                        setUserEmeraldBalance(player, 100);
                    if (levelsFile.getString("users." + player.getUniqueId() + ".diamonds") == null)
                        setUserDiamondBalance(player, 0);
                }
            }
        }, 0, 20);
    }

    public static void setUserEmeraldBalance(Player player, int emeraldCount) {
        levelsFile.set("users." + player.getUniqueId() + ".emeralds", emeraldCount);
        levelsFileManager.saveData();
    }

    public static void setUserDiamondBalance(Player player, int diamondCount) {
        levelsFile.set("users." + player.getUniqueId() + ".diamonds", diamondCount);
        levelsFileManager.saveData();
    }

    public static int getUserEmeraldBalance(Player player) {
        return levelsFile.getInt("users." + player.getUniqueId() + ".emeralds");
    }

    public static int getUserDiamondBalance(Player player) {
        return levelsFile.getInt("users." + player.getUniqueId() + ".diamonds");
    }
}
