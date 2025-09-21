package com.hihelloy.hollowKnightMC;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

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
        bossTypes.put("hornet", Zombie.class);
        bossTypes.put("shadowknight", Zombie.class);
        bossTypes.put("mantislord", Zombie.class);
        bossTypes.put("soulmaster", Zombie.class);
        bossTypes.put("dungdefender", Zombie.class);
        bossTypes.put("brokenvessel", Zombie.class);
        bossTypes.put("crystalguardian", Zombie.class);
        bossTypes.put("watcherknight", Zombie.class);
        bossTypes.put("traitorlord", Zombie.class);
        bossTypes.put("grimm", Zombie.class);
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