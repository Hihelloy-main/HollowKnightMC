package com.hihelloy.hollowKnightMC.managers;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HornetManager {
    private final HollowKnightMC plugin;
    private final ConfigManager configManager;
    private final Map<UUID, HornetPlayer> hornetPlayers;
    private BukkitTask updateTask;

    public HornetManager(HollowKnightMC plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.hornetPlayers = new HashMap<>();
        startUpdateTask();
    }

    public boolean enableHornetAbilities(Player player) {
        if (!configManager.getConfig().getBoolean("Hornet.Enabled", true)) {
            return false;
        }

        UUID uuid = player.getUniqueId();
        HornetPlayer hornetPlayer = new HornetPlayer(player, configManager);
        hornetPlayers.put(uuid, hornetPlayer);
        
        applyHornetAttributes(player);
        hornetPlayer.initializeSilk();
        
        // Create scoreboard
        plugin.getScoreboardManager().createScoreboard(player);
        
        return true;
    }

    public void disableHornetAbilities(Player player) {
        UUID uuid = player.getUniqueId();
        hornetPlayers.remove(uuid);
        resetPlayerAttributes(player);
        
        // Remove scoreboard
        plugin.getScoreboardManager().removeScoreboard(player);
    }

    public HornetPlayer getHornetPlayer(Player player) {
        return hornetPlayers.get(player.getUniqueId());
    }

    public boolean hasHornetAbilities(Player player) {
        return hornetPlayers.containsKey(player.getUniqueId());
    }

    private void applyHornetAttributes(Player player) {
        // Set health
        double maxHealth = configManager.getConfig().getDouble("Hornet.MaxHealth", 16.0);
        double startingHealth = configManager.getConfig().getDouble("Hornet.StartingHealth", 12.0);
        
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        player.setHealth(Math.min(startingHealth, maxHealth));
        
        // Set movement speed (Hornet is faster than Knight)
        float walkSpeed = (float) configManager.getConfig().getDouble("Hornet.WalkSpeed", 0.3);
        float flySpeed = (float) configManager.getConfig().getDouble("Hornet.FlySpeed", 0.15);
        
        player.setWalkSpeed(walkSpeed);
        player.setFlySpeed(flySpeed);
        
        // Apply jump boost (Hornet has higher jumps)
        int jumpBoost = configManager.getConfig().getInt("Hornet.JumpBoost", 3);
        if (jumpBoost > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, jumpBoost - 1, false, false));
        }
        
        // Set combat attributes
        double meleeDamage = configManager.getConfig().getDouble("Hornet.Combat.MeleeDamage", 4.0);
        double attackSpeed = configManager.getConfig().getDouble("Hornet.Combat.AttackSpeed", 2.0);
        double knockbackResistance = configManager.getConfig().getDouble("Hornet.Combat.KnockbackResistance", 0.1);
        
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
            for (HornetPlayer hornetPlayer : hornetPlayers.values()) {
                hornetPlayer.update();
            }
        }, 0L, 1L); // Run every tick
    }

    public void shutdown() {
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
        }
        
        // Reset all players
        for (UUID uuid : hornetPlayers.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                resetPlayerAttributes(player);
            }
        }
        hornetPlayers.clear();
    }

    public void reloadConfig() {
        configManager.reloadConfig();
        
        // Reapply attributes to all hornet players
        for (Map.Entry<UUID, HornetPlayer> entry : hornetPlayers.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                applyHornetAttributes(player);
                entry.getValue().reloadConfig(configManager);
            }
        }
    }
}