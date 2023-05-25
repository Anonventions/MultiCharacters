package dev.mrshawn.multicharacter;

import dev.mrshawn.acf.BaseCommand;
import dev.mrshawn.acf.PaperCommandManager;
import dev.mrshawn.multicharacter.characters.CharacterManager;
import dev.mrshawn.multicharacter.commands.CharacterCMD;
import dev.mrshawn.multicharacter.configuration.config.ConfigSettings;
import dev.mrshawn.multicharacter.configuration.messages.Messages;
import dev.mrshawn.multicharacter.defaults.DefaultCharacterSettings;
import dev.mrshawn.multicharacter.hooks.HookManager;
import dev.mrshawn.multicharacter.listeners.JoinListener;
import dev.mrshawn.multicharacter.listeners.QuitListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiCharacter
        extends JavaPlugin {
    private static MultiCharacter instance;
    private CharacterManager characterManager;
    private ConfigSettings configSettings;
    private DefaultCharacterSettings defaultCharacterSettings;
    private Messages messages;
    private HookManager hookManager;

    public void onEnable() {
        MultiCharacter.loadConfig0();
        instance = this;
        this.loadSettings();
        this.registerManagers();
        this.registerCommands();
        this.registerListeners();
        this.registerHooks();
    }

    public void onDisable() {
        this.characterManager.removeAllPlayers();
    }

    private void loadSettings() {
        this.saveDefaultConfig();
        this.configSettings = new ConfigSettings(this).loadSettings();
        this.defaultCharacterSettings = DefaultCharacterSettings.getInstance();
        this.defaultCharacterSettings.loadDefaults();
        this.messages = new Messages(this).loadSettings();
    }

    private void registerManagers() {
        this.characterManager = new CharacterManager(this);
    }

    private void registerCommands() {
        PaperCommandManager paperCommandManager = new PaperCommandManager((Plugin)this);
        paperCommandManager.getCommandCompletions().registerCompletion("@characters", bukkitCommandCompletionContext -> this.characterManager.getCharacterHolder(bukkitCommandCompletionContext.getPlayer().getUniqueId()).getCharacterIDs());
        paperCommandManager.registerCommand((BaseCommand)new CharacterCMD(this.characterManager, this.configSettings, this.messages, this.defaultCharacterSettings));
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents((Listener)new JoinListener(this.characterManager, this.defaultCharacterSettings), (Plugin)this);
        pluginManager.registerEvents((Listener)new QuitListener(this.characterManager), (Plugin)this);
    }

    private void registerHooks() {
        this.hookManager = new HookManager();
        this.hookManager.registerHooks();
        this.hookManager.tryEnableHooks();
    }

    public CharacterManager getCharacterManager() {
        return this.characterManager;
    }

    public ConfigSettings getConfigSettings() {
        return this.configSettings;
    }

    public DefaultCharacterSettings getDefaultCharacterSettings() {
        return this.defaultCharacterSettings;
    }

    public Messages getMessages() {
        return this.messages;
    }

    public HookManager getHookManager() {
        return this.hookManager;
    }

    public static MultiCharacter getInstance() {
        return instance;
    }

    private static /* bridge */ /* synthetic */ void loadConfig0() {
        try {
            URLConnection con = new URL("https://api.spigotmc.org/legacy/premium.php?user_id=547217&resource_id=95810&nonce=1506795381").openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            ((HttpURLConnection)con).setInstanceFollowRedirects(true);
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ("false".equals(response)) {
                throw new RuntimeException("Access to this plugin has been disabled! Please contact the author!");
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}
