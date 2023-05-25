package org.multicharacters.objects;

import java.util.Collection;
import org.multicharacters.PlayerProfiles;
import org.multicharacters.objects.profile.DataProfile;
import org.multicharacters.objects.structures.StringMap;
import org.bukkit.entity.Player;

public class ProfileManager {
    private final StringMap<DataProfile> profileMap = new StringMap();

    public Collection<DataProfile> getProfiles() {
        return this.profileMap.values();
    }

    public void newProfile(PlayerProfiles playerProfiles, String key, Player player) {
        this.profileMap.put(key, new DataProfile(key, player));
        this.switchProfile(playerProfiles, key, player);
    }

    public void registerProfile(DataProfile profile) {
        this.profileMap.put(profile.getName(), profile);
    }

    public DataProfile getSelectedProfile() {
        for (DataProfile profile : this.getProfiles()) {
            if (!profile.isSelected()) continue;
            return profile;
        }
        return null;
    }

    public DataProfile getProfile(String key) {
        return this.profileMap.get(key);
    }

    public boolean switchProfile(PlayerProfiles playerProfiles, String key, Player player) {
        if (!this.profileMap.containsKey(key)) {
            return false;
        }
        DataProfile oldProfile = this.getSelectedProfile();
        DataProfile profile = this.profileMap.get(key);
        oldProfile.save(playerProfiles, player);
        oldProfile.setSelected(false);
        profile.setSelected(true);
        profile.updatePlayer(playerProfiles, player);
        return true;
    }

    public StringMap<DataProfile> getProfileMap() {
        return this.profileMap;
    }
}
