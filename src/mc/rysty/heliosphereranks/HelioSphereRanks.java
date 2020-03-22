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
import mc.rysty.heliosphereranks.player.Permissions;
import mc.rysty.heliosphereranks.setup.Setup;
import mc.rysty.heliosphereranks.setup.UpdateConfigYaml;

public class HelioSphereRanks extends JavaPlugin {

	public static HelioSphereRanks plugin;

	public static HelioSphereRanks getInstance() {
		return plugin;
	}

	public static HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<>();
	PluginManager pm = this.getServer().getPluginManager();

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();

		new Nickname(this);
		new Prefix(this);
		new SetGroup(this);

		pm.registerEvents(new Permissions(), this);
		pm.registerEvents(new DisplayName(), this);
		pm.registerEvents(new Setup(), this);
		pm.registerEvents(new UpdateConfigYaml(), this);

		System.out.println("HS-Ranks enabled");
	}

	public void onDisable() {
		playerPermissions.clear();
		System.out.println("HS-Ranks disabled");
	}

}
