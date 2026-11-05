package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Handles clicks inside the Morphing Skin menu.
 */
public class SkinListener implements Listener {

    private final Main plugin;
    private final SkinManager manager;

    public SkinListener(Main plugin, SkinManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (event.getView().title() == null
                || !event.getView().title().toString().contains("Morphing Skin")) {
            return;
        }
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR || item.getItemMeta() == null) {
            return;
        }

        if (item.getType() == Material.BARRIER) {
            manager.resetSkin(player);
            player.sendMessage(Component.text("Your skin has been restored.").color(NamedTextColor.GREEN));
            player.closeInventory();
            return;
        }

        NamespacedKey key = new NamespacedKey(plugin, "morphingskin.name");
        String skinName = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (skinName == null) {
            return;
        }

        SkinEntry entry = manager.getSkin(skinName);
        if (entry == null) {
            return;
        }

        manager.applySkin(player, entry);
        player.sendMessage(Component.text("Skin morphed into '")
                .color(NamedTextColor.GREEN)
                .append(Component.text(entry.display()).color(NamedTextColor.GOLD))
                .append(Component.text("'. Rejoin to see the change.").color(NamedTextColor.GREEN)));
        player.closeInventory();
    }
}
