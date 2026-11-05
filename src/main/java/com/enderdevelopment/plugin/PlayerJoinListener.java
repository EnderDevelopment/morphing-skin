package com.enderdevelopment.plugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Reapplies a stored skin profile when a player rejoins, so the chosen
 * morphing skin persists and becomes visible.
 */
public class PlayerJoinListener implements Listener {

    private final SkinManager manager;

    public PlayerJoinListener(SkinManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String active = manager.getActiveSkin(player.getUniqueId());
        if (active == null) {
            return;
        }
        SkinEntry entry = manager.getSkin(active);
        if (entry != null) {
            manager.applySkin(player, entry);
        }
    }
}
