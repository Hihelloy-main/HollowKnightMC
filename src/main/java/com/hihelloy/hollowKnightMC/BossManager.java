package com.hihelloy.hollowKnightMC;

import org.bukkit.Location;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Map;

public class BossManager {
    private final HollowKnightMC plugin;
    private ConfigManager configManager;
    private final Map<String, Class<? extends LivingEntity>> bossTypes;

    public BossManager(HollowKnightMC plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.bossTypes = new HashMap<>();
        initializeBossTypes();
    }

    private void initializeBossTypes() {
        // Map boss names to entity types (using existing Minecraft entities as base)
        bossTypes.put("hornet", Bee.class);
        bossTypes.put("shadowknight", Enderman.class);
        bossTypes.put("mantislord", Wither.class);
        bossTypes.put("soulmaster", Ocelot.class);
        bossTypes.put("dungdefender", Bee.class);
        bossTypes.put("brokenvessel", Flying.class);
        bossTypes.put("crystalguardian", Endermite.class);
        bossTypes.put("watcherknight", org.bukkit.entity.Allay.class);
        bossTypes.put("traitorlord", Silverfish.class);
        bossTypes.put("grimm", CaveSpider.class);
    }

    public boolean spawnBoss(String bossName, Location location) {
        String configKey = "Bosses." + bossName;
        
        if (!configManager.getConfig().getBoolean(configKey + ".Enabled", false)) {
            return false;
        }

        try {
            // Spawn the base entity
            LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
            
            // Configure the boss
            configureBoss(entity, bossName, configKey);
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to spawn boss " + bossName + ": " + e.getMessage());
            return false;
        }
    }

    private void configureBoss(LivingEntity entity, String bossName, String configKey) {
        // Set basic attributes
        double health = configManager.getConfig().getDouble(configKey + ".Health", 100.0);
        double speed = configManager.getConfig().getDouble(configKey + ".Speed", 0.3);
        
        entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        entity.setHealth(health);
        entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        
        // Set custom name
        entity.setCustomName("§4§l" + bossName);
        entity.setCustomNameVisible(true);
        
        // Make persistent
        entity.setRemoveWhenFarAway(false);
        
        // Add boss metadata for event handling
        entity.setMetadata("hollow_knight_boss", new org.bukkit.metadata.FixedMetadataValue(plugin, bossName));
    }

    public void reloadConfig() {
        // Reload configuration for all bosses
        plugin.getLogger().info("Boss configuration reloaded!");
    }

    public void shutdown() {
        // Cleanup if needed
    }
}