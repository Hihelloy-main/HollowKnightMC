package com.hihelloy.hollowKnightMC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KnightCommand implements CommandExecutor {
    private final KnightManager knightManager;

    public KnightCommand(KnightManager knightManager) {
        this.knightManager = knightManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Toggle knight abilities
            if (knightManager.hasKnightAbilities(player)) {
                knightManager.disableKnightAbilities(player);
                player.sendMessage(ChatColor.YELLOW + "Knight abilities disabled!");
            } else {
                if (!player.hasPermission("hollowknightmc.knight")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use Knight abilities!");
                    return true;
                }
                knightManager.enableKnightAbilities(player);
                player.sendMessage(ChatColor.GREEN + "Knight abilities enabled!");
                player.sendMessage(ChatColor.GRAY + "Controls:");
                player.sendMessage(ChatColor.GRAY + "- Double-tap SHIFT: Dash");
                player.sendMessage(ChatColor.GRAY + "- SHIFT + SPACE (near wall): Wall Jump");
                player.sendMessage(ChatColor.GRAY + "- Hold SHIFT + Right-click: Focus (heal)");
                player.sendMessage(ChatColor.GRAY + "- Right-click: Vengeful Spirit");
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "soul":
                if (!knightManager.hasKnightAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Knight abilities enabled!");
                    return true;
                }
                KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
                player.sendMessage(ChatColor.BLUE + "Soul: " + knightPlayer.getCurrentSoul() + "/" + knightPlayer.getMaxSoul());
                break;

            case "reload":
                if (!player.hasPermission("hollowknightmc.knight.admin")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to reload the config!");
                    return true;
                }
                knightManager.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Knight configuration reloaded!");
                break;

            case "toggle":
                // Same as no args
                return onCommand(sender, command, label, new String[0]);

            default:
                player.sendMessage(ChatColor.RED + "Usage: /knight [toggle|soul|reload]");
                break;
        }

        return true;
    }
}