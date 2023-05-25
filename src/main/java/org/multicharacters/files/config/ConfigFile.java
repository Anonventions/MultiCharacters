package org.multicharacters.files.config;

import java.io.File;
import java.io.IOException;
import org.multicharacters.PlayerProfiles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFile {
    private FileConfiguration configuration;
    private File file;
    private String fileName;

    public ConfigFile(PlayerProfiles playerProfiles, String name) {
        this.fileName = name;
        this.file = new File(playerProfiles.getDataFolder(), this.fileName);
        if (!this.file.exists()) {
            playerProfiles.saveResource(this.fileName, true);
        }
        this.configuration = YamlConfiguration.loadConfiguration((File)this.file);
    }

    public void reload(PlayerProfiles playerProfiles) {
        this.file = new File(playerProfiles.getDataFolder(), this.fileName);
        if (!this.file.exists()) {
            playerProfiles.saveResource(this.fileName, true);
        }
        this.configuration = YamlConfiguration.loadConfiguration((File)this.file);
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    public File getFile() {
        return this.file;
    }

    public String getFileName() {
        return this.fileName;
    }
}
