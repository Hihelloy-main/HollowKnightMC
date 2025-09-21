package com.hihelloy.hollowKnightMC;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final HollowKnightMC plugin;
    private FileConfiguration config;

    public ConfigManager(HollowKnightMC plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        Bukkit.getLogger().info("HollowKnightMC " + plugin.getDescription().getVersion() + " reloaded!");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        plugin.saveConfig();
    }
}