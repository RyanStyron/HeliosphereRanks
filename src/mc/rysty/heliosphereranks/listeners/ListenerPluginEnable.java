package mc.rysty.heliosphereranks.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.filemanagers.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.filemanagers.PlayersFileManager;

public class ListenerPluginEnable {

    private static HelioSphereRanks plugin = HelioSphereRanks.getInstance();
    private static PlayersFileManager playersFileManager = HelioSphereRanks.getPlayersFile();
    private static GroupsFileManager groupsFileManager = HelioSphereRanks.getGroupsFile();
    private static FileConfiguration playersFile = playersFileManager.getData();
    private static FileConfiguration groupsFile = groupsFileManager.getData();

    public static void enableScheduler() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayerYmlData(player);
                    updateDisplayName(player);
                }
            }
        }, 0, 10);
    }

    private static void updatePlayerYmlData(Player player) {
        String playerName = player.getName();
        String displayName = player.getDisplayName();
        UUID playerId = player.getUniqueId();

        if (playersFile.getString("Players." + playerId + ".username") == null
                || !playersFile.getString("Players." + playerId + ".username").equals(playerName)) {
            playersFile.set("Players." + playerId + ".username", playerName);
            playersFileManager.saveData();
        }

        if (playersFile.getString("Players." + playerId + ".displayname") == null
                || !playersFile.getString("Players." + playerId + ".displayname").equals(displayName)) {
            playersFile.set("Players." + playerId + ".displayname", displayName);
            playersFileManager.saveData();
        }
    }

    private static void updateDisplayName(Player player) {
        UUID playerId = player.getUniqueId();
        String playerName = player.getName();
        String previousDisplayName = player.getDisplayName();
        String displayName = playerName;

        // Set the player's display name to only their nickname.
        if (playersFile.getString("Players." + playerId + ".nickname") != null)
            displayName = playersFile.getString("Players." + playerId + ".nickname");

        // Check for if the player is a member of a group.
        if (playersFile.getString("Players." + playerId + ".group") != null) {
            String playerGroup = playersFile.getString("Players." + playerId + ".group");

            // Set the player's display name to only their group's prefix if it has one.
            if (groupsFile.getString("Groups." + playerGroup + ".prefix") != null) {
                String groupPrefix = groupsFile.getString("Groups." + playerGroup + ".prefix");

                displayName = groupPrefix + playerName;

                // Set the player's display name to their group's prefix and nickname.
                if (playersFile.getString("Players." + playerId + ".nickname") != null) {
                    String nickname = playersFile.getString("Players." + playerId + ".nickname");

                    displayName = groupPrefix + nickname;
                }
            }
        }

        // Set the player's display name to their personal prefix if they have one.
        if (playersFile.getString("Players." + playerId + ".prefix") != null) {
            String personalPrefix = playersFile.getString("Players." + playerId + ".prefix");

            displayName = personalPrefix + playerName;

            // Set the player's display name to their personal prefix and nickname.
            if (playersFile.getString("Players." + playerId + ".nickname") != null) {
                String nickname = playersFile.getString("Players." + playerId + ".nickname");

                displayName = personalPrefix + nickname;
            }
        }

        if (displayName != previousDisplayName)
            player.setDisplayName(MessageUtils.convertChatColors(displayName));
    }
}
