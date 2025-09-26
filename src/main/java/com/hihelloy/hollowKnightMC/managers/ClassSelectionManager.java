package com.hihelloy.hollowKnightMC.managers;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClassSelectionManager {
    private final HollowKnightMC plugin;
    private final ConfigManager configManager;
    private final Map<UUID, String> playerClasses;

    public ClassSelectionManager(HollowKnightMC plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerClasses = new HashMap<>();
    }

    public boolean selectClass(Player player, String className) {
        UUID uuid = player.getUniqueId();
        String currentClass = playerClasses.get(uuid);
        
        // Remove current abilities if any
        if (currentClass != null) {
            if (currentClass.equals("knight")) {
                plugin.getKnightManager().disableKnightAbilities(player);
            } else if (currentClass.equals("hornet")) {
                plugin.getHornetManager().disableHornetAbilities(player);
            }
        }
        
        // Set new class
        playerClasses.put(uuid, className);
        
        // Enable new abilities
        switch (className.toLowerCase()) {
            case "knight":
                if (plugin.getKnightManager().enableKnightAbilities(player)) {
                    player.sendMessage(ChatColor.BLUE + "You are now a Knight!");
                    player.sendMessage(ChatColor.GRAY + "Use /hkabilities to see your abilities");
                    return true;
                }
                break;
            case "hornet":
                if (plugin.getHornetManager().enableHornetAbilities(player)) {
                    player.sendMessage(ChatColor.WHITE + "You are now Hornet!");
                    player.sendMessage(ChatColor.GRAY + "Use /hkabilities to see your abilities");
                    return true;
                }
                break;
            default:
                player.sendMessage(ChatColor.RED + "Invalid class! Choose 'knight' or 'hornet'");
                return false;
        }
        
        return false;
    }

    public String getPlayerClass(Player player) {
        return playerClasses.get(player.getUniqueId());
    }

    public boolean hasSelectedClass(Player player) {
        return playerClasses.containsKey(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        playerClasses.remove(player.getUniqueId());
    }

    public boolean requiresClassSelection() {
        return configManager.getConfig().getBoolean("Plugin.RequireClassSelection", true);
    }

    public void shutdown() {
        playerClasses.clear();
    }
}