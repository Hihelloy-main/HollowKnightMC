package com.hihelloy.hollowKnightMC.commands;

import com.hihelloy.hollowKnightMC.managers.ClassSelectionManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassSelectionCommand implements CommandExecutor, TabCompleter {
    private final ClassSelectionManager classSelectionManager;

    public ClassSelectionCommand(ClassSelectionManager classSelectionManager) {
        this.classSelectionManager = classSelectionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can select classes!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.GOLD + "=== Class Selection ===");
            player.sendMessage(ChatColor.YELLOW + "/hkselect knight - Become the Knight");
            player.sendMessage(ChatColor.YELLOW + "/hkselect hornet - Become Hornet");
            
            String currentClass = classSelectionManager.getPlayerClass(player);
            if (currentClass != null) {
                player.sendMessage(ChatColor.GRAY + "Current class: " + ChatColor.WHITE + currentClass);
            }
            return true;
        }

        String className = args[0];
        if (classSelectionManager.selectClass(player, className)) {
            // Success message is handled in ClassSelectionManager
        } else {
            player.sendMessage(ChatColor.RED + "Invalid class! Choose 'knight' or 'hornet'");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> classes = Arrays.asList("knight", "hornet");
            for (String className : classes) {
                if (className.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(className);
                }
            }
        }
        
        return completions;
    }
}