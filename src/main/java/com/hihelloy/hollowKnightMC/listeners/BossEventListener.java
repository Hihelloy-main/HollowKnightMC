package com.hihelloy.hollowKnightMC.listeners;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.managers.BossManager;
import com.hihelloy.hollowKnightMC.players.KnightPlayer;
import com.hihelloy.hollowKnightMC.players.HornetPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossEventListener implements Listener {
    private final BossManager bossManager;

    public BossEventListener(BossManager bossManager) {
        this.bossManager = bossManager;
    }

    @EventHandler
    public void onBossDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!entity.hasMetadata("hollow_knight_boss")) return;
        
        // Boss-specific damage handling
        String bossName = entity.getMetadata("hollow_knight_boss").get(0).asString();
        
        // Increase damage for boss fights
        event.setDamage(event.getDamage() * 1.2);
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.hasMetadata("hollow_knight_boss")) return;
        
        String bossName = entity.getMetadata("hollow_knight_boss").get(0).asString();
        
        // Give extra rewards for boss kills
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            
            // Add extra XP and drops
            event.setDroppedExp(event.getDroppedExp() * 5);
            
            // Give soul to knight players
            KnightPlayer knightPlayer = HollowKnightMC.plugin.getKnightManager().getKnightPlayer(killer);
            if (knightPlayer != null) {
                knightPlayer.addSoul(33); // Large soul reward for boss kills
            }
            
            // Give silk to hornet players
            HornetPlayer hornetPlayer = HollowKnightMC.plugin.getHornetManager().getHornetPlayer(killer);
            if (hornetPlayer != null) {
                hornetPlayer.addSilk(20); // Large silk reward for boss kills
            }
        }
    }
}