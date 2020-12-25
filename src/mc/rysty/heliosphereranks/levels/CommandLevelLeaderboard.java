package mc.rysty.heliosphereranks.levels;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MathUtils;
import mc.rysty.heliosphereranks.utils.MessageUtils;

public class CommandLevelLeaderboard implements CommandExecutor {

    private FileConfiguration levelsFile = HelioSphereRanks.getLevelsFile().getData();
    private FileConfiguration playersFile = HelioSphereRanks.getPlayersFile().getData();

    public CommandLevelLeaderboard(HelioSphereRanks plugin) {
        plugin.getCommand("levelleaderboard").setExecutor(this);
    }

    private HashMap<String, Integer> userExperienceMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> userLevelMap = new HashMap<String, Integer>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("levelleaderboard")) {
            Set<String> configurationSection = levelsFile.getConfigurationSection("users").getKeys(false);
            int userCount = configurationSection.size();

            for (String user : configurationSection) {
                String userUuidString = user;
                String userDisplayname = playersFile.getString("Players." + userUuidString + ".displayname");
                int userExperience = levelsFile.getInt("users." + user + ".xp");

                userExperienceMap.put(userDisplayname, userExperience);
                userLevelMap.put(userDisplayname, levelsFile.getInt("users." + userUuidString + ".level"));
            }
            MathUtils.ValueComparator experienceMapValueComparator = new MathUtils.ValueComparator(userExperienceMap);
            TreeMap<String, Integer> sortedExperienceMap = new TreeMap<String, Integer>(experienceMapValueComparator);

            sortedExperienceMap.putAll(userExperienceMap);
            MessageUtils.message(sender, "&b-===-&3 Levels Leaderboard &b-===-");
            for (int i = 1; i <= (userCount >= 10 ? 10 : userCount); i++) {
                Entry<String, Integer> entry = sortedExperienceMap.pollFirstEntry();
                String key = entry.getKey();
                int value = entry.getValue();

                MessageUtils.message(sender, "&b" + i + ". &e" + key + " &b--&e Level " + userLevelMap.get(key)
                        + " &6(&e" + value + " Exp&6)");
            }
        }
        return false;
    }
}
