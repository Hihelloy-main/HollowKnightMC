package com.hihelloy.hollowKnightMC.commands;

import com.hihelloy.hollowKnightMC.managers.ConfigManager;
import com.hihelloy.hollowKnightMC.spawners.NoskSpawner;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoskCommand implements CommandExecutor {
    private final ConfigManager configManager;

    public NoskCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("hollowknightmc.nosk")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        try {
            NoskSpawner.spawnNosk(player.getLocation(), configManager);
            player.sendMessage(ChatColor.GREEN + "A Nosk has been spawned!");
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Failed to spawn Nosk: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}