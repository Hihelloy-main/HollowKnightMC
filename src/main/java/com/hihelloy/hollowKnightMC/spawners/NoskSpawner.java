package com.hihelloy.hollowKnightMC.spawners;

import com.hihelloy.hollowKnightMC.entities.Nosk;
import com.hihelloy.hollowKnightMC.managers.ConfigManager;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Entity;

public class NoskSpawner {
    
    public static void spawnNosk(Location location, ConfigManager configManager) {
        try {
            // Create the custom Nosk entity
            Nosk nosk = new Nosk(location, configManager);
            
            // Add to world
            ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(nosk);
            
            // Get the Bukkit entity
            Entity bukkitEntity = nosk.getBukkitEntity();
            
            // Start shapeshift task
            nosk.startShapeshiftTask(bukkitEntity);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to spawn Nosk", e);
        }
    }
}