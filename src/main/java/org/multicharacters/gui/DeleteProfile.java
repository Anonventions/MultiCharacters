package org.multicharacters.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import java.util.ArrayList;
import java.util.Locale;
import org.multicharacters.PlayerProfiles;
import org.multicharacters.files.gui.ProfileFileGUI;
import org.multicharacters.objects.ProfileManager;
import org.multicharacters.objects.profile.DataProfile;
import org.multicharacters.utils.ItemBuilder;
import org.multicharacters.utils.Tools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeleteProfile
        implements InventoryProvider {
    private static SmartInventory smartInventory;
    private final PlayerProfiles playerProfiles;
    private final ProfileManager profileManager;
    private final DataProfile dataProfile;
    private final ProfileFileGUI profileFileGUI;

    public DeleteProfile(PlayerProfiles pl, DataProfile dt, ProfileManager pm) {
        this.playerProfiles = pl;
        this.profileManager = pm;
        this.dataProfile = dt;
        this.profileFileGUI = this.playerProfiles.getFileManager().getProfileFileGUI();
    }

    public static SmartInventory getInventory(PlayerProfiles playerProfiles, DataProfile dataProfile, ProfileManager pm) {
        smartInventory = SmartInventory.builder().id("Members").provider((InventoryProvider)new DeleteProfile(playerProfiles, dataProfile, pm)).size(3, 9).title(Tools.translate(playerProfiles.getFileManager().getProfileFileGUI().getConfiguration().getString("DELETEGUI.title"))).build();
        return smartInventory;
    }

    public void init(Player player, InventoryContents contents) {
        this.createConfirm(player, contents);
        this.createDelete(player, contents);
    }

    private void createConfirm(Player player, InventoryContents contents) {
        Material material = Material.valueOf((String)this.profileFileGUI.getConfiguration().getString("DELETEGUI.items.deleteConfirm.type").toUpperCase(Locale.ROOT));
        short damage = (short)this.profileFileGUI.getConfiguration().getInt("DELETEGUI.items.deleteConfirm.damage");
        ItemBuilder itemBuilder = new ItemBuilder(material, damage);
        String name = this.profileFileGUI.getConfiguration().getString("DELETEGUI.items.deleteConfirm.name");
        ArrayList<String> lore = new ArrayList<String>();
        for (String s : this.profileFileGUI.getConfiguration().getStringList("DELETEGUI.items.deleteConfirm.lore")) {
            lore.add(this.addPlaceholder(this.dataProfile, s));
        }
        itemBuilder.setName(this.addPlaceholder(this.dataProfile, name));
        itemBuilder.setLore(lore);
        contents.set(1, 2, ClickableItem.of((ItemStack)itemBuilder.build(), event -> {
            if (this.profileManager.getSelectedProfile() == this.dataProfile) {
                this.profileManager.switchProfile(this.playerProfiles, "default", player);
            }
            this.profileManager.getProfileMap().remove(this.dataProfile.getName());
            player.sendMessage(this.playerProfiles.getLanguage().getMessage("profile-deleted", this.dataProfile));
            player.closeInventory();
        }));
    }

    private void createDelete(Player player, InventoryContents contents) {
        Material material = Material.valueOf((String)this.profileFileGUI.getConfiguration().getString("DELETEGUI.items.deleteCancel.type").toUpperCase(Locale.ROOT));
        short damage = (short)this.profileFileGUI.getConfiguration().getInt("DELETEGUI.items.deleteCancel.damage");
        ItemBuilder itemBuilder = new ItemBuilder(material, damage);
        String name = this.profileFileGUI.getConfiguration().getString("DELETEGUI.items.deleteCancel.name");
        ArrayList<String> lore = new ArrayList<String>();
        for (String s : this.profileFileGUI.getConfiguration().getStringList("DELETEGUI.items.deleteCancel.lore")) {
            lore.add(this.addPlaceholder(this.dataProfile, s));
        }
        itemBuilder.setName(this.addPlaceholder(this.dataProfile, name));
        itemBuilder.setLore(lore);
        contents.set(1, 5, ClickableItem.of((ItemStack)itemBuilder.build(), event -> player.closeInventory()));
    }

    public String addPlaceholder(DataProfile dataProfile, String message) {
        return message.replaceAll("%profile_name%", Tools.toTitleCase(dataProfile.getName())).replaceAll("%world%", dataProfile.getLastLocation().getWorld().getName()).replaceAll("%status%", dataProfile.isSelected() ? "&aSelected" : "&cNot Selected").replaceAll("%loc_x%", Tools.doubleDigitNumber(dataProfile.getLastLocation().getX())).replaceAll("%loc_y%", Tools.doubleDigitNumber(dataProfile.getLastLocation().getY())).replaceAll("%loc_z%", Tools.doubleDigitNumber(dataProfile.getLastLocation().getZ()));
    }

    public void update(Player player, InventoryContents contents) {
        int state = (Integer)contents.property("state", (Object)0);
        contents.setProperty("state", (Object)(state + 1));
        if (state % 100 != 0) {
            return;
        }
        this.init(player, contents);
    }
}
