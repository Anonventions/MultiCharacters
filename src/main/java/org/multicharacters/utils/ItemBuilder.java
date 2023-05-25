package org.multicharacters.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.multicharacters.utils.Tools;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, short damage) {
        this.item = new ItemStack(material, 1, damage);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(ItemStack items) {
        this.item = items;
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(ItemStack items, int amount) {
        this.item = items;
        if (amount > 64 || amount < 0) {
            amount = 64;
        }
        this.item.setAmount(amount);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        this.meta.setDisplayName(Tools.translate(name));
        return this;
    }

    public ItemBuilder setLore(String ... lore) {
        return this.setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        this.meta.setLore(Tools.translate(lore));
        return this;
    }

    public ItemBuilder addLore(List<String> lores) {
        List newLore = this.meta.getLore();
        newLore.addAll(lores);
        this.meta.setLore(newLore);
        return this;
    }

    public ItemBuilder setFlags(ItemFlag ... flags) {
        for (ItemFlag flag : flags) {
            this.meta.addItemFlags(new ItemFlag[]{flag});
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        this.meta.addEnchant(ench, level, true);
        return this;
    }

    public ItemBuilder setSkull(String playerName) {
        SkullMeta meta = (SkullMeta) this.meta;
        // No need for reflection here, just use the Bukkit API method
        meta.setOwner(playerName);
        this.meta = meta;
        return this;
    }

    public ItemBuilder setPlayerSkull(String playerName) {
        SkullMeta meta = (SkullMeta)this.meta;
        meta.setOwner(playerName);
        this.meta = meta;
        return this;
    }

    public ItemBuilder addLoreLines(String ... lines) {
        ArrayList<String> lore = new ArrayList<String>();
        if (this.meta.hasLore()) {
            lore = new ArrayList(this.meta.getLore());
        }
        for (String line : lines) {
            if (line.equalsIgnoreCase("%empty%")) continue;
            lore.add(Tools.translate(line));
        }
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean state) {
        this.meta.spigot().setUnbreakable(state);
        return this;
    }

    public ItemBuilder setLeatherColor(int red, int green, int blue) {
        LeatherArmorMeta im = (LeatherArmorMeta)this.meta;
        im.setColor(Color.fromRGB((int)red, (int)green, (int)blue));
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}
