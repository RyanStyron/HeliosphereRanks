package mc.rysty.heliosphereranks.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.GroupsFileManager;
import mc.rysty.heliosphereranks.utils.ListUtils;
import mc.rysty.heliosphereranks.utils.MessageUtils;

public class ListGroups implements CommandExecutor {

    private GroupsFileManager groupsFileManager = GroupsFileManager.getInstance();
    private FileConfiguration groupsFile = groupsFileManager.getData();

    public ListGroups(HelioSphereRanks plugin) {
        plugin.getCommand("listgroups").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("listgroups")) {
            if (sender.hasPermission("hs.listgroups")) {
                List<String> groups = new ArrayList<>();

                if (groupsFile.getConfigurationSection("Groups") != null)
                    for (String key : groupsFile.getConfigurationSection("Groups").getKeys(false))
                        groups.add(key);

                MessageUtils.message(sender, ListUtils.fromList(groups, false, false));
            } else 
                MessageUtils.noPermissionMessage(sender);
        }
        return false;
    }
}