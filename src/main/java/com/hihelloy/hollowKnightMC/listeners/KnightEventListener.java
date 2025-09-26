package com.hihelloy.hollowKnightMC.listeners;

import com.hihelloy.hollowKnightMC.managers.KnightManager;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
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

public class KnightEventListener implements Listener {
    private final KnightManager knightManager;

    public KnightEventListener(KnightManager knightManager) {
        this.knightManager = knightManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        knightManager.disableKnightAbilities(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
        
        if (knightPlayer == null) return;
        
        if (event.isSneaking()) {
            // Check for double-tap dash
            knightPlayer.performDash();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
        
        if (knightPlayer == null) return;
        
        if (event.getAction().name().contains("LEFT_CLICK")) {
            // Cast vengeful spirit on left click
            if (knightPlayer.castVengefulSpirit()) {
                event.setCancelled(true);
            }
        } else if (event.getAction().name().contains("RIGHT_CLICK")) {
            if (player.isSneaking()) {
                // Start focus healing
                if (knightPlayer.startFocus()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
        
        if (knightPlayer == null) return;
        
        // Cancel focus if player moves while focusing
        if (knightPlayer.isFocusing() && 
            (event.getFrom().distance(event.getTo()) > 0.1)) {
            knightPlayer.cancelFocus();
        }
        
        // Check for wall jump (simplified - when player jumps while sneaking near wall)
        if (player.isSneaking() && player.getVelocity().getY() > 0) {
            knightPlayer.performWallJump();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
        
        if (knightPlayer == null) return;
        
        // Dash invulnerability
        if (knightPlayer.isDashing()) {
            event.setCancelled(true);
            return;
        }
        
        // Cancel focus on damage
        if (knightPlayer.isFocusing()) {
            knightPlayer.cancelFocus();
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Handle knight player dealing damage
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
            
            if (knightPlayer != null && event.getEntity() instanceof LivingEntity) {
                // Add soul on hit
                int soulPerHit = 3;
                knightPlayer.addSoul(soulPerHit);
                
                // Check if target dies to give kill soul
                LivingEntity target = (LivingEntity) event.getEntity();
                if (target.getHealth() - event.getFinalDamage() <= 0) {
                    int soulPerKill = 11;
                    knightPlayer.addSoul(soulPerKill);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        
        // Handle vengeful spirit hits
        if (projectile.hasMetadata("vengeful_spirit")) {
            Entity hitEntity = event.getHitEntity();
            if (hitEntity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) hitEntity;
                
                for (MetadataValue meta : projectile.getMetadata("vengeful_spirit")) {
                    double damage = meta.asDouble();
                    target.damage(damage);
                    
                    // Add soul to caster if it's a knight player
                    if (projectile.getShooter() instanceof Player) {
                        Player shooter = (Player) projectile.getShooter();
                        KnightPlayer knightPlayer = knightManager.getKnightPlayer(shooter);
                        if (knightPlayer != null) {
                            knightPlayer.addSoul(3);
                            
                            // Check for kill
                            if (target.getHealth() - damage <= 0) {
                                knightPlayer.addSoul(11);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}