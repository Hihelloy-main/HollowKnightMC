package com.hihelloy.hollowKnightMC.managers;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.api.abilities.AddonAbility;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final HollowKnightMC plugin;
    private final ConfigManager configManager;
    private final Map<UUID, Scoreboard> playerScoreboards;
    private BukkitTask updateTask;
    private int titleAnimation = 0;

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
        
        String title = getAnimatedTitle();
        
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

    public void updatePlayerScoreboard(Player player) {
        Scoreboard scoreboard = playerScoreboards.get(player.getUniqueId());
        if (scoreboard == null) return;
        
        Objective objective = scoreboard.getObjective("hkstats");
        if (objective == null) return;
        
        // Update title with animation
        if (configManager.getConfig().getBoolean("Scoreboard.AnimatedTitle", true)) {
            objective.setDisplayName(getAnimatedTitle());
        }
        
        // Clear existing scores
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
        
        int line = 15;
        
        // Check for Knight abilities
        KnightPlayer knightPlayer = plugin.getKnightManager().getKnightPlayer(player);
        if (knightPlayer != null) {
            line = displayKnightScoreboard(objective, player, knightPlayer, line);
        }
        
        // Check for Hornet abilities
        HornetPlayer hornetPlayer = plugin.getHornetManager().getHornetPlayer(player);
        if (hornetPlayer != null) {
            line = displayHornetScoreboard(objective, player, hornetPlayer, line);
        }
        
        // Display addon abilities
        List<AddonAbility> addonAbilities = plugin.getAddonManager().getPlayerAbilities(player);
        if (!addonAbilities.isEmpty()) {
            objective.getScore("").setScore(line--);
            objective.getScore(ChatColor.LIGHT_PURPLE + "=== ADDONS ===").setScore(line--);
            for (AddonAbility ability : addonAbilities) {
                String display = ability.getScoreboardDisplay(player);
                if (display != null && !display.isEmpty()) {
                    objective.getScore(display).setScore(line--);
                }
            }
        }
        
        // If no class selected
        if (knightPlayer == null && hornetPlayer == null) {
            objective.getScore(ChatColor.GOLD + "No Class Selected").setScore(line--);
            objective.getScore("    ").setScore(line--);
            objective.getScore(ChatColor.YELLOW + "Use /knight give").setScore(line--);
            objective.getScore(ChatColor.YELLOW + "or /hornet give").setScore(line--);
            objective.getScore(ChatColor.YELLOW + "to get abilities!").setScore(line--);
        }
    }

    private int displayKnightScoreboard(Objective objective, Player player, KnightPlayer knightPlayer, int line) {
        objective.getScore(ChatColor.BLUE + "=== ⚔ KNIGHT ⚔ ===").setScore(line--);
        objective.getScore("").setScore(line--);
        
        // Stats
        if (configManager.getConfig().getBoolean("Scoreboard.ShowStats", true)) {
            objective.getScore(ChatColor.BLUE + "Soul: " + ChatColor.WHITE + knightPlayer.getCurrentSoul() + "/" + knightPlayer.getMaxSoul()).setScore(line--);
            objective.getScore(ChatColor.RED + "Health: " + ChatColor.WHITE + Math.round(player.getHealth()) + "/" + Math.round(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue())).setScore(line--);
            objective.getScore(" ").setScore(line--);
        }
        
        // Current action with enhanced display
        if (configManager.getConfig().getBoolean("Scoreboard.ShowCurrentAction", true)) {
            String currentAction = getCurrentKnightAction(knightPlayer);
            objective.getScore(currentAction).setScore(line--);
            objective.getScore("  ").setScore(line--);
        }
        
        // Cooldowns
        if (configManager.getConfig().getBoolean("Scoreboard.ShowCooldowns", true)) {
            objective.getScore(ChatColor.GRAY + "Abilities:").setScore(line--);
            objective.getScore(getDashStatus(knightPlayer)).setScore(line--);
            objective.getScore(getFocusStatus(knightPlayer)).setScore(line--);
            objective.getScore("   ").setScore(line--);
        }
        
        // Controls
        objective.getScore(ChatColor.DARK_GRAY + "Controls:").setScore(line--);
        objective.getScore(ChatColor.WHITE + "Left: Spirit").setScore(line--);
        objective.getScore(ChatColor.WHITE + "Shift+Right: Focus").setScore(line--);
        objective.getScore(ChatColor.WHITE + "Shift x2: Dash").setScore(line--);
        
        return line;
    }

    private int displayHornetScoreboard(Objective objective, Player player, HornetPlayer hornetPlayer, int line) {
        objective.getScore(ChatColor.WHITE + "=== 🕷 HORNET 🕷 ===").setScore(line--);
        objective.getScore("").setScore(line--);
        
        // Stats
        if (configManager.getConfig().getBoolean("Scoreboard.ShowStats", true)) {
            objective.getScore(ChatColor.WHITE + "Silk: " + ChatColor.GRAY + hornetPlayer.getCurrentSilk() + "/" + hornetPlayer.getMaxSilk()).setScore(line--);
            objective.getScore(ChatColor.RED + "Health: " + ChatColor.WHITE + Math.round(player.getHealth()) + "/" + Math.round(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue())).setScore(line--);
            objective.getScore(" ").setScore(line--);
        }
        
        // Current action
        if (configManager.getConfig().getBoolean("Scoreboard.ShowCurrentAction", true)) {
            String currentAction = getCurrentHornetAction(hornetPlayer);
            objective.getScore(currentAction).setScore(line--);
            objective.getScore("  ").setScore(line--);
        }
        
        // Cooldowns
        if (configManager.getConfig().getBoolean("Scoreboard.ShowCooldowns", true)) {
            objective.getScore(ChatColor.GRAY + "Abilities:").setScore(line--);
            objective.getScore(getHornetDashStatus(hornetPlayer)).setScore(line--);
            objective.getScore(getNeedleStatus(hornetPlayer)).setScore(line--);
            objective.getScore("   ").setScore(line--);
        }
        
        // Controls
        objective.getScore(ChatColor.DARK_GRAY + "Controls:").setScore(line--);
        objective.getScore(ChatColor.WHITE + "Left: Needle").setScore(line--);
        objective.getScore(ChatColor.WHITE + "Right: Silk Trap").setScore(line--);
        objective.getScore(ChatColor.WHITE + "Shift x2: Dash").setScore(line--);
        
        return line;
    }

    private String getCurrentKnightAction(KnightPlayer knightPlayer) {
        if (knightPlayer.isDashing()) {
            return ChatColor.YELLOW + "⚡ " + ChatColor.BOLD + "DASHING" + ChatColor.RESET + ChatColor.YELLOW + " ⚡";
        } else if (knightPlayer.isFocusing()) {
            return ChatColor.LIGHT_PURPLE + "✦ " + ChatColor.BOLD + "FOCUSING" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + " ✦";
        } else if (knightPlayer.isCrystalDashing()) {
            return ChatColor.AQUA + "◆ " + ChatColor.BOLD + "CRYSTAL DASH" + ChatColor.RESET + ChatColor.AQUA + " ◆";
        } else {
            return ChatColor.GREEN + "● " + ChatColor.BOLD + "READY" + ChatColor.RESET + ChatColor.GREEN + " ●";
        }
    }

    private String getCurrentHornetAction(HornetPlayer hornetPlayer) {
        if (hornetPlayer.isDashing()) {
            return ChatColor.YELLOW + "⚡ " + ChatColor.BOLD + "DASHING" + ChatColor.RESET + ChatColor.YELLOW + " ⚡";
        } else {
            return ChatColor.GREEN + "● " + ChatColor.BOLD + "READY" + ChatColor.RESET + ChatColor.GREEN + " ●";
        }
    }

    private String getDashStatus(KnightPlayer knightPlayer) {
        // This would need to be implemented in KnightPlayer to track cooldowns
        return ChatColor.GRAY + "Dash: " + ChatColor.GREEN + "Ready";
    }

    private String getFocusStatus(KnightPlayer knightPlayer) {
        if (knightPlayer.isFocusing()) {
            return ChatColor.GRAY + "Focus: " + ChatColor.YELLOW + "Casting";
        }
        return ChatColor.GRAY + "Focus: " + ChatColor.GREEN + "Ready";
    }

    private String getHornetDashStatus(HornetPlayer hornetPlayer) {
        return ChatColor.GRAY + "Dash: " + ChatColor.GREEN + "Ready";
    }

    private String getNeedleStatus(HornetPlayer hornetPlayer) {
        return ChatColor.GRAY + "Needle: " + ChatColor.GREEN + "Ready";
    }

    private String getAnimatedTitle() {
        String baseTitle = configManager.getConfig().getString("Plugin.ScoreboardTitle", "§6§l⚔ HOLLOW KNIGHT ⚔");
        
        if (!configManager.getConfig().getBoolean("Scoreboard.AnimatedTitle", true)) {
            return ChatColor.translateAlternateColorCodes('&', baseTitle);
        }
        
        // Simple color animation
        String[] colors = {"§6", "§e", "§f", "§e"};
        String color = colors[titleAnimation % colors.length];
        
        return ChatColor.translateAlternateColorCodes('&', color + "§l⚔ HOLLOW KNIGHT ⚔");
    }

    private void startUpdateTask() {
        int interval = configManager.getConfig().getInt("Scoreboard.UpdateInterval", 5);
        
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            titleAnimation++;
            
            for (UUID uuid : playerScoreboards.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    updatePlayerScoreboard(player);
                }
            }
        }, 0L, interval);
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