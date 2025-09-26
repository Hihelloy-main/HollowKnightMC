package com.hihelloy.hollowKnightMC.api;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.api.abilities.AddonAbility;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Main API class for HollowKnightMC plugin
 * Provides access to all plugin features for external addons
 */
public class HollowKnightAPI {
    private final HollowKnightMC plugin;

    public HollowKnightAPI(HollowKnightMC plugin) {
        this.plugin = plugin;
    }

    /**
     * Get a player's Knight abilities
     * @param player The player
     * @return KnightPlayer instance or null if not a knight
     */
    public KnightPlayer getKnightPlayer(Player player) {
        return plugin.getKnightManager().getKnightPlayer(player);
    }

    /**
     * Get a player's Hornet abilities
     * @param player The player
     * @return HornetPlayer instance or null if not hornet
     */
    public HornetPlayer getHornetPlayer(Player player) {
        return plugin.getHornetManager().getHornetPlayer(player);
    }

    /**
     * Enable Knight abilities for a player
     * @param player The player
     * @return true if successful
     */
    public boolean enableKnightAbilities(Player player) {
        return plugin.getKnightManager().enableKnightAbilities(player);
    }

    /**
     * Enable Hornet abilities for a player
     * @param player The player
     * @return true if successful
     */
    public boolean enableHornetAbilities(Player player) {
        return plugin.getHornetManager().enableHornetAbilities(player);
    }

    /**
     * Spawn a boss at a location
     * @param bossName The boss name
     * @param player The player to spawn near
     * @return true if successful
     */
    public boolean spawnBoss(String bossName, Player player) {
        return plugin.getBossManager().spawnBoss(bossName, player.getLocation());
    }

    /**
     * Register a custom addon ability
     * @param ability The addon ability to register
     * @return true if successful
     */
    public boolean registerAddonAbility(AddonAbility ability) {
        return plugin.getAddonManager().registerAbility(ability);
    }

    /**
     * Get all registered addon abilities for a player
     * @param player The player
     * @return List of addon abilities
     */
    public List<AddonAbility> getPlayerAddonAbilities(Player player) {
        return plugin.getAddonManager().getPlayerAbilities(player);
    }

    /**
     * Trigger an addon ability
     * @param player The player
     * @param abilityName The ability name
     * @return true if successful
     */
    public boolean triggerAddonAbility(Player player, String abilityName) {
        return plugin.getAddonManager().triggerAbility(player, abilityName);
    }

    /**
     * Update scoreboard for a player (useful for addon abilities)
     * @param player The player
     */
    public void updateScoreboard(Player player) {
        plugin.getScoreboardManager().updatePlayerScoreboard(player);
    }
}