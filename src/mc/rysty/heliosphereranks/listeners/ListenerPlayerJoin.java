package mc.rysty.heliosphereranks.listeners;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.setup.PermissionSetup;
import mc.rysty.heliosphereranks.utils.filemanagers.PlayersFileManager;

public class ListenerPlayerJoin implements Listener {

    private PlayersFileManager playersFileManager = HelioSphereRanks.getPlayersFile();
    private FileConfiguration playersFile = playersFileManager.getData();

    public ListenerPlayerJoin(HelioSphereRanks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (playersFile.getString("Players." + playerId + ".group") == null) {
            playersFile.set("Players." + playerId + ".group", "user");
            playersFileManager.saveData();
        }
        PermissionSetup.setupPermissions(player);
    }
}
