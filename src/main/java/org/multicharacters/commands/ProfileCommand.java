package org.multicharacters.commands;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.multicharacters.PlayerProfiles;
import org.multicharacters.gui.DeleteProfile;
import org.multicharacters.gui.ProfileManagement;
import org.multicharacters.objects.ProfileManager;
import org.multicharacters.objects.profile.DataProfile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ProfileCommand
        implements CommandExecutor,
        TabCompleter {
    private final PlayerProfiles playerProfiles;

    public ProfileCommand(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
        this.playerProfiles.getCommand("profile").setExecutor((CommandExecutor)this);
        this.playerProfiles.getCommand("profile").setTabCompleter((TabCompleter)this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return false;
        }
        if (!command.getName().equalsIgnoreCase("profile")) {
            return false;
        }
        Player player = (Player)sender;
        ProfileManager profileManager = this.playerProfiles.getProfiles().get(player.getUniqueId());
        if (args.length != 2) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("profile.reload")) {
                    player.sendMessage(this.playerProfiles.getLanguage().getMessage("not_permission"));
                    return false;
                }
                this.playerProfiles.getFileManager().getProfileFileGUI().reload(this.playerProfiles);
                this.playerProfiles.getFileManager().getConfigFile().reload(this.playerProfiles);
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("reload_completed"));
                return true;
            }
            if (!player.hasPermission("profile.gui")) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("not_permission"));
                return false;
            }
            ProfileManagement.getInventory(this.playerProfiles, profileManager).open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (!player.hasPermission("profile.delete")) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("not_permission"));
                return false;
            }
            if (profileManager.getProfile(args[1]) == null) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-does_not_exist"));
                return false;
            }
            if (args[1].equalsIgnoreCase("default")) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-cannot_delete_default"));
                return false;
            }
            DeleteProfile.getInventory(this.playerProfiles, profileManager.getProfile(args[1]), profileManager).open(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("switch")) {
            if (!player.hasPermission("profile.switch")) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("not_permission"));
                return false;
            }
            if (profileManager.getSelectedProfile().getName().equalsIgnoreCase(args[1])) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-in_use"));
                return false;
            }
            if (!profileManager.switchProfile(this.playerProfiles, args[1], player)) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-does_not_exist"));
                return false;
            }
            player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-switch", profileManager.getProfile(args[1])));
            return true;
        }
        if (args[0].equalsIgnoreCase("new")) {
            if (!player.hasPermission("profile.new")) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("not_permission"));
                return false;
            }
            if (profileManager.getProfileMap().size() >= this.playerProfiles.getFileManager().getConfigFile().getConfiguration().getInt("profile.maxNumberPerPlayer")) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("reached_max_profile_number"));
                return false;
            }
            if (profileManager.getProfileMap().containsKey(args[1])) {
                player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-already_exist", profileManager.getProfile(args[1])));
                return false;
            }
            profileManager.newProfile(this.playerProfiles, args[1], player);
            player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-created", profileManager.getProfile(args[1])));
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList completer = Lists.newArrayList();
        if (sender instanceof ConsoleCommandSender) {
            return completer;
        }
        Player player = (Player)sender;
        ProfileManager profileManager = this.playerProfiles.getProfiles().get(player.getUniqueId());
        if (args.length == 1) {
            completer.add("switch");
            completer.add("new");
            completer.add("delete");
            if (sender.hasPermission("profile.reload")) {
                completer.add("reload");
            }
            return completer;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("switch") || args[0].equalsIgnoreCase("delete"))) {
            for (DataProfile profile : profileManager.getProfiles()) {
                completer.add(profile.getName());
            }
            return completer;
        }
        return null;
    }
}
