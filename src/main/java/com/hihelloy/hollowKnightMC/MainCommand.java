package com.hihelloy.hollowKnightMC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
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
        if (!sender.hasPermission("hollowknightmc.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "=== HollowKnightMC Commands ===");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc reload - Reload configuration");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc list - List all available bosses");
            sender.sendMessage(ChatColor.YELLOW + "/hkmc spawn <boss> - Spawn a boss");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reload":
                configManager.reloadConfig();
                plugin.getKnightManager().reloadConfig();
                bossManager.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "HollowKnightMC configuration reloaded!");
                break;

            case "list":
                sender.sendMessage(ChatColor.GOLD + "=== Available Bosses ===");
                String[] bosses = {"Hornet", "ShadowKnight", "MantisLord", "SoulMaster", "DungDefender", 
                                 "BrokenVessel", "CrystalGuardian", "WatcherKnight", "TraitorLord", "Grimm"};
                for (String boss : bosses) {
                    boolean enabled = configManager.getConfig().getBoolean("Bosses." + boss + ".Enabled", true);
                    String status = enabled ? ChatColor.GREEN + "✓" : ChatColor.RED + "✗";
                    sender.sendMessage(status + ChatColor.YELLOW + " " + boss);
                }
                break;

            case "spawn":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can spawn bosses!");
                    return true;
                }
                
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hkmc spawn <boss_name>");
                    return true;
                }
                
                Player player = (Player) sender;
                String bossName = args[1];
                
                if (bossManager.spawnBoss(bossName, player.getLocation())) {
                    player.sendMessage(ChatColor.GREEN + "Spawned " + bossName + "!");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to spawn " + bossName + ". Check if it's enabled in config.");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /hkmc for help.");
                break;
        }

        return true;
    }
}