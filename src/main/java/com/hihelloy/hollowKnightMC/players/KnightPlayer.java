package com.hihelloy.hollowKnightMC.players;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.managers.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class KnightPlayer {
    private final Player player;
    private ConfigManager configManager;
    
    // Soul system
    private int currentSoul;
    private int maxSoul;
    
    // Cooldowns (in ticks)
    private int dashCooldown = 0;
    private int wallJumpCooldown = 0;
    private int focusCooldown = 0;
    private int vengefulSpiritCooldown = 0;
    
    // States
    private boolean isFocusing = false;
    private int focusTimer = 0;
    private boolean isDashing = false;
    private int dashInvulnerabilityTimer = 0;
    private boolean hasDoubleJumped = false;

    public KnightPlayer(Player player, ConfigManager configManager) {
        this.player = player;
        this.configManager = configManager;
        this.maxSoul = configManager.getConfig().getInt("Knight.Soul.MaxSoul", 99);
        this.currentSoul = configManager.getConfig().getInt("Knight.Soul.StartingSoul", 33);
    }

    public void initializeSoul() {
        updateSoulDisplay();
    }

    public void update() {
        // Update cooldowns
        if (dashCooldown > 0) dashCooldown--;
        if (wallJumpCooldown > 0) wallJumpCooldown--;
        if (focusCooldown > 0) focusCooldown--;
        if (vengefulSpiritCooldown > 0) vengefulSpiritCooldown--;
        if (dashInvulnerabilityTimer > 0) dashInvulnerabilityTimer--;
        
        // Reset double jump when on ground
        if (player.isOnGround()) {
            hasDoubleJumped = false;
        }
        
        // Handle focus healing
        if (isFocusing) {
            focusTimer--;
            if (focusTimer <= 0) {
                completeFocus();
            } else {
                // Show healing particles
                if (configManager.getConfig().getBoolean("Knight.Effects.HealingParticles", true)) {
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 1);
                }
            }
        }
        
        // Update soul display
        updateSoulDisplay();
    }

    public boolean performDash() {
        if (!configManager.getConfig().getBoolean("Knight.Dash.Enabled", true)) return false;
        if (dashCooldown > 0) return false;
        
        double distance = configManager.getConfig().getDouble("Knight.Dash.Distance", 8.0);
        double height = configManager.getConfig().getDouble("Knight.Dash.Height", 0.3);
        int cooldown = configManager.getConfig().getInt("Knight.Dash.Cooldown", 40);
        int invulnerabilityTicks = configManager.getConfig().getInt("Knight.Dash.InvulnerabilityTicks", 10);
        
        Vector direction = player.getLocation().getDirection().normalize();
        direction.multiply(distance / 10.0); // Adjust for smooth movement
        direction.setY(height);
        
        player.setVelocity(direction);
        dashCooldown = cooldown;
        isDashing = true;
        dashInvulnerabilityTimer = invulnerabilityTicks;
        
        // Effects
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.5f);
        if (configManager.getConfig().getBoolean("Knight.Effects.DashParticles", true)) {
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
        }
        
        return true;
    }

    public boolean performWallJump() {
        if (!configManager.getConfig().getBoolean("Knight.WallJump.Enabled", true)) return false;
        if (wallJumpCooldown > 0) return false;
        
        // Check if player is near a wall
        Location loc = player.getLocation();
        boolean nearWall = false;
        Vector wallDirection = new Vector();
        
        // Check surrounding blocks
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                
                Block block = loc.clone().add(x, 0, z).getBlock();
                if (block.getType().isSolid()) {
                    nearWall = true;
                    wallDirection = new Vector(-x, 0, -z).normalize();
                    break;
                }
            }
            if (nearWall) break;
        }
        
        if (!nearWall) return false;
        
        double jumpHeight = configManager.getConfig().getDouble("Knight.WallJump.Height", 1.2);
        double horizontalBoost = configManager.getConfig().getDouble("Knight.WallJump.HorizontalBoost", 0.8);
        int cooldown = configManager.getConfig().getInt("Knight.WallJump.Cooldown", 20);
        
        Vector velocity = wallDirection.multiply(horizontalBoost);
        velocity.setY(jumpHeight);
        
        player.setVelocity(velocity);
        wallJumpCooldown = cooldown;
        
        // Effects
        player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.0f, 1.2f);
        
        return true;
    }

    public boolean startFocus() {
        if (!configManager.getConfig().getBoolean("Knight.Soul.Focus.Enabled", true)) return false;
        if (isFocusing || focusCooldown > 0) return false;
        
        int soulCost = configManager.getConfig().getInt("Knight.Soul.Focus.SoulCost", 33);
        if (currentSoul < soulCost) return false;
        
        int castTime = configManager.getConfig().getInt("Knight.Soul.Focus.CastTime", 60);
        
        currentSoul -= soulCost;
        isFocusing = true;
        focusTimer = castTime;
        
        // Effects
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
        
        return true;
    }

    private void completeFocus() {
        if (!isFocusing) return;
        
        double healAmount = configManager.getConfig().getDouble("Knight.Soul.Focus.HealAmount", 2.0);
        double newHealth = Math.min(player.getHealth() + healAmount, player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
        
        player.setHealth(newHealth);
        isFocusing = false;
        focusCooldown = 100; // 5 second cooldown
        
        // Effects
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        if (configManager.getConfig().getBoolean("Knight.Effects.HealingParticles", true)) {
            player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 5, 0.5, 0.5, 0.5, 0);
        }
    }

    public void cancelFocus() {
        if (isFocusing) {
            isFocusing = false;
            focusTimer = 0;
            // Refund half the soul cost
            int soulCost = configManager.getConfig().getInt("Knight.Soul.Focus.SoulCost", 33);
            addSoul(soulCost / 2);
        }
    }

    public boolean castVengefulSpirit() {
        if (!configManager.getConfig().getBoolean("Knight.Soul.VengefulSpirit.Enabled", true)) return false;
        if (vengefulSpiritCooldown > 0) return false;
        
        int soulCost = configManager.getConfig().getInt("Knight.Soul.VengefulSpirit.SoulCost", 11);
        if (currentSoul < soulCost) return false;
        
        double damage = configManager.getConfig().getDouble("Knight.Soul.VengefulSpirit.Damage", 4.0);
        double speed = configManager.getConfig().getDouble("Knight.Soul.VengefulSpirit.Speed", 2.0);
        
        currentSoul -= soulCost;
        vengefulSpiritCooldown = 20; // 1 second cooldown
        
        // Create projectile
        Projectile spirit = player.launchProjectile(Snowball.class);
        spirit.setVelocity(player.getLocation().getDirection().multiply(speed));
        spirit.setMetadata("vengeful_spirit", new FixedMetadataValue(HollowKnightMC.plugin, damage));
        spirit.setMetadata("knight_projectile", new FixedMetadataValue(HollowKnightMC.plugin, true));
        
        // Effects
        player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.5f);
        if (configManager.getConfig().getBoolean("Knight.Effects.SoulParticles", true)) {
            player.getWorld().spawnParticle(Particle.SOUL, player.getLocation().add(0, 1, 0), 5, 0.2, 0.2, 0.2, 0.1);
        }
        
        return true;
    }

    public void addSoul(int amount) {
        currentSoul = Math.min(currentSoul + amount, maxSoul);
        updateSoulDisplay();
        
        if (configManager.getConfig().getBoolean("Knight.Effects.SoulParticles", true)) {
            player.getWorld().spawnParticle(Particle.SOUL, player.getLocation().add(0, 1, 0), 2);
        }
    }

    private void updateSoulDisplay() {
        // Update experience bar to show soul
        float soulPercentage = (float) currentSoul / maxSoul;
        player.setExp(soulPercentage);
        player.setLevel(currentSoul);
    }

    public void reloadConfig(ConfigManager configManager) {
        this.configManager = configManager;
        this.maxSoul = configManager.getConfig().getInt("Knight.Soul.MaxSoul", 99);
        this.currentSoul = Math.min(this.currentSoul, this.maxSoul);
    }

    // Getters
    public int getCurrentSoul() { return currentSoul; }
    public int getMaxSoul() { return maxSoul; }
    public boolean isDashing() { return isDashing && dashInvulnerabilityTimer > 0; }
    public boolean isFocusing() { return isFocusing; }
    public Player getPlayer() { return player; }
}