package com.hihelloy.hollowKnightMC.commands;

import com.hihelloy.hollowKnightMC.managers.KnightManager;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnightCommand implements CommandExecutor, TabCompleter {
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
            player.sendMessage(ChatColor.GOLD + "=== Knight Commands ===");
            player.sendMessage(ChatColor.YELLOW + "/knight give - Get Knight abilities");
            player.sendMessage(ChatColor.YELLOW + "/knight remove - Remove Knight abilities");
            player.sendMessage(ChatColor.YELLOW + "/knight abilities - View Knight abilities");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "give":
                if (knightManager.hasKnightAbilities(player)) {
                    player.sendMessage(ChatColor.YELLOW + "You already have Knight abilities!");
                    return true;
                }
                
                if (knightManager.enableKnightAbilities(player)) {
                    player.sendMessage(ChatColor.BLUE + "⚔ You are now a Knight! ⚔");
                    player.sendMessage(ChatColor.GRAY + "Use /knight abilities to see your powers");
                    player.sendMessage(ChatColor.GRAY + "Controls:");
                    player.sendMessage(ChatColor.GRAY + "- Double-tap SHIFT: Dash");
                    player.sendMessage(ChatColor.GRAY + "- LEFT CLICK: Vengeful Spirit");
                    player.sendMessage(ChatColor.GRAY + "- SHIFT + RIGHT CLICK: Focus (heal)");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to give Knight abilities!");
                }
                break;

            case "remove":
                if (!knightManager.hasKnightAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Knight abilities!");
                    return true;
                }
                
                knightManager.disableKnightAbilities(player);
                player.sendMessage(ChatColor.YELLOW + "Knight abilities removed!");
                break;

            case "abilities":
                if (!knightManager.hasKnightAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Knight abilities! Use /knight give first");
                    return true;
                }
                
                showKnightAbilities(player);
                break;

            case "soul":
                if (!knightManager.hasKnightAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Knight abilities!");
                    return true;
                }
                KnightPlayer knightPlayer = knightManager.getKnightPlayer(player);
                if (knightPlayer != null) {
                    player.sendMessage(ChatColor.BLUE + "Soul: " + knightPlayer.getCurrentSoul() + "/" + knightPlayer.getMaxSoul());
                }
                break;

            default:
                player.sendMessage(ChatColor.RED + "Usage: /knight [give|remove|abilities|soul]");
                break;
        }

        return true;
    }

    private void showKnightAbilities(Player player) {
        player.sendMessage(ChatColor.BLUE + "=== ⚔ KNIGHT ABILITIES ⚔ ===");
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_BLUE + "Soul System:");
        player.sendMessage(ChatColor.GRAY + "• Gain soul from hitting enemies (3 soul)");
        player.sendMessage(ChatColor.GRAY + "• Gain extra soul from kills (11 soul)");
        player.sendMessage(ChatColor.GRAY + "• Soul displayed on XP bar");
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_BLUE + "Movement Abilities:");
        player.sendMessage(ChatColor.YELLOW + "• Dash" + ChatColor.GRAY + " - Double-tap SHIFT");
        player.sendMessage(ChatColor.GRAY + "  Dash forward with invulnerability");
        player.sendMessage(ChatColor.YELLOW + "• Wall Jump" + ChatColor.GRAY + " - SHIFT + SPACE near wall");
        player.sendMessage(ChatColor.GRAY + "  Jump off walls for extra mobility");
        player.sendMessage(ChatColor.YELLOW + "• Double Jump" + ChatColor.GRAY + " - SPACE in mid-air");
        player.sendMessage(ChatColor.GRAY + "  Additional jump while airborne");
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_BLUE + "Combat Abilities:");
        player.sendMessage(ChatColor.YELLOW + "• Vengeful Spirit" + ChatColor.GRAY + " - LEFT CLICK");
        player.sendMessage(ChatColor.GRAY + "  Ranged soul projectile (11 soul)");
        player.sendMessage(ChatColor.YELLOW + "• Focus" + ChatColor.GRAY + " - SHIFT + RIGHT CLICK");
        player.sendMessage(ChatColor.GRAY + "  Heal yourself (33 soul)");
        player.sendMessage(ChatColor.YELLOW + "• Crystal Dash" + ChatColor.GRAY + " - Hold SHIFT");
        player.sendMessage(ChatColor.GRAY + "  Charged long-distance dash");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("give", "remove", "abilities", "soul");
            for (String subCmd : subCommands) {
                if (subCmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCmd);
                }
            }
        }
        
        return completions;
    }
}