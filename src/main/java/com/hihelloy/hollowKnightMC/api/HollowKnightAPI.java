package com.hihelloy.hollowKnightMC.api;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.entity.Player;

/**
 * Main API class for HollowKnightMC plugin
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
}