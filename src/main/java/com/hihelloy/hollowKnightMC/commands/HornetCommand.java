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
            player.sendMessage(ChatColor.GOLD + "=== Hornet Commands ===");
            player.sendMessage(ChatColor.YELLOW + "/hornet give - Get Hornet abilities");
            player.sendMessage(ChatColor.YELLOW + "/hornet remove - Remove Hornet abilities");
            player.sendMessage(ChatColor.YELLOW + "/hornet abilities - View Hornet abilities");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "give":
                if (hornetManager.hasHornetAbilities(player)) {
                    player.sendMessage(ChatColor.YELLOW + "You already have Hornet abilities!");
                    return true;
                }
                
                if (hornetManager.enableHornetAbilities(player)) {
                    player.sendMessage(ChatColor.WHITE + "🕷 You are now Hornet! 🕷");
                    player.sendMessage(ChatColor.GRAY + "Use /hornet abilities to see your powers");
                    player.sendMessage(ChatColor.GRAY + "Controls:");
                    player.sendMessage(ChatColor.GRAY + "- Double-tap SHIFT: Dash");
                    player.sendMessage(ChatColor.GRAY + "- LEFT CLICK: Needle Throw");
                    player.sendMessage(ChatColor.GRAY + "- RIGHT CLICK: Silk Trap");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to give Hornet abilities!");
                }
                break;

            case "remove":
                if (!hornetManager.hasHornetAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Hornet abilities!");
                    return true;
                }
                
                hornetManager.disableHornetAbilities(player);
                player.sendMessage(ChatColor.YELLOW + "Hornet abilities removed!");
                break;

            case "abilities":
                if (!hornetManager.hasHornetAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Hornet abilities! Use /hornet give first");
                    return true;
                }
                
                showHornetAbilities(player);
                break;

            case "silk":
                if (!hornetManager.hasHornetAbilities(player)) {
                    player.sendMessage(ChatColor.RED + "You don't have Hornet abilities!");
                    return true;
                }
                HornetPlayer hornetPlayer = hornetManager.getHornetPlayer(player);
                if (hornetPlayer != null) {
                    player.sendMessage(ChatColor.WHITE + "Silk: " + hornetPlayer.getCurrentSilk() + "/" + hornetPlayer.getMaxSilk());
                }
                break;

            default:
                player.sendMessage(ChatColor.RED + "Usage: /hornet [give|remove|abilities|silk]");
                break;
        }

        return true;
    }

    private void showHornetAbilities(Player player) {
        player.sendMessage(ChatColor.WHITE + "=== 🕷 HORNET ABILITIES 🕷 ===");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Silk System:");
        player.sendMessage(ChatColor.GRAY + "• Gain silk from hitting enemies (2 silk)");
        player.sendMessage(ChatColor.GRAY + "• Gain extra silk from kills (5 silk)");
        player.sendMessage(ChatColor.GRAY + "• Silk regenerates slowly over time");
        player.sendMessage(ChatColor.GRAY + "• Silk displayed on XP bar");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Movement Abilities:");
        player.sendMessage(ChatColor.YELLOW + "• Dash" + ChatColor.GRAY + " - Double-tap SHIFT");
        player.sendMessage(ChatColor.GRAY + "  Faster dash than Knight with shorter cooldown");
        player.sendMessage(ChatColor.YELLOW + "• Enhanced Speed" + ChatColor.GRAY + " - Passive");
        player.sendMessage(ChatColor.GRAY + "  Higher movement speed and jump height");
        player.sendMessage("");
        player.sendMessage(ChatColor.GRAY + "Combat Abilities:");
        player.sendMessage(ChatColor.YELLOW + "• Needle Throw" + ChatColor.GRAY + " - LEFT CLICK");
        player.sendMessage(ChatColor.GRAY + "  Ranged silk needle (5 silk)");
        player.sendMessage(ChatColor.YELLOW + "• Silk Trap" + ChatColor.GRAY + " - RIGHT CLICK");
        player.sendMessage(ChatColor.GRAY + "  Place web trap (15 silk)");
        player.sendMessage(ChatColor.YELLOW + "• Silk Lash" + ChatColor.GRAY + " - SHIFT + LEFT CLICK");
        player.sendMessage(ChatColor.GRAY + "  Melee silk combo (8 silk)");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("give", "remove", "abilities", "silk");
            for (String subCmd : subCommands) {
                if (subCmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCmd);
                }
            }
        }
        
        return completions;
    }
}