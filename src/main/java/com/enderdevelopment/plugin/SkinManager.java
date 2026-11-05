package com.enderdevelopment.plugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Loads skins from the config, tracks each player's active skin and
 * applies skin textures to player profiles.
 */
public class SkinManager {

    private final Main plugin;
    private final Map<String, SkinEntry> skins = new HashMap<>();
    private final Map<UUID, String> active = new HashMap<>();

    public SkinManager(Main plugin) {
        this.plugin = plugin;
    }

    /** Loads all skins declared in the config. */
    public void load() {
        skins.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("skins");
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            ConfigurationSection s = section.getConfigurationSection(key);
            if (s == null) {
                continue;
            }
            String name = key.toLowerCase();
            SkinEntry entry = new SkinEntry(
                    name,
                    s.getString("display", key),
                    s.getString("material", "PLAYER_HEAD"),
                    s.getString("texture", ""),
                    s.getString("signature", null)
            );
            skins.put(name, entry);
        }
    }

    public int size() {
        return skins.size();
    }

    public List<SkinEntry> getAllSkins() {
        return new ArrayList<>(skins.values());
    }

    public SkinEntry getSkin(String name) {
        return skins.get(name.toLowerCase());
    }

    /** Returns the name of the active skin for a player, or null. */
    public String getActiveSkin(UUID uuid) {
        return active.get(uuid);
    }

    public void setActive(UUID uuid, String skinName) {
        active.put(uuid, skinName);
    }

    public void clearActive(UUID uuid) {
        active.remove(uuid);
    }

    /**
     * Applies the given skin to the player's profile. The change becomes
     * visibly effective once the player reconnects / respawns.
     */
    public void applySkin(Player player, SkinEntry entry) {
        if (entry == null || entry.texture() == null || entry.texture().isBlank()) {
            return;
        }
        Part part = parseSkinPayload(entry.texture());
        if (part == null || part.url.isBlank()) {
            return;
        }
        try {
            com.destroystokyo.paper.profile.PlayerProfile profile = player.getPlayerProfile();
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(new URL(part.url), part.model);
            profile.setTextures(textures);
            player.setPlayerProfile(profile);
            setActive(player.getUniqueId(), entry.name());
        } catch (Exception e) {
            player.sendMessage(org.bukkit.ChatColor.RED + "Could not apply skin '" + entry.display() + "'.");
        }
    }

    /** Resets the player to their original skin. */
    public void resetSkin(Player player) {
        player.setPlayerProfile(player.getPlayerProfile().clone());
        clearActive(player.getUniqueId());
    }

    /**
     * Parses the base64 encoded skin payload to extract the texture url and model.
     */
    private static Part parseSkinPayload(String textureValue) {
        try {
            String json = new String(java.util.Base64.getDecoder().decode(textureValue),
                    java.nio.charset.StandardCharsets.UTF_8);
            String url = "";
            int urlIdx = json.indexOf("\"url\":\"");
            if (urlIdx >= 0) {
                int start = urlIdx + "\"url\":\"".length();
                int end = json.indexOf('"', start);
                if (end > start) {
                    url = json.substring(start, end);
                }
            }
            org.bukkit.profile.PlayerTextures.SkinModel model =
                    json.contains("\"model\":\"slim\"")
                            ? org.bukkit.profile.PlayerTextures.SkinModel.SLIM
                            : org.bukkit.profile.PlayerTextures.SkinModel.CLASSIC;
            return new Part(url, model);
        } catch (Exception e) {
            return null;
        }
    }

    private record Part(String url, org.bukkit.profile.PlayerTextures.SkinModel model) {
    }
}
