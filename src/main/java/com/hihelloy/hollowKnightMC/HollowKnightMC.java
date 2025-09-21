package com.hihelloy.hollowKnightMC;

import org.bukkit.plugin.java.JavaPlugin;

public final class HollowKnightMC extends JavaPlugin {
    public static HollowKnightMC plugin;
    private ConfigManager configManager;
    private KnightManager knightManager;
    private BossManager bossManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        plugin = this;
        
        // Initialize configuration
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        
        // Initialize managers
        knightManager = new KnightManager(this, configManager);
        bossManager = new BossManager(this, configManager);
        scoreboardManager = new ScoreboardManager(this, configManager);
        
        // Initialize Knight manager
        knightManager = new KnightManager(this, configManager);
        
        // Register commands
        getCommand("nosk").setExecutor(new NoskCommand(configManager));
        getCommand("knight").setExecutor(new KnightCommand(knightManager));
        getCommand("hkmc").setExecutor(new MainCommand(this, configManager, bossManager));
        getCommand("knight").setExecutor(new KnightCommand(knightManager));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new NoskEventListener(), this);
        getServer().getPluginManager().registerEvents(new KnightEventListener(knightManager), this);
        getServer().getPluginManager().registerEvents(new BossEventListener(bossManager), this);
        getServer().getPluginManager().registerEvents(new KnightEventListener(knightManager), this);
        
        getLogger().info("HollowKnightMC has been enabled!");
        getLogger().info("Nosk entities can now be spawned with /nosk command");
        getLogger().info("Knight abilities are now available for players!");
        getLogger().info("10 Hollow Knight bosses are now available!");
        getLogger().info("Knight abilities are now available for players!");
    }

    @Override
    public void onDisable() {
        if (knightManager != null) {
            knightManager.shutdown();
        }
        if (bossManager != null) {
            bossManager.shutdown();
        }
        if (scoreboardManager != null) {
            scoreboardManager.shutdown();
        }
        if (knightManager != null) {
            knightManager.shutdown();
        }
        getLogger().info("HollowKnightMC has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    
    public BossManager getBossManager() {
        return bossManager;
    }
    
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    
    public KnightManager getKnightManager() {
        return knightManager;
    }
}