package org.multicharacters.objects.profile;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import org.multicharacters.PlayerProfiles;
import org.multicharacters.files.config.ConfigFile;
import org.multicharacters.utils.Experience;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class DataProfile {
    private String name;
    private boolean selected;
    private double lastHealth;
    private float lastXP;
    private int lastHunger;
    private Location lastLocation;
    private List<PotionEffect> potionEffects;
    private ItemStack[] enderchestItems;
    private ItemStack[] inventoryItems;
    private ItemStack[] armorItems;

    public DataProfile(String key, Player player) {
        this.name = key;
        this.selected = false;
        this.potionEffects = Lists.newArrayList();
        this.enderchestItems = new ItemStack[player.getEnderChest().getContents().length];
        this.inventoryItems = new ItemStack[player.getInventory().getContents().length];
        this.armorItems = new ItemStack[player.getInventory().getArmorContents().length];
        this.lastHealth = 20.0;
        this.lastHunger = 20;
        this.lastXP = 0.0f;
        this.lastLocation = player.getLocation();
    }

    public void save(PlayerProfiles playerProfiles, Player player) {
        ConfigFile configFile = playerProfiles.getFileManager().getConfigFile();
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncHealth")) {
            this.lastHealth = player.getHealth();
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncHunger")) {
            this.lastHunger = player.getFoodLevel();
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncXP")) {
            this.lastXP = Experience.getExp(player);
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncLocation")) {
            this.lastLocation = player.getLocation();
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncEffects")) {
            this.potionEffects.clear();
            this.potionEffects.addAll(player.getActivePotionEffects());
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncInventory")) {
            this.inventoryItems = (ItemStack[])player.getInventory().getContents().clone();
            this.armorItems = (ItemStack[])player.getInventory().getArmorContents().clone();
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncEnderchest")) {
            this.enderchestItems = (ItemStack[])player.getEnderChest().getContents().clone();
        }
    }

    public void updatePlayer(PlayerProfiles playerProfiles, Player player) {
        ConfigFile configFile = playerProfiles.getFileManager().getConfigFile();
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncHealth")) {
            player.setHealth(this.lastHealth);
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncHunger")) {
            player.setFoodLevel(this.lastHunger);
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncXP")) {
            Experience.changeExp(player, (int)this.lastXP);
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncEffects")) {
            for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(activePotionEffect.getType());
            }
            player.addPotionEffects(this.potionEffects);
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncInventory")) {
            player.getInventory().clear();
            player.getInventory().setContents(this.inventoryItems);
            player.getInventory().setArmorContents(this.armorItems);
            player.updateInventory();
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncEnderchest")) {
            player.getEnderChest().setContents(this.enderchestItems);
        }
        if (configFile.getConfiguration().getBoolean("profile.syncOptions.syncLocation")) {
            player.teleport(this.lastLocation);
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public double getLastHealth() {
        return this.lastHealth;
    }

    public float getLastXP() {
        return this.lastXP;
    }

    public int getLastHunger() {
        return this.lastHunger;
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    public List<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public ItemStack[] getEnderchestItems() {
        return this.enderchestItems;
    }

    public ItemStack[] getInventoryItems() {
        return this.inventoryItems;
    }

    public ItemStack[] getArmorItems() {
        return this.armorItems;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setLastHealth(double lastHealth) {
        this.lastHealth = lastHealth;
    }

    public void setLastXP(float lastXP) {
        this.lastXP = lastXP;
    }

    public void setLastHunger(int lastHunger) {
        this.lastHunger = lastHunger;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void setPotionEffects(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    public void setEnderchestItems(ItemStack[] enderchestItems) {
        this.enderchestItems = enderchestItems;
    }

    public void setInventoryItems(ItemStack[] inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public void setArmorItems(ItemStack[] armorItems) {
        this.armorItems = armorItems;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataProfile)) {
            return false;
        }
        DataProfile other = (DataProfile)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isSelected() != other.isSelected()) {
            return false;
        }
        if (Double.compare(this.getLastHealth(), other.getLastHealth()) != 0) {
            return false;
        }
        if (Float.compare(this.getLastXP(), other.getLastXP()) != 0) {
            return false;
        }
        if (this.getLastHunger() != other.getLastHunger()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        Location this$lastLocation = this.getLastLocation();
        Location other$lastLocation = other.getLastLocation();
        if (this$lastLocation == null ? other$lastLocation != null : !this$lastLocation.equals(other$lastLocation)) {
            return false;
        }
        List<PotionEffect> this$potionEffects = this.getPotionEffects();
        List<PotionEffect> other$potionEffects = other.getPotionEffects();
        if (this$potionEffects == null ? other$potionEffects != null : !((Object)this$potionEffects).equals(other$potionEffects)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getEnderchestItems(), other.getEnderchestItems())) {
            return false;
        }
        if (!Arrays.deepEquals(this.getInventoryItems(), other.getInventoryItems())) {
            return false;
        }
        return Arrays.deepEquals(this.getArmorItems(), other.getArmorItems());
    }

    protected boolean canEqual(Object other) {
        return other instanceof DataProfile;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSelected() ? 79 : 97);
        long $lastHealth = Double.doubleToLongBits(this.getLastHealth());
        result = result * 59 + (int)($lastHealth >>> 32 ^ $lastHealth);
        result = result * 59 + Float.floatToIntBits(this.getLastXP());
        result = result * 59 + this.getLastHunger();
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Location $lastLocation = this.getLastLocation();
        result = result * 59 + ($lastLocation == null ? 43 : $lastLocation.hashCode());
        List<PotionEffect> $potionEffects = this.getPotionEffects();
        result = result * 59 + ($potionEffects == null ? 43 : ((Object)$potionEffects).hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getEnderchestItems());
        result = result * 59 + Arrays.deepHashCode(this.getInventoryItems());
        result = result * 59 + Arrays.deepHashCode(this.getArmorItems());
        return result;
    }

    public String toString() {
        return "DataProfile(name=" + this.getName() + ", selected=" + this.isSelected() + ", lastHealth=" + this.getLastHealth() + ", lastXP=" + this.getLastXP() + ", lastHunger=" + this.getLastHunger() + ", lastLocation=" + this.getLastLocation() + ", potionEffects=" + this.getPotionEffects() + ", enderchestItems=" + Arrays.deepToString(this.getEnderchestItems()) + ", inventoryItems=" + Arrays.deepToString(this.getInventoryItems()) + ", armorItems=" + Arrays.deepToString(this.getArmorItems()) + ")";
    }

    public DataProfile(String name, boolean selected, double lastHealth, float lastXP, int lastHunger, Location lastLocation, List<PotionEffect> potionEffects, ItemStack[] enderchestItems, ItemStack[] inventoryItems, ItemStack[] armorItems) {
        this.name = name;
        this.selected = selected;
        this.lastHealth = lastHealth;
        this.lastXP = lastXP;
        this.lastHunger = lastHunger;
        this.lastLocation = lastLocation;
        this.potionEffects = potionEffects;
        this.enderchestItems = enderchestItems;
        this.inventoryItems = inventoryItems;
        this.armorItems = armorItems;
    }
}
