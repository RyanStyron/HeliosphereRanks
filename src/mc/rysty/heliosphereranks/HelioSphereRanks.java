package mc.rysty.heliosphereranks;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mc.rysty.heliosphereranks.commands.Nickname;
import mc.rysty.heliosphereranks.commands.Prefix;
import mc.rysty.heliosphereranks.commands.SetGroup;
import mc.rysty.heliosphereranks.player.DisplayName;
import mc.rysty.heliosphereranks.player.SetDefaultGroup;
import mc.rysty.heliosphereranks.setup.Setup;
import mc.rysty.heliosphereranks.setup.UpdateConfigYaml;
import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.PlayersFileManager;

public class HelioSphereRanks extends JavaPlugin {

	private static HelioSphereRanks plugin;

	public static HelioSphereRanks getInstance() {
		return plugin;
	}

	public static HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<>();

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		GroupsFileManager.getInstance().setup(this);
		PlayersFileManager.getInstance().setup(this);

		new Nickname(this);
		new Prefix(this);
		new SetGroup(this);

		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new SetDefaultGroup(), this);
		pluginManager.registerEvents(new DisplayName(), this);
		pluginManager.registerEvents(new Setup(), this);
		pluginManager.registerEvents(new UpdateConfigYaml(), this);

		System.out.println("HS-Ranks enabled");
	}

	public void onDisable() {
		playerPermissions.clear();
		System.out.println("HS-Ranks disabled");
	}

}
