package com.hihelloy.hollowKnightMC.listeners;

import com.hihelloy.hollowKnightMC.managers.HornetManager;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.MetadataValue;

public class HornetEventListener implements Listener {
    private final HornetManager hornetManager;

    public HornetEventListener(HornetManager hornetManager) {
        this.hornetManager = hornetManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        hornetManager.disableHornetAbilities(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(player);
        
        if (hornetPlayer == null) return;
        
        if (event.isSneaking()) {
            // Check for double-tap dash
            hornetPlayer.performDash();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(player);
        
        if (hornetPlayer == null) return;
        
        if (event.getAction().name().contains("LEFT_CLICK")) {
            // Throw needle on left click
            if (hornetPlayer.throwNeedle()) {
                event.setCancelled(true);
            }
        } else if (event.getAction().name().contains("RIGHT_CLICK")) {
            // Place silk trap on right click
            if (hornetPlayer.placeSilkTrap()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(player);
        
        if (hornetPlayer == null) return;
        
        // Dash invulnerability
        if (hornetPlayer.isDashing()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Handle hornet player dealing damage
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(player);
            
            if (hornetPlayer != null && event.getEntity() instanceof LivingEntity) {
                // Add silk on hit
                int silkPerHit = 2;
                hornetPlayer.addSilk(silkPerHit);
                
                // Check if target dies to give kill silk
                LivingEntity target = (LivingEntity) event.getEntity();
                if (target.getHealth() - event.getFinalDamage() <= 0) {
                    int silkPerKill = 5;
                    hornetPlayer.addSilk(silkPerKill);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        
        // Handle needle hits
        if (projectile.hasMetadata("hornet_needle")) {
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) hitEntity;
                
                for (MetadataValue meta : projectile.getMetadata("hornet_needle")) {
                    double damage = meta.asDouble();
                    target.damage(damage);
                    
                    // Add silk to caster if it's a hornet player
                    if (projectile.getShooter() instanceof Player) {
                        Player shooter = (Player) projectile.getShooter();
                        HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(shooter);
                        if (hornetPlayer != null) {
                            hornetPlayer.addSilk(2);
                            
                            // Check for kill
                            if (target.getHealth() - damage <= 0) {
                                hornetPlayer.addSilk(5);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}