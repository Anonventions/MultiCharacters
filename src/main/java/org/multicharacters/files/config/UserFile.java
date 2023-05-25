package org.multicharacters.files.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UserFile {
    private FileConfiguration configuration;
    private File file;

    public UserFile(JavaPlugin javaPlugin, String fileName) {
        this.file = new File(javaPlugin.getDataFolder() + File.separator + "userdata" + File.separator, fileName + ".yml");
        if (!this.file.exists()) {
            this.file.mkdir();
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
}
