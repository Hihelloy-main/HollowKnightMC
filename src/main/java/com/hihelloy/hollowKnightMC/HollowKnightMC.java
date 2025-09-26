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
    private ClassSelectionManager classSelectionManager;
    private AddonManager addonManager;
    private HollowKnightAPI api;

    @Override
    public void onEnable() {
        plugin = this;
        
        // Initialize configuration
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        
        // Initialize managers
        classSelectionManager = new ClassSelectionManager(this, configManager);
        knightManager = new KnightManager(this, configManager);
        hornetManager = new HornetManager(this, configManager);
        bossManager = new BossManager(this, configManager);
        scoreboardManager = new ScoreboardManager(this, configManager);
        addonManager = new AddonManager(this);
        
        // Initialize API
        api = new HollowKnightAPI(this);
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
        
        // Load addons
        addonManager.loadAddons();
        
        getLogger().info("HollowKnightMC v2.0.0 has been enabled!");
        getLogger().info("Knight and Hornet abilities are now available!");
        getLogger().info("All 25 Hollow Knight and Silksong bosses are loaded!");
        getLogger().info("Addon system initialized - place addon JARs in plugins/HollowKnightMC/abilities/");
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
        if (classSelectionManager != null) {
            classSelectionManager.shutdown();
        }
        if (addonManager != null) {
            addonManager.shutdown();
        }
        getLogger().info("HollowKnightMC has been disabled!");
    }

    private void registerCommands() {
        getCommand("nosk").setExecutor(new NoskCommand(configManager));
        getCommand("hkmc").setExecutor(new MainCommand(this, configManager, bossManager));
        getCommand("hkselect").setExecutor(new ClassSelectionCommand(classSelectionManager));
        getCommand("hkabilities").setExecutor(new AbilitiesCommand(this));
        getCommand("knight").setExecutor(new KnightCommand(knightManager));
        getCommand("hornet").setExecutor(new HornetCommand(hornetManager));
        
        // Set tab completers
        getCommand("hkmc").setTabCompleter(new MainCommand(this, configManager, bossManager));
        getCommand("hkselect").setTabCompleter(new ClassSelectionCommand(classSelectionManager));
        getCommand("knight").setTabCompleter(new KnightCommand(knightManager));
        getCommand("hornet").setTabCompleter(new HornetCommand(hornetManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new NoskEventListener(), this);
        getServer().getPluginManager().registerEvents(new KnightEventListener(knightManager), this);
        getServer().getPluginManager().registerEvents(new HornetEventListener(hornetManager), this);
        getServer().getPluginManager().registerEvents(new BossEventListener(bossManager), this);
        getServer().getPluginManager().registerEvents(new ClassSelectionListener(classSelectionManager), this);
    }

    // Getters
    public ConfigManager getConfigManager() { return configManager; }
    public KnightManager getKnightManager() { return knightManager; }
    public HornetManager getHornetManager() { return hornetManager; }
    public BossManager getBossManager() { return bossManager; }
    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public ClassSelectionManager getClassSelectionManager() { return classSelectionManager; }
    public AddonManager getAddonManager() { return addonManager; }
    public HollowKnightAPI getAPI() { return api; }
}