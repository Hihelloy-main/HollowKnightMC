package com.hihelloy.hollowKnightMC;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KnightManager {
    private final HollowKnightMC plugin;
    private final ConfigManager configManager;
    private final Map<UUID, KnightPlayer> knightPlayers;
    private BukkitTask updateTask;

    public KnightManager(HollowKnightMC plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.knightPlayers = new HashMap<>();
        startUpdateTask();
    }

    public void enableKnightAbilities(Player player) {
        if (!configManager.getConfig().getBoolean("Knight.Enabled", true)) {
            return;
        }

        UUID uuid = player.getUniqueId();
        KnightPlayer knightPlayer = new KnightPlayer(player, configManager);
        knightPlayers.put(uuid, knightPlayer);
        
        applyKnightAttributes(player);
        knightPlayer.initializeSoul();
        
        // Create scoreboard
        plugin.getScoreboardManager().createScoreboard(player);
    }

    public void disableKnightAbilities(Player player) {
        UUID uuid = player.getUniqueId();
        knightPlayers.remove(uuid);
        resetPlayerAttributes(player);
        
        // Remove scoreboard
        plugin.getScoreboardManager().removeScoreboard(player);
    }

    public KnightPlayer getKnightPlayer(Player player) {
        return knightPlayers.get(player.getUniqueId());
    }

    public boolean hasKnightAbilities(Player player) {
        return knightPlayers.containsKey(player.getUniqueId());
    }

    private void applyKnightAttributes(Player player) {
        // Set health
        double maxHealth = configManager.getConfig().getDouble("Knight.MaxHealth", 18.0);
        double startingHealth = configManager.getConfig().getDouble("Knight.StartingHealth", 10.0);
        
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        player.setHealth(Math.min(startingHealth, maxHealth));
        
        // Set movement speed
        float walkSpeed = (float) configManager.getConfig().getDouble("Knight.WalkSpeed", 0.25);
        float flySpeed = (float) configManager.getConfig().getDouble("Knight.FlySpeed", 0.1);
        
        player.setWalkSpeed(walkSpeed);
        player.setFlySpeed(flySpeed);
        
        // Apply jump boost
        int jumpBoost = configManager.getConfig().getInt("Knight.JumpBoost", 2);
        if (jumpBoost > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, jumpBoost - 1, false, false));
        }
        
        // Set combat attributes
        double meleeDamage = configManager.getConfig().getDouble("Knight.Combat.MeleeDamage", 3.0);
        double attackSpeed = configManager.getConfig().getDouble("Knight.Combat.AttackSpeed", 1.5);
        double knockbackResistance = configManager.getConfig().getDouble("Knight.Combat.KnockbackResistance", 0.2);
        
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(meleeDamage);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(knockbackResistance);
    }

    private void resetPlayerAttributes(Player player) {
        // Reset to default values
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20.0);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        
        // Remove potion effects
        player.removePotionEffect(PotionEffectType.JUMP);
        
        // Reset combat attributes
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.0);
    }

    private void startUpdateTask() {
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (KnightPlayer knightPlayer : knightPlayers.values()) {
                knightPlayer.update();
            }
        }, 0L, 1L); // Run every tick
    }

    public void shutdown() {
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
        }
        
        // Reset all players
        for (UUID uuid : knightPlayers.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                resetPlayerAttributes(player);
            }
        }
        knightPlayers.clear();
    }

    public void reloadConfig() {
        configManager.reloadConfig();
        
        // Reapply attributes to all knight players
        for (Map.Entry<UUID, KnightPlayer> entry : knightPlayers.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                applyKnightAttributes(player);
                entry.getValue().reloadConfig(configManager);
            }
        }
    }
}