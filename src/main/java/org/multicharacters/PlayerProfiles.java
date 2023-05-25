package org.multicharacters;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import org.multicharacters.commands.ProfileCommand;
import org.multicharacters.files.FileManager;
import org.multicharacters.files.lang.Language;
import org.multicharacters.listeners.EventListener;
import org.multicharacters.loaders.BasicLoader;
import org.multicharacters.loaders.impl.FileLoader;
import org.multicharacters.objects.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerProfiles
        extends JavaPlugin {
    private BasicLoader loader;
    private final Map<UUID, ProfileManager> profiles = Maps.newHashMap();
    private FileManager fileManager;
    private Language language;

    public void onEnable() {
        this.fileManager = new FileManager(this);
        this.loader = new FileLoader(this);
        this.language = new Language(this.fileManager.getConfigFile());
        Bukkit.getPluginManager().registerEvents((Listener)new EventListener(this), (Plugin)this);
        new ProfileCommand(this);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.loader.loadProfiles(onlinePlayer);
        }
        this.getLogger().info("PlayerProfiles Enabled!");
    }

    public void onDisable() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.loader.saveProfiles(onlinePlayer);
        }
        this.getLogger().info("PlayerProfiles Disabled!");
    }

    public BasicLoader getLoader() {
        return this.loader;
    }

    public Map<UUID, ProfileManager> getProfiles() {
        return this.profiles;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
