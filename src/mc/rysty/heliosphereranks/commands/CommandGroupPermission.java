package mc.rysty.heliosphereranks.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import mc.rysty.heliosphereranks.HelioSphereRanks;
import mc.rysty.heliosphereranks.utils.MessageUtils;
import mc.rysty.heliosphereranks.utils.filemanagers.GroupsFileManager;

public class CommandGroupPermission implements CommandExecutor, TabCompleter {

    private GroupsFileManager groupsFileManager = HelioSphereRanks.getGroupsFile();
    private FileConfiguration groupsFile = groupsFileManager.getData();

    public CommandGroupPermission(HelioSphereRanks plugin) {
        plugin.getCommand("grouppermission").setExecutor(this);
        plugin.getCommand("grouppermission").setTabCompleter(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("grouppermission")) {
            if (sender.hasPermission("hs.grouppermission")) {
                if (args.length == 3) {
                    String group = args[0].toLowerCase();
                    String selector = args[1].toLowerCase();
                    String permission = args[2];

                    if (groupsFile.getString("Groups." + group) != null) {
                        if (selector.equals("add")) {
                            if (!groupHasPermission(group, permission)) {
                                groupsFile.getStringList("Groups." + group + ".permissions").add(permission);
                                MessageUtils.configStringMessage(sender, "grouppermission.group-permission-added");
                                groupsFileManager.saveData();
                            } else
                                MessageUtils.configStringMessage(sender, "grouppermission.group-has-permission-error");
                        } else if (selector.equals("remove")) {
                            if (groupHasPermission(group, permission)) {
                                groupsFile.getStringList("Groups." + group + ".permissions").remove(permission);
                                MessageUtils.configStringMessage(sender, "grouppermission.group-permission-removed");
                                groupsFileManager.saveData();
                            } else
                                MessageUtils.configStringMessage(sender, "grouppermission.group-no-permission-error");
                        } else if (selector.equals("check")) {
                            if (groupHasPermission(group, permission))
                                MessageUtils.configStringMessage(sender, "grouppermission.permission-check-true");
                            else
                                MessageUtils.configStringMessage(sender, "grouppermission.permission-check-false");
                        } else
                            MessageUtils.argumentError(sender,
                                    "/grouppermission <group> <add | remove | check> <permission>");
                    } else
                        MessageUtils.configStringMessage(sender, "grouppermission.group-error");
                } else
                    MessageUtils.argumentError(sender, "/grouppermission <group> <add | remove | check> <permission>");
            } else
                MessageUtils.noPermissionMessage(sender);
        }
        return false;
    }

    private boolean groupHasPermission(String group, String permission) {
        if (groupsFile.getStringList("Groups." + group + ".permissions").contains(permission))
            return true;
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> groupCompletions = new ArrayList<>();
        List<String> selectorCompletions = new ArrayList<>();

        for (String key : groupsFile.getConfigurationSection("Groups").getKeys(false))
            groupCompletions.add(key);
        selectorCompletions.add("add");
        selectorCompletions.add("remove");
        selectorCompletions.add("check");

        if (args.length == 1)
            return groupCompletions;
        else if (args.length == 2)
            return selectorCompletions;
        return null;
    }
}
