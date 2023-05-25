package org.multicharacters.files.lang;

import org.multicharacters.files.config.ConfigFile;
import org.multicharacters.objects.profile.DataProfile;
import org.multicharacters.utils.Tools;

public class Language {
    private final ConfigFile configFile;

    public String getMessage(String path, DataProfile profile) {
        return Tools.translate(this.configFile.getConfiguration().getString("lang." + path).replaceAll("%profile%", profile.getName()));
    }

    public String getMessage(String path) {
        return Tools.translate(this.configFile.getConfiguration().getString("lang." + path));
    }

    public Language(ConfigFile configFile) {
        this.configFile = configFile;
    }
}
