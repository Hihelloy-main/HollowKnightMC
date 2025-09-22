package com.hihelloy.hollowKnightMC.managers;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class BossManager {
    private final HollowKnightMC plugin;
    private ConfigManager configManager;
    private final Map<String, EntityType> bossTypes;

    public BossManager(HollowKnightMC plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.bossTypes = new HashMap<>();
        initializeBossTypes();
    }

    private void initializeBossTypes() {
        // Hollow Knight bosses
        bossTypes.put("hornet", EntityType.ZOMBIE);
        bossTypes.put("shadowknight", EntityType.WITHER_SKELETON);
        bossTypes.put("mantislord", EntityType.ZOMBIE);
        bossTypes.put("soulmaster", EntityType.PHANTOM);
        bossTypes.put("dungdefender", EntityType.ZOMBIE);
        bossTypes.put("brokenvessel", EntityType.ZOMBIE);
        bossTypes.put("crystalguardian", EntityType.IRON_GOLEM);
        bossTypes.put("watcherknight", EntityType.IRON_GOLEM);
        bossTypes.put("traitorlord", EntityType.ZOMBIE);
        bossTypes.put("grimm", EntityType.BLAZE);
        bossTypes.put("radiance", EntityType.PHANTOM);
        bossTypes.put("nightmarekingrimm", EntityType.BLAZE);
        bossTypes.put("absoluteradiance", EntityType.PHANTOM);
        bossTypes.put("purevessel", EntityType.WITHER_SKELETON);
        bossTypes.put("sistesofbattle", EntityType.ZOMBIE);
        
        // Silksong bosses
        bossTypes.put("lace", EntityType.ZOMBIE);
        bossTypes.put("moss", EntityType.ZOMBIE);
        bossTypes.put("sharpe", EntityType.SKELETON);
        bossTypes.put("gannet", EntityType.PHANTOM);
        bossTypes.put("carmelita", EntityType.SPIDER);
        bossTypes.put("macabre", EntityType.WITHER_SKELETON);
        bossTypes.put("krow", EntityType.PHANTOM);
        bossTypes.put("bonebottom", EntityType.SKELETON);
        bossTypes.put("seth", EntityType.BLAZE);
        bossTypes.put("coral", EntityType.GUARDIAN);
    }

    public boolean spawnBoss(String bossName, Location location) {
        String configKey = "Bosses." + bossName;
        
        if (!configManager.getConfig().getBoolean(configKey + ".Enabled", false)) {
            return false;
        }

        EntityType entityType = bossTypes.get(bossName.toLowerCase());
        if (entityType == null) {
            return false;
        }

        try {
            // Spawn the base entity
            LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
            
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

    public String[] getAllBossNames() {
        return bossTypes.keySet().toArray(new String[0]);
    }

    public void reloadConfig() {
        // Reload configuration for all bosses
        plugin.getLogger().info("Boss configuration reloaded!");
    }

    public void shutdown() {
        // Cleanup if needed
    }
}