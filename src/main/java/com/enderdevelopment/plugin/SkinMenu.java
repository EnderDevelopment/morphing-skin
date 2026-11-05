package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Builds a chest menu where the player picks from all configured morphing skins.
 */
public class SkinMenu {

    public static final String GUI_TITLE = "Morphing Skin";

    private final Main plugin;
    private final SkinManager manager;

    public SkinMenu(Main plugin, SkinManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void open(Player player) {
        int count = manager.size();
        int rows = Math.max(1, (int) Math.ceil((count + 1) / 9.0));
        rows = Math.min(rows, 6);
        Inventory inv = Bukkit.createInventory(null, rows * 9, Component.text(GUI_TITLE).color(NamedTextColor.DARK_PURPLE));

        int slot = 0;
        for (SkinEntry entry : manager.getAllSkins()) {
            if (slot >= rows * 9) {
                break;
            }
            inv.setItem(slot++, buildSkinIcon(player, entry));
        }

        // Reset button in the last slot.
        if (slot < rows * 9) {
            ItemStack reset = new ItemStack(Material.BARRIER);
            ItemMeta meta = reset.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("Reset Skin").color(NamedTextColor.RED));
                meta.lore(List.of(
                        Component.text("Click to restore your").color(NamedTextColor.GRAY),
                        Component.text("original skin.").color(NamedTextColor.GRAY)
                ));
                reset.setItemMeta(meta);
            }
            inv.setItem(rows * 9 - 1, reset);
        }

        player.openInventory(inv);
    }

    private ItemStack buildSkinIcon(Player viewer, SkinEntry entry) {
        Material material = entry.material() != null && Material.matchMaterial(entry.material()) != null
                ? Material.matchMaterial(entry.material()) : Material.PLAYER_HEAD;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        meta.displayName(Component.text(entry.display()).color(NamedTextColor.LIGHT_PURPLE));

        // Store the skin name so clicks resolve reliably.
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "morphingskin.name"),
                PersistentDataType.STRING,
                entry.name()
        );

        boolean active = entry.name().equals(manager.getActiveSkin(viewer.getUniqueId()));
        meta.lore(List.of(
                Component.text("Skin: " + entry.name()).color(NamedTextColor.GRAY),
                Component.text(active ? "Currently equipped" : "Click to morph")
                        .color(active ? NamedTextColor.GREEN : NamedTextColor.YELLOW)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
