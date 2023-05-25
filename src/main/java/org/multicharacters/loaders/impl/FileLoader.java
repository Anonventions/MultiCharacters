package org.multicharacters.loaders.impl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.multicharacters.PlayerProfiles;
import org.multicharacters.files.config.UserFile;
import org.multicharacters.loaders.BasicLoader;
import org.multicharacters.objects.ProfileManager;
import org.multicharacters.objects.profile.DataProfile;
import org.multicharacters.utils.BukkitSerialization;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class FileLoader
        implements BasicLoader {
    private final PlayerProfiles playerProfiles;

    @Override
    public void loadProfiles(Player player) {
        ProfileManager profileManager = new ProfileManager();
        UserFile userFile = new UserFile(this.playerProfiles, player.getUniqueId().toString());
        ConfigurationSection section = userFile.getConfiguration().getConfigurationSection("profiles");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                boolean isSelected = section.getBoolean(key + ".selected");
                double lastHealth = section.getDouble(key + ".lastHealth");
                float lastXP = (float)section.getDouble(key + ".lastXP");
                int lastHunger = section.getInt(key + ".lastHunger");
                Location lastLocation = BukkitSerialization.deserializeLocation(section.getString(key + ".lastLocation"));
                ItemStack[] enderchestItems = BukkitSerialization.itemStackArrayFromBase64(section.getString(key + ".enderchestItems"));
                ItemStack[] inventoryItems = BukkitSerialization.itemStackArrayFromBase64(section.getString(key + ".inventoryItems"));
                ItemStack[] armorItems = BukkitSerialization.itemStackArrayFromBase64(section.getString(key + ".armorItems"));
                ArrayList effects = Lists.newArrayList();
                for (String effectSectionKey : section.getStringList(key + ".effects")) {
                    PotionEffect potionEffect = BukkitSerialization.deserializePotionEffect(effectSectionKey);
                    effects.add(potionEffect);
                }
                profileManager.registerProfile(new DataProfile(key, isSelected, lastHealth, lastXP, lastHunger, lastLocation, effects, enderchestItems, inventoryItems, armorItems));
            }
        } else {
            profileManager.getProfileMap().put("default", new DataProfile("default", player));
            profileManager.getProfileMap().get("default").setSelected(true);
        }
        this.playerProfiles.getProfiles().put(player.getUniqueId(), profileManager);
    }

    @Override
    public void saveProfiles(Player player) {
        UserFile userFile = new UserFile(this.playerProfiles, player.getUniqueId().toString());
        ProfileManager profileManager = this.playerProfiles.getProfiles().get(player.getUniqueId());
        userFile.getConfiguration().set("profiles", null);
        for (DataProfile profile : profileManager.getProfiles()) {
            if (profileManager.getSelectedProfile() == profile) {
                profile.save(this.playerProfiles, player);
            }
            userFile.getConfiguration().set("profiles." + profile.getName() + ".selected", (Object)profile.isSelected());
            userFile.getConfiguration().set("profiles." + profile.getName() + ".lastHealth", (Object)profile.getLastHealth());
            userFile.getConfiguration().set("profiles." + profile.getName() + ".lastXP", (Object)Float.valueOf(profile.getLastXP()));
            userFile.getConfiguration().set("profiles." + profile.getName() + ".lastHunger", (Object)profile.getLastHunger());
            userFile.getConfiguration().set("profiles." + profile.getName() + ".lastLocation", (Object)BukkitSerialization.serializeLocation(profile.getLastLocation()));
            userFile.getConfiguration().set("profiles." + profile.getName() + ".enderchestItems", (Object)BukkitSerialization.itemStackArrayToBase64(profile.getEnderchestItems()));
            userFile.getConfiguration().set("profiles." + profile.getName() + ".inventoryItems", (Object)BukkitSerialization.itemStackArrayToBase64(profile.getInventoryItems()));
            userFile.getConfiguration().set("profiles." + profile.getName() + ".armorItems", (Object)BukkitSerialization.itemStackArrayToBase64(profile.getArmorItems()));
            userFile.getConfiguration().set("profiles." + profile.getName() + ".effects", BukkitSerialization.getEffectListString(profile.getPotionEffects()));
        }
        userFile.save();
    }

    public FileLoader(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }
}
