package com.hihelloy.hollowKnightMC.commands;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilitiesCommand implements CommandExecutor {
    private final HollowKnightMC plugin;

    public AbilitiesCommand(HollowKnightMC plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can view abilities!");
            return true;
        }

        Player player = (Player) sender;
        String playerClass = plugin.getClassSelectionManager().getPlayerClass(player);

        if (playerClass == null) {
            player.sendMessage(ChatColor.RED + "You haven't selected a class yet!");
            player.sendMessage(ChatColor.YELLOW + "Use /hkselect <knight|hornet> to choose your class");
            return true;
        }

        showDetailedAbilities(player, playerClass);
        return true;
    }

    private void showDetailedAbilities(Player player, String className) {
        player.sendMessage(ChatColor.GOLD + "=== " + className.toUpperCase() + " ABILITIES ===");
        
        if (className.equals("knight")) {
            player.sendMessage(ChatColor.BLUE + "⚔ KNIGHT ABILITIES ⚔");
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
            
        } else if (className.equals("hornet")) {
            player.sendMessage(ChatColor.WHITE + "🕷 HORNET ABILITIES 🕷");
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
        }
        
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "Use /hkmc to see more commands!");
    }
}