package com.hihelloy.hollowKnightMC.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NoskEventListener implements Listener {
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        
        // Handle Nosk blob spit damage
        if (projectile instanceof Snowball && projectile.getShooter() != null) {
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof Player) {
                Player player = (Player) hitEntity;
                // Deal custom damage for Nosk blob
                player.damage(4.0); // 2 hearts of damage
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Handle custom damage scaling for Nosk attacks if needed
        Entity damager = event.getDamager();
        if (damager.getCustomName() != null && damager.getCustomName().contains("Nosk")) {
            // Increase damage for Nosk melee attacks
            event.setDamage(event.getDamage() * 1.5);
        }
    }
}