package org.multicharacters.listeners;

import org.multicharacters.PlayerProfiles;
import org.multicharacters.objects.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener
        implements Listener {
    private final PlayerProfiles playerProfiles;

    @EventHandler
    public void login(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.playerProfiles.getLoader().loadProfiles(player);
        ProfileManager profileManager = this.playerProfiles.getProfiles().get(player.getUniqueId());
        profileManager.getSelectedProfile().updatePlayer(this.playerProfiles, player);
        player.sendMessage(this.playerProfiles.getLanguage().getMessage("join-message", profileManager.getSelectedProfile()));
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        this.playerProfiles.getLoader().saveProfiles(event.getPlayer());
    }

    public EventListener(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }
}
