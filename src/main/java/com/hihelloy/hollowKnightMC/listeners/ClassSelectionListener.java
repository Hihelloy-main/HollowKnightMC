package com.hihelloy.hollowKnightMC.listeners;

import com.hihelloy.hollowKnightMC.managers.ClassSelectionManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ClassSelectionListener implements Listener {
    private final ClassSelectionManager classSelectionManager;

    public ClassSelectionListener(ClassSelectionManager classSelectionManager) {
        this.classSelectionManager = classSelectionManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (!player.hasPermission("hollowknightmc.use")) {
            return;
        }
        
        if (classSelectionManager.requiresClassSelection() && !classSelectionManager.hasSelectedClass(player)) {
            player.sendMessage(ChatColor.GOLD + "Welcome to Hollow Knight MC!");
            player.sendMessage(ChatColor.YELLOW + "Choose your class: /hkselect <knight|hornet>");
            player.sendMessage(ChatColor.GRAY + "Use /hkabilities to see what each class can do");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        classSelectionManager.removePlayer(event.getPlayer());
    }
}