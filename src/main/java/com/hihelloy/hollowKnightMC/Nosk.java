package com.hihelloy.hollowKnightMC;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

import static com.hihelloy.hollowKnightMC.HollowKnightMC.plugin;
import static com.hihelloy.hollowKnightMC.NoskCommand.configManager;

public class Nosk extends Spider {
    private BukkitTask shapeshiftTask;

    public Nosk(Location loc, ConfigManager configManager) {
        super(EntityType.SPIDER, ((CraftWorld) loc.getWorld()).getHandle());
        
        // Set custom attributes from config
        double speed = configManager.getConfig().getDouble("Nosk.Speed", 0.3);
        double health = configManager.getConfig().getDouble("Nosk.Health", 52.0);
        
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
        this.setHealth((float) health);
        
        // Set custom name
        this.setCustomName(net.minecraft.network.chat.Component.literal("§4§lNosk"));
        this.setCustomNameVisible(true);
        
        // Make it persistent
        this.setPersistenceRequired(true);
    }

    @Override
    protected void registerGoals() {
        // Clear existing goals
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);
        
        // Add custom goals in priority order
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.6F));
        this.goalSelector.addGoal(3, new NoskDashGoal(this));
        this.goalSelector.addGoal(4, new NoskBlobSpitGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        // Target selection goals
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.player.Player.class, true));
    }

    @Override
    public net.minecraft.world.entity.Pose getPose() {
        return net.minecraft.world.entity.Pose.STANDING;
    }

    @Override
    public net.minecraft.world.entity.EntityDimensions getDimensions(net.minecraft.world.entity.Pose pose) {
        // Set custom dimensions - wider and taller than normal spider
        return net.minecraft.world.entity.EntityDimensions.scalable(1.4F, 7.0F);
    }

    public void startShapeshiftTask(Entity bukkitEntity) {
        long cooldown = configManager.getConfig().getLong("Nosk.ShapeShiftCooldown", 200L);
        
        shapeshiftTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            try {
                DisguiseType[] types = {
                        DisguiseType.ZOMBIE, DisguiseType.CREEPER, DisguiseType.ENDERMAN,
                        DisguiseType.SPIDER, DisguiseType.SKELETON, DisguiseType.WITCH,
                        DisguiseType.VINDICATOR, DisguiseType.PILLAGER
                };
                
                DisguiseType randomType = types[new Random().nextInt(types.length)];
                Disguise disguise = new MobDisguise(randomType);
                
                // Apply the disguise
                DisguiseAPI.disguiseToAll(bukkitEntity, disguise);
                
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to apply disguise to Nosk: " + e.getMessage());
            }
        }, 40L, cooldown); // Wait 2 seconds before first shapeshift, then use config cooldown
    }

    @Override
    public void remove(RemovalReason reason) {
        // Cancel shapeshift task when entity is removed
        if (shapeshiftTask != null && !shapeshiftTask.isCancelled()) {
            shapeshiftTask.cancel();
        }
        super.remove(reason);
    }

    // Custom dash goal
    static class NoskDashGoal extends Goal {
        private final Nosk nosk;
        private int dashCooldown = 0;
        private final int maxCooldown;
        private final double dashSpeed;
        private final double dashHeight;

        public NoskDashGoal(Nosk nosk) {
            this.nosk = nosk;
            this.maxCooldown = configManager.getConfig().getInt("Nosk.DashCooldown", 60);
            this.dashSpeed = configManager.getConfig().getDouble("Nosk.DashSpeed", 1.5);
            this.dashHeight = configManager.getConfig().getDouble("Nosk.DashHeight", 0.2);
        }

        @Override
        public boolean canUse() {
            if (dashCooldown > 0) {
                dashCooldown--;
                return false;
            }
            return nosk.getTarget() != null && nosk.distanceTo(nosk.getTarget()) > 3.0 && 
                   nosk.distanceTo(nosk.getTarget()) < 12.0 && nosk.onGround();
        }

        @Override
        public void start() {
            if (nosk.getTarget() != null) {
                double dx = nosk.getTarget().getX() - nosk.getX();
                double dz = nosk.getTarget().getZ() - nosk.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);
                
                if (dist > 0) {
                    nosk.setDeltaMovement(dx / dist * dashSpeed, dashHeight, dz / dist * dashSpeed);
                    dashCooldown = maxCooldown;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false; // One-shot ability
        }
    }

    // Custom blob spit goal
    static class NoskBlobSpitGoal extends Goal {
        private final Nosk nosk;
        private int spitCooldown = 0;
        private final int maxCooldown;
        private final float blobSpeed;

        public NoskBlobSpitGoal(Nosk nosk) {
            this.nosk = nosk;
            this.maxCooldown = configManager.getConfig().getInt("Nosk.BlobSpitCooldown", 40);
            this.blobSpeed = (float) configManager.getConfig().getDouble("Nosk.BlobSpeed", 1.2);
        }

        @Override
        public boolean canUse() {
            if (spitCooldown > 0) {
                spitCooldown--;
                return false;
            }
            return nosk.getTarget() != null && nosk.hasLineOfSight(nosk.getTarget()) &&
                   nosk.distanceTo(nosk.getTarget()) > 4.0 && nosk.distanceTo(nosk.getTarget()) < 20.0;
        }

        @Override
        public void start() {
            if (nosk.getTarget() != null) {
                try {
                    net.minecraft.world.entity.projectile.Snowball blob = 
                        new net.minecraft.world.entity.projectile.Snowball(nosk.level(), nosk);
                    
                    double dx = nosk.getTarget().getX() - nosk.getX();
                    double dy = nosk.getTarget().getEyeY() - blob.getY();
                    double dz = nosk.getTarget().getZ() - nosk.getZ();
                    
                    blob.shoot(dx, dy, dz, blobSpeed, 1.0F);
                    nosk.level().addFreshEntity(blob);
                    spitCooldown = maxCooldown;
                    
                } catch (Exception e) {
                    // Fallback if snowball creation fails
                    spitCooldown = maxCooldown / 2;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false; // One-shot ability
        }
    }
}