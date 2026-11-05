package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the /skin command:
 *  - /skin            opens the skin selection menu
 *  - /skin <name>     applies a specific skin immediately
 *  - /skin reset      restores the player's original skin
 */
public class SkinCommand implements CommandExecutor {

    private final Main plugin;
    private final SkinManager manager;

    public SkinCommand(Main plugin, SkinManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED));
            return true;
        }
        if (!player.hasPermission("morphingskin.use")) {
            player.sendMessage(Component.text("You do not have permission to do that.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            new SkinMenu(plugin, manager).open(player);
            return true;
        }

        String name = args[0].toLowerCase();
        if (name.equals("reset")) {
            manager.resetSkin(player);
            player.sendMessage(Component.text("Your skin has been restored.").color(NamedTextColor.GREEN));
            return true;
        }

        SkinEntry entry = manager.getSkin(name);
        if (entry == null) {
            player.sendMessage(Component.text("Unknown skin '").color(NamedTextColor.RED)
                    .append(Component.text(name).color(NamedTextColor.WHITE))
                    .append(Component.text("'. Available: /skin to open the menu.").color(NamedTextColor.RED)));
            return true;
        }

        manager.applySkin(player, entry);
        player.sendMessage(Component.text("Skin morphed into '").color(NamedTextColor.GREEN)
                .append(Component.text(entry.display()).color(NamedTextColor.GOLD))
                .append(Component.text("'. Rejoin to see the change.").color(NamedTextColor.GREEN)));
        return true;
    }
}
