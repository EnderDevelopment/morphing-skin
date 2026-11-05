package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * MorphingSkin - lets players morph their skin into different predefined forms.
 */
public final class Main extends JavaPlugin {

    private static Main instance;
    private SkinManager skinManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        skinManager = new SkinManager(this);
        skinManager.load();

        getCommand("skin").setExecutor(new SkinCommand(this, skinManager));
        this.getServer().getPluginManager().registerEvents(new SkinListener(this, skinManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(skinManager), this);

        this.getLogger().info("MorphingSkin enabled. Loaded " + skinManager.size() + " skins.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("MorphingSkin disabled.");
    }

    public static Main getInstance() {
        return instance;
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }
}
