package org.multicharacters.files;

import org.multicharacters.PlayerProfiles;
import org.multicharacters.files.config.ConfigFile;
import org.multicharacters.files.gui.ProfileFileGUI;

public class FileManager {
    private final PlayerProfiles profiles;
    private final ConfigFile configFile;
    private final ProfileFileGUI profileFileGUI;

    public FileManager(PlayerProfiles playerProfiles) {
        this.profiles = playerProfiles;
        this.configFile = new ConfigFile(this.profiles, "config.yml");
        this.profileFileGUI = new ProfileFileGUI(this.profiles, "profile.yml");
    }

    public PlayerProfiles getProfiles() {
        return this.profiles;
    }

    public ConfigFile getConfigFile() {
        return this.configFile;
    }

    public ProfileFileGUI getProfileFileGUI() {
        return this.profileFileGUI;
    }
}
