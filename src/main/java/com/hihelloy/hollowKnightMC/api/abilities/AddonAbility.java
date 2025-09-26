package com.hihelloy.hollowKnightMC.api.abilities;

import org.bukkit.entity.Player;

/**
 * Abstract class for creating addon abilities
 * Extend this class to create custom abilities for the plugin
 */
public abstract class AddonAbility {
    protected final String name;
    protected final String description;
    protected final String playerClass; // "knight", "hornet", or "both"
    protected final int cooldown;
    protected final int cost;
    protected final String costType; // "soul", "silk", or "none"

    public AddonAbility(String name, String description, String playerClass, int cooldown, int cost, String costType) {
        this.name = name;
        this.description = description;
        this.playerClass = playerClass;
        this.cooldown = cooldown;
        this.cost = cost;
        this.costType = costType;
    }

    /**
     * Execute the ability
     * @param player The player using the ability
     * @return true if successful
     */
    public abstract boolean execute(Player player);

    /**
     * Check if the ability can be used
     * @param player The player
     * @return true if can be used
     */
    public abstract boolean canUse(Player player);

    /**
     * Get the current cooldown for a player
     * @param player The player
     * @return cooldown in ticks
     */
    public abstract int getCurrentCooldown(Player player);

    /**
     * Get the scoreboard display text for this ability
     * @param player The player
     * @return display text
     */
    public abstract String getScoreboardDisplay(Player player);

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPlayerClass() { return playerClass; }
    public int getCooldown() { return cooldown; }
    public int getCost() { return cost; }
    public String getCostType() { return costType; }
}