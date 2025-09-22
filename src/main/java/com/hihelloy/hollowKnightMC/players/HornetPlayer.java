package com.hihelloy.hollowKnightMC.players;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.managers.ConfigManager;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class HornetPlayer {
    private final Player player;
    private ConfigManager configManager;
    
    // Silk system
    private int currentSilk;
    private int maxSilk;
    
    // Cooldowns (in ticks)
    private int dashCooldown = 0;
    private int needleThrowCooldown = 0;
    private int silkTrapCooldown = 0;
    
    // States
    private boolean isDashing = false;
    private int dashInvulnerabilityTimer = 0;

    public HornetPlayer(Player player, ConfigManager configManager) {
        this.player = player;
        this.configManager = configManager;
        this.maxSilk = configManager.getConfig().getInt("Hornet.Silk.MaxSilk", 50);
        this.currentSilk = configManager.getConfig().getInt("Hornet.Silk.StartingSilk", 25);
    }

    public void initializeSilk() {
        updateSilkDisplay();
    }

    public void update() {
        // Update cooldowns
        if (dashCooldown > 0) dashCooldown--;
        if (needleThrowCooldown > 0) needleThrowCooldown--;
        if (silkTrapCooldown > 0) silkTrapCooldown--;
        if (dashInvulnerabilityTimer > 0) dashInvulnerabilityTimer--;
        
        // Regenerate silk slowly
        if (currentSilk < maxSilk && player.getTicksLived() % 40 == 0) { // Every 2 seconds
            currentSilk = Math.min(currentSilk + 1, maxSilk);
        }
        
        // Update silk display
        updateSilkDisplay();
    }

    public boolean performDash() {
        if (!configManager.getConfig().getBoolean("Hornet.Dash.Enabled", true)) return false;
        if (dashCooldown > 0) return false;
        
        double distance = configManager.getConfig().getDouble("Hornet.Dash.Distance", 10.0);
        double height = configManager.getConfig().getDouble("Hornet.Dash.Height", 0.4);
        int cooldown = configManager.getConfig().getInt("Hornet.Dash.Cooldown", 30);
        int invulnerabilityTicks = configManager.getConfig().getInt("Hornet.Dash.InvulnerabilityTicks", 8);
        
        Vector direction = player.getLocation().getDirection().normalize();
        direction.multiply(distance / 8.0); // Adjust for smooth movement
        direction.setY(height);
        
        player.setVelocity(direction);
        dashCooldown = cooldown;
        isDashing = true;
        dashInvulnerabilityTimer = invulnerabilityTicks;
        
        // Effects
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.2f);
        if (configManager.getConfig().getBoolean("Hornet.Effects.DashParticles", true)) {
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 8, 0.3, 0.3, 0.3, 0.1);
        }
        
        return true;
    }

    public boolean throwNeedle() {
        if (!configManager.getConfig().getBoolean("Hornet.NeedleThrow.Enabled", true)) return false;
        if (needleThrowCooldown > 0) return false;
        
        int silkCost = configManager.getConfig().getInt("Hornet.NeedleThrow.SilkCost", 5);
        if (currentSilk < silkCost) return false;
        
        double damage = configManager.getConfig().getDouble("Hornet.NeedleThrow.Damage", 5.0);
        double speed = configManager.getConfig().getDouble("Hornet.NeedleThrow.Speed", 2.5);
        
        currentSilk -= silkCost;
        needleThrowCooldown = 15; // 0.75 second cooldown
        
        // Create projectile
        Projectile needle = player.launchProjectile(Snowball.class);
        needle.setVelocity(player.getLocation().getDirection().multiply(speed));
        needle.setMetadata("hornet_needle", new FixedMetadataValue(HollowKnightMC.plugin, damage));
        needle.setMetadata("hornet_projectile", new FixedMetadataValue(HollowKnightMC.plugin, true));
        
        // Effects
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.8f);
        if (configManager.getConfig().getBoolean("Hornet.Effects.SilkParticles", true)) {
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation().add(0, 1, 0), 3, 0.1, 0.1, 0.1, 0.05);
        }
        
        return true;
    }

    public boolean placeSilkTrap() {
        if (!configManager.getConfig().getBoolean("Hornet.SilkTrap.Enabled", true)) return false;
        if (silkTrapCooldown > 0) return false;
        
        int silkCost = configManager.getConfig().getInt("Hornet.SilkTrap.SilkCost", 15);
        if (currentSilk < silkCost) return false;
        
        currentSilk -= silkCost;
        silkTrapCooldown = 100; // 5 second cooldown
        
        // Place web block at player's location
        player.getLocation().getBlock().setType(org.bukkit.Material.COBWEB);
        
        // Effects
        player.playSound(player.getLocation(), Sound.BLOCK_WOOL_PLACE, 1.0f, 0.8f);
        if (configManager.getConfig().getBoolean("Hornet.Effects.SilkParticles", true)) {
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.1);
        }
        
        return true;
    }

    public void addSilk(int amount) {
        currentSilk = Math.min(currentSilk + amount, maxSilk);
        updateSilkDisplay();
        
        if (configManager.getConfig().getBoolean("Hornet.Effects.SilkParticles", true)) {
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation().add(0, 1, 0), 2);
        }
    }

    private void updateSilkDisplay() {
        // Update experience bar to show silk
        float silkPercentage = (float) currentSilk / maxSilk;
        player.setExp(silkPercentage);
        player.setLevel(currentSilk);
    }

    public void reloadConfig(ConfigManager configManager) {
        this.configManager = configManager;
        this.maxSilk = configManager.getConfig().getInt("Hornet.Silk.MaxSilk", 50);
        this.currentSilk = Math.min(this.currentSilk, this.maxSilk);
    }

    // Getters
    public int getCurrentSilk() { return currentSilk; }
    public int getMaxSilk() { return maxSilk; }
    public boolean isDashing() { return isDashing && dashInvulnerabilityTimer > 0; }
    public Player getPlayer() { return player; }
}