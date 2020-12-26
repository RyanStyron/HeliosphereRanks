package mc.rysty.heliosphereranks;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import mc.rysty.heliosphereranks.commands.CommandGroupPermission;
import mc.rysty.heliosphereranks.commands.Nickname;
import mc.rysty.heliosphereranks.commands.Prefix;
import mc.rysty.heliosphereranks.commands.SetGroup;
import mc.rysty.heliosphereranks.levels.CommandLevelInfo;
import mc.rysty.heliosphereranks.levels.CommandLevelLeaderboard;
import mc.rysty.heliosphereranks.levels.CommandLevelModify;
import mc.rysty.heliosphereranks.levels.CommandMonetaryModify;
import mc.rysty.heliosphereranks.levels.LevelsInitializer;
import mc.rysty.heliosphereranks.levels.MonetaryInitializer;
import mc.rysty.heliosphereranks.listeners.ListenerPlayerJoin;
import mc.rysty.heliosphereranks.listeners.ListenerPluginEnable;
import mc.rysty.heliosphereranks.utils.filemanagers.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.filemanagers.LevelsFileManager;
import mc.rysty.heliosphereranks.utils.filemanagers.PlayersFileManager;

public class HelioSphereRanks extends JavaPlugin {

	private static HelioSphereRanks plugin;

	public static HelioSphereRanks getInstance() {
		return plugin;
	}

	private static GroupsFileManager groupsFileManager = GroupsFileManager.getInstance();
	private static PlayersFileManager playersFileManager = PlayersFileManager.getInstance();
	private static LevelsFileManager levelsFileManager = LevelsFileManager.getInstance();

	public static HashMap<UUID, PermissionAttachment> permissionsMap = new HashMap<>();

	@Override
	public void onEnable() {
		/* Plugin Setup. */
		plugin = this;
		saveDefaultConfig();
		groupsFileManager.setup(this);
		playersFileManager.setup(this);
		levelsFileManager.setup(this);

		/* Listeners. */
		ListenerPluginEnable.enableScheduler();
		new ListenerPlayerJoin(this);

		/* Commands. */
		new Nickname(this);
		new Prefix(this);
		new SetGroup(this);
		new CommandGroupPermission(this);

		/* Levels. */
		LevelsInitializer.enableScheduler();
		MonetaryInitializer.enableScheduler();
		new CommandLevelModify(this);
		new CommandLevelInfo(this);
		new CommandLevelLeaderboard(this);
		new CommandMonetaryModify(this);

		System.out.println("HS-Ranks enabled");
	}

	@Override
	public void onDisable() {
		permissionsMap.clear();
		System.out.println("HS-Ranks disabled");
	}

	public static GroupsFileManager getGroupsFile() {
		return groupsFileManager;
	}

	public static PlayersFileManager getPlayersFile() {
		return playersFileManager;
	}

	public static LevelsFileManager getLevelsFile() {
		return levelsFileManager;
	}
}
