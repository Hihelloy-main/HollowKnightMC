package com.hihelloy.hollowKnightMC;

import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;

public class NoskSpawner {
    
    public static void spawnNosk(Location location, ConfigManager configManager) {
        // Create the custom Nosk entity
        Nosk nosk = new Nosk(location, configManager);
        
        // Add to world
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(nosk);
        
        // Get the bukkit entity for the disguise system
        org.bukkit.entity.Entity bukkitEntity = nosk.getBukkitEntity();
        
        // Start the shapeshift task
        if (bukkitEntity instanceof LivingEntity) {
            nosk.startShapeshiftTask(bukkitEntity);
        }
        
        // Set spawn location
        nosk.setPos(location.getX(), location.getY(), location.getZ());
    }
}