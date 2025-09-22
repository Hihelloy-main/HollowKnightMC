package com.hihelloy.hollowKnightMC.commands;

import com.hihelloy.hollowKnightMC.managers.HornetManager;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HornetCommand implements CommandExecutor, TabCompleter {
    private final HornetManager hornetManager;

    public HornetCommand(HornetManager hornetManager) {
        this.hornetManager = hornetManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Toggle hornet abilities
            if (hornetManager.hasHornetAbilities(player)) {
                hornetManager.disableHornetAbilities(player);
                player.sendMessage(ChatColor.YELLOW + "Hornet abilities disabled!");
            } else {
                if (!player.hasPermission("hollowknightmc.hornet")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use Hornet abilities!");
                    return true;
                }
                if (hornetManager.enableHornetAbilities(player)) {
                    player.sendMessage(ChatColor.GREEN + "Hornet abilities enabled!");
                    player.sendMessage(ChatColor.GRAY + "Controls:");
                    player.sendMessage(ChatColor.GRAY + "- Double-tap SHIFT: Dash");
                    player.sendMessage(ChatColor.GRAY + "- Left-click: Needle Throw");
                    player.sendMessage(ChatColor.GRAY + "- Right-click: Silk Trap");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to enable Hornet abilities!");
                }
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "silk":
                if (!hornetManager.hasHornetAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Hornet abilities enabled!");
                    return true;
                }
                HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(player);
                if (hornetPlayer != null) {
                    player.sendMessage(ChatColor.WHITE + "Silk: " + hornetPlayer.getCurrentSilk() + "/" + hornetPlayer.getMaxSilk());
                }
                break;

            case "reload":
                if (!player.hasPermission("hollowknightmc.admin")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to reload the config!");
                    return true;
                }
                hornetManager.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Hornet configuration reloaded!");
                break;

            case "toggle":
                // Same as no args
                return onCommand(sender, command, label, new String[0]);

            default:
                player.sendMessage(ChatColor.RED + "Usage: /hornet [toggle|silk|reload]");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("toggle", "silk", "reload"));
        }
        
        return completions;
    }
}