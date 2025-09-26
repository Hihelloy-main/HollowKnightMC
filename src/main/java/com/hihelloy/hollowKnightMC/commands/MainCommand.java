package com.hihelloy.hollowKnightMC.commands;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.managers.BossManager;
import com.hihelloy.hollowKnightMC.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final HollowKnightMC plugin;
    private final ConfigManager configManager;
    private final BossManager bossManager;

    public MainCommand(HollowKnightMC plugin, ConfigManager configManager, BossManager bossManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.bossManager = bossManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "=== HollowKnightMC Commands ===");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc select <knight|hornet> - Select your class");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc abilities - View your abilities");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc spawn <boss> - Spawn a boss");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc list - List all available bosses");
            if (sender.hasPermission("hollowknightmc.admin")) {
                sender.sendMessage(ChatColor.YELLOW + "/hkmc reload - Reload configuration");
            }
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "select":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can select classes!");
                    return true;
                }
                
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hkmc select <knight|hornet>");
                    return true;
                }
                
                Player player = (Player) sender;
                String className = args[1];
                
                if (plugin.getClassSelectionManager().selectClass(player, className)) {
                    // Success message is handled in ClassSelectionManager
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to select class " + className);
                }
                break;

            case "abilities":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can view abilities!");
                    return true;
                }
                
                Player p = (Player) sender;
                String playerClass = plugin.getClassSelectionManager().getPlayerClass(p);
                
                if (playerClass == null) {
                    p.sendMessage(ChatColor.RED + "You haven't selected a class yet! Use /hkmc select <knight|hornet>");
                    return true;
                }
                
                showAbilities(p, playerClass);
                break;

            case "reload":
                if (!sender.hasPermission("hollowknightmc.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                    return true;
                }
                
                configManager.reloadConfig();
                plugin.getKnightManager().reloadConfig();
                plugin.getHornetManager().reloadConfig();
                bossManager.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "HollowKnightMC configuration reloaded!");
                break;

            case "list":
                sender.sendMessage(ChatColor.GOLD + "=== Available Bosses ===");
                String[] bosses = bossManager.getAllBossNames();
                for (String boss : bosses) {
                    boolean enabled = configManager.getConfig().getBoolean("Bosses." + boss + ".Enabled", true);
                    String status = enabled ? ChatColor.GREEN + "✓" : ChatColor.RED + "✗";
                    sender.sendMessage(status + ChatColor.YELLOW + " " + boss);
                }
                break;

            case "spawn":
                if (!sender.hasPermission("hollowknightmc.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to spawn bosses!");
                    return true;
                }
                
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can spawn bosses!");
                    return true;
                }
                
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hkmc spawn <boss_name>");
                    return true;
                }
                
                Player spawner = (Player) sender;
                String bossName = args[1];
                
                if (bossManager.spawnBoss(bossName, spawner.getLocation())) {
                    spawner.sendMessage(ChatColor.GREEN + "Spawned " + bossName + "!");
                } else {
                    spawner.sendMessage(ChatColor.RED + "Failed to spawn " + bossName + ". Check if it's enabled in config.");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /hkmc for help.");
                break;
        }

        return true;
    }

    private void showAbilities(Player player, String className) {
        player.sendMessage(ChatColor.GOLD + "=== " + className.toUpperCase() + " ABILITIES ===");
        
        if (className.equals("knight")) {
            player.sendMessage(ChatColor.BLUE + "Soul System:");
            player.sendMessage(ChatColor.GRAY + "- Gain soul from combat");
            player.sendMessage(ChatColor.GRAY + "- Soul shown on XP bar");
            player.sendMessage("");
            player.sendMessage(ChatColor.BLUE + "Abilities:");
            player.sendMessage(ChatColor.YELLOW + "• Dash" + ChatColor.GRAY + " - Double-tap SHIFT");
            player.sendMessage(ChatColor.YELLOW + "• Wall Jump" + ChatColor.GRAY + " - SHIFT + SPACE near wall");
            player.sendMessage(ChatColor.YELLOW + "• Double Jump" + ChatColor.GRAY + " - SPACE in mid-air");
            player.sendMessage(ChatColor.YELLOW + "• Vengeful Spirit" + ChatColor.GRAY + " - LEFT CLICK (costs soul)");
            player.sendMessage(ChatColor.YELLOW + "• Focus" + ChatColor.GRAY + " - SHIFT + RIGHT CLICK (costs soul)");
        } else if (className.equals("hornet")) {
            player.sendMessage(ChatColor.WHITE + "Silk System:");
            player.sendMessage(ChatColor.GRAY + "- Gain silk from combat");
            player.sendMessage(ChatColor.GRAY + "- Silk shown on XP bar");
            player.sendMessage("");
            player.sendMessage(ChatColor.WHITE + "Abilities:");
            player.sendMessage(ChatColor.YELLOW + "• Dash" + ChatColor.GRAY + " - Double-tap SHIFT (faster than Knight)");
            player.sendMessage(ChatColor.YELLOW + "• Needle Throw" + ChatColor.GRAY + " - LEFT CLICK (costs silk)");
            player.sendMessage(ChatColor.YELLOW + "• Silk Trap" + ChatColor.GRAY + " - RIGHT CLICK (costs silk)");
            player.sendMessage(ChatColor.YELLOW + "• Enhanced Speed" + ChatColor.GRAY + " - Faster movement and jumps");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("select", "abilities", "spawn", "list");
            if (sender.hasPermission("hollowknightmc.admin")) {
                subCommands = Arrays.asList("select", "abilities", "spawn", "list", "reload");
            }
            
            for (String subCmd : subCommands) {
                if (subCmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCmd);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("select")) {
                completions.addAll(Arrays.asList("knight", "hornet"));
            } else if (args[0].equalsIgnoreCase("spawn") && sender.hasPermission("hollowknightmc.admin")) {
                completions.addAll(Arrays.asList(bossManager.getAllBossNames()));
            }
        }
        
        return completions;
    }
}