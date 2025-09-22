package com.hihelloy.hollowKnightMC;

import com.hihelloy.hollowKnightMC.api.HollowKnightAPI;
import com.hihelloy.hollowKnightMC.commands.*;
import com.hihelloy.hollowKnightMC.listeners.*;
import com.hihelloy.hollowKnightMC.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class HollowKnightMC extends JavaPlugin {
    public static HollowKnightMC plugin;
    private ConfigManager configManager;
    private KnightManager knightManager;
    private HornetManager hornetManager;
    private BossManager bossManager;
    private ScoreboardManager scoreboardManager;
    private HollowKnightAPI api;

    @Override
    public void onEnable() {
        plugin = this;
        
        // Initialize configuration
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        
        // Initialize managers
        knightManager = new KnightManager(this, configManager);
        hornetManager = new HornetManager(this, configManager);
        bossManager = new BossManager(this, configManager);
        scoreboardManager = new ScoreboardManager(this, configManager);
        
        // Initialize API
        api = new HollowKnightAPI(this);
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
        
        getLogger().info("HollowKnightMC v2.0.0 has been enabled!");
        getLogger().info("Knight and Hornet abilities are now available!");
        getLogger().info("All Hollow Knight and Silksong bosses are loaded!");
    }

    @Override
    public void onDisable() {
        if (knightManager != null) {
            knightManager.shutdown();
        }
        if (hornetManager != null) {
            hornetManager.shutdown();
        }
        if (bossManager != null) {
            bossManager.shutdown();
        }
        if (scoreboardManager != null) {
            scoreboardManager.shutdown();
        }
        getLogger().info("HollowKnightMC has been disabled!");
    }

    private void registerCommands() {
        getCommand("nosk").setExecutor(new NoskCommand(configManager));
        getCommand("knight").setExecutor(new KnightCommand(knightManager));
        getCommand("hornet").setExecutor(new HornetCommand(hornetManager));
        getCommand("hkmc").setExecutor(new MainCommand(this, configManager, bossManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new NoskEventListener(), this);
        getServer().getPluginManager().registerEvents(new KnightEventListener(knightManager), this);
        getServer().getPluginManager().registerEvents(new HornetEventListener(hornetManager), this);
        getServer().getPluginManager().registerEvents(new BossEventListener(bossManager), this);
    }

    // Getters
    public ConfigManager getConfigManager() { return configManager; }
    public KnightManager getKnightManager() { return knightManager; }
    public HornetManager getHornetManager() { return hornetManager; }
    public BossManager getBossManager() { return bossManager; }
    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public HollowKnightAPI getAPI() { return api; }
}