package com.hihelloy.hollowKnightMC.managers;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final HollowKnightMC plugin;
    private final ConfigManager configManager;
    private final Map<UUID, Scoreboard> playerScoreboards;
    private BukkitTask updateTask;

    public ScoreboardManager(HollowKnightMC plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerScoreboards = new HashMap<>();
        startUpdateTask();
    }

    public void createScoreboard(Player player) {
        if (!configManager.getConfig().getBoolean("Plugin.ShowScoreboard", true)) return;
        
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;
        
        Scoreboard scoreboard = manager.getNewScoreboard();
        
        String title = ChatColor.translateAlternateColorCodes('&', 
            configManager.getConfig().getString("Plugin.ScoreboardTitle", "§6§lHollow Knight Stats"));
        
        Objective objective = scoreboard.registerNewObjective("hkstats", "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        playerScoreboards.put(player.getUniqueId(), scoreboard);
        player.setScoreboard(scoreboard);
        
        updatePlayerScoreboard(player);
    }

    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            player.setScoreboard(manager.getMainScoreboard());
        }
    }

    private void updatePlayerScoreboard(Player player) {
        Scoreboard scoreboard = playerScoreboards.get(player.getUniqueId());
        if (scoreboard == null) return;
        
        Objective objective = scoreboard.getObjective("hkstats");
        if (objective == null) return;
        
        // Clear existing scores
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
        
        int line = 10;
        
        // Check for Knight abilities
        KnightPlayer knightPlayer = plugin.getKnightManager().getKnightPlayer(player);
        if (knightPlayer != null) {
            objective.getScore(ChatColor.BLUE + "Soul: " + knightPlayer.getCurrentSoul() + "/" + knightPlayer.getMaxSoul()).setScore(line--);
            objective.getScore(ChatColor.RED + "Health: " + Math.round(player.getHealth()) + "/" + Math.round(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue())).setScore(line--);
            objective.getScore("").setScore(line--);
            
            objective.getScore(ChatColor.GREEN + "Knight Abilities:").setScore(line--);
            objective.getScore(knightPlayer.isDashing() ? ChatColor.YELLOW + "⚡ Dashing" : ChatColor.GRAY + "⚡ Dash Ready").setScore(line--);
            objective.getScore(knightPlayer.isFocusing() ? ChatColor.LIGHT_PURPLE + "✦ Focusing" : ChatColor.GRAY + "✦ Focus Ready").setScore(line--);
        }
        
        // Check for Hornet abilities
        HornetPlayer hornetPlayer = plugin.getHornetManager().getHornetPlayer(player);
        if (hornetPlayer != null) {
            objective.getScore(ChatColor.WHITE + "Silk: " + hornetPlayer.getCurrentSilk() + "/" + hornetPlayer.getMaxSilk()).setScore(line--);
            objective.getScore(ChatColor.RED + "Health: " + Math.round(player.getHealth()) + "/" + Math.round(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue())).setScore(line--);
            objective.getScore(" ").setScore(line--);
            
            objective.getScore(ChatColor.GREEN + "Hornet Abilities:").setScore(line--);
            objective.getScore(hornetPlayer.isDashing() ? ChatColor.YELLOW + "⚡ Dashing" : ChatColor.GRAY + "⚡ Dash Ready").setScore(line--);
        }
        
        if (knightPlayer != null || hornetPlayer != null) {
            objective.getScore("  ").setScore(line--);
            objective.getScore(ChatColor.GOLD + "Controls:").setScore(line--);
            if (knightPlayer != null) {
                objective.getScore(ChatColor.WHITE + "Left Click: Spirit").setScore(line--);
                objective.getScore(ChatColor.WHITE + "Shift+Right: Focus").setScore(line--);
            }
            if (hornetPlayer != null) {
                objective.getScore(ChatColor.WHITE + "Left Click: Needle").setScore(line--);
                objective.getScore(ChatColor.WHITE + "Right Click: Silk").setScore(line--);
            }
            objective.getScore(ChatColor.WHITE + "Shift+Shift: Dash").setScore(line--);
        }
    }

    private void startUpdateTask() {
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (UUID uuid : playerScoreboards.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    updatePlayerScoreboard(player);
                }
            }
        }, 0L, 20L); // Update every second
    }

    public void shutdown() {
        if (updateTask != null && !updateTask.isCancelled()) {
            updateTask.cancel();
        }
        
        // Remove all scoreboards
        for (UUID uuid : playerScoreboards.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                removeScoreboard(player);
            }
        }
        playerScoreboards.clear();
    }
}