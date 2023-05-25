package org.multicharacters.utils;

import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class BukkitSerialization {
    public static String itemStackArrayToBase64(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream((OutputStream)outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; ++i) {
                dataOutput.writeObject((Object)items[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines((byte[])outputStream.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String)data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream((InputStream)inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = (ItemStack)dataInput.readObject();
            }
            dataInput.close();
            return items;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Location deserializeLocation(String string) {
        if (string == null) {
            return null;
        }
        String[] split = string.split(":");
        return new Location(Bukkit.getWorld((String)split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static String serializeLocation(Location location) {
        if (location == null) {
            return null;
        }
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static String serializePotionEffect(PotionEffect effect) {
        return effect.getType().getName() + ":" + effect.getDuration() + ":" + effect.getAmplifier();
    }

    public static PotionEffect deserializePotionEffect(String effect) {
        String[] split = effect.split(":");
        return new PotionEffect(PotionEffectType.getByName((String)split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public static List<String> getEffectListString(Collection<PotionEffect> effects) {
        ArrayList list = Lists.newArrayList();
        for (PotionEffect effect : effects) {
            list.add(BukkitSerialization.serializePotionEffect(effect));
        }
        return list;
    }
}
