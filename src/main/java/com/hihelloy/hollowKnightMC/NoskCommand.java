package com.hihelloy.hollowKnightMC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoskCommand implements CommandExecutor, TabCompleter {
    public static ConfigManager configManager;

    private final List<String> subCommands = List.of("spawn");

    public NoskCommand(ConfigManager configManager) {
        NoskCommand.configManager = configManager;
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

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /nosk <spawn|kill|reload>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "spawn":
                try {
                    NoskSpawner.spawnNosk(player.getLocation(), configManager);
                    player.sendMessage(ChatColor.GREEN + "A Nosk has been spawned!");
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "Failed to spawn Nosk: " + e.getMessage());
                    e.printStackTrace();
                }
                break;



            default:
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Use: /nosk spawn");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String current = args[0].toLowerCase();
            for (String sub : subCommands) {
                if (sub.startsWith(current)) {
                    completions.add(sub);
                }
            }
        }

        return completions;
    }
}
