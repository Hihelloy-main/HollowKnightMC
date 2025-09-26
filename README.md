# HollowKnightMC Plugin v2.0.0

A comprehensive Minecraft Bukkit/Spigot plugin that adds complete Hollow Knight and Silksong content to your server with advanced class system, addon support, and enhanced visual effects.

## Features

### Command-Based Class System
Players use commands to get Knight or Hornet abilities, with no auto-enabling.

### Enhanced Knight & Hornet Classes

#### Knight Abilities
- **Dash**: Quick movement ability with invulnerability frames
- **Wall Jump**: Jump off walls for enhanced mobility  
- **Double Jump**: Additional jump in mid-air
- **Crystal Dash**: Charged long-distance dash with damage
- **Soul System**: Gain soul from combat, use for abilities
- **Focus**: Spend soul to heal yourself
- **Vengeful Spirit**: Ranged soul projectile attack (Left Click)
- **Shade Soul**: Upgraded piercing projectile
- **Howling Wraiths**: Area-of-effect soul attack
- **Custom Stats**: Configurable health, speed, damage, and size
- **Advanced Scoreboard**: Real-time display with animations and current actions
- **Visual Effects**: Particles and sounds for all abilities

#### Hornet Abilities
- **Dash**: Fast agile dash with shorter cooldown
- **Needle Throw**: Ranged silk-based projectile (Left Click)
- **Silk Trap**: Place web traps (Right Click)
- **Silk Lash**: Melee combo attack with knockback
- **Spike Trap**: Place damaging spike traps
- **Silk System**: Gain silk from combat, regenerates over time
- **Enhanced Mobility**: Higher speed and jump than Knight
- **Custom Stats**: Configurable health, speed, damage, and size
- **Advanced Scoreboard**: Real-time display with animations
- **Visual Effects**: Silk particles and unique sounds

### Addon System
- **Custom Ability API**: Create addon abilities that integrate seamlessly
- **JAR Loading**: Place addon JARs in `plugins/HollowKnightMC/abilities/`
- **Scoreboard Integration**: Addon abilities appear on the scoreboard
- **Class Support**: Addons can target Knight, Hornet, or both classes

### Enhanced Visual Effects
- **Advanced Particles**: Multiple particle types with configurable counts
- **Falling Block Effects**: Dynamic falling blocks for abilities
- **Sound System**: Configurable sounds for all abilities
- **Animated Scoreboard**: Color-changing title and real-time updates

### Complete Boss Collection
The plugin includes **25 bosses** from both Hollow Knight and Silksong with NMS implementation:

#### Hollow Knight Bosses (15)
1. **Hornet** - Agile fighter with needle attacks
2. **Shadow Knight** - Dark mirror entity
3. **Mantis Lord** - Precise warrior
4. **Soul Master** - Floating soul mage
5. **Dung Defender** - Bouncing tank
6. **Broken Vessel** - Infected vessel
7. **Crystal Guardian** - Crystal laser entity
8. **Watcher Knight** - Rolling guardian
9. **Traitor Lord** - Powerful mantis
10. **Grimm** - Master of nightmares
11. **The Radiance** - Final boss entity
12. **Nightmare King Grimm** - Ultimate Grimm form
13. **Absolute Radiance** - Godmaster final boss
14. **Pure Vessel** - Perfect Knight form
15. **Sisters of Battle** - Enhanced Mantis Lords

#### Silksong Bosses (10)
16. **Lace** - Swift silk warrior
17. **Moss Mother** - Nature guardian
18. **Sharpe** - Bone collector
19. **Gannet** - Flying predator
20. **Carmelita** - Spider queen
21. **Macabre** - Death dancer
22. **Krow** - Crow king
23. **Bone Bottom** - Skeletal giant
24. **Seth** - Fire serpent
25. **Coral** - Ocean guardian

### Split Configuration System
- **Main config.yml**: Global plugin settings
- **Knight.yml**: All Knight-specific configurations
- **Hornet.yml**: All Hornet-specific configurations  
- **Bosses.yml**: All boss configurations and abilities

### Nosk Entity
- **Nosk Entity**: A custom spider-based mob with special abilities
  - Shapeshifting disguises using LibsDisguises
  - Dash attack ability
  - Blob spit projectile attack
  - Custom health and speed values
  - Advanced AI behavior

## Requirements

- Bukkit/Spigot/Paper server (1.20.4)
- LibsDisguises plugin (required dependency)
- Java 17 or higher

## JitPack Integration

This plugin is available through JitPack for easy integration:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.hihelloy</groupId>
    <artifactId>hollow-knight-mc</artifactId>
    <version>2.0.0</version>
</dependency>
```

## API Usage

```java
// Get the API
HollowKnightAPI api = HollowKnightMC.plugin.getAPI();

// Enable abilities for players
api.enableKnightAbilities(player);
api.enableHornetAbilities(player);

// Get player instances
KnightPlayer knight = api.getKnightPlayer(player);
HornetPlayer hornet = api.getHornetPlayer(player);

// Spawn bosses
api.spawnBoss("grimm", player);
```

## Installation

1. Download LibsDisguises plugin and install it on your server
2. Compile this plugin using Maven: `mvn clean package`
3. Place the generated JAR file in your server's `plugins` folder
4. Restart your server
5. Configure the plugin using `plugins/HollowKnightMC/config.yml`

## Commands

- `/knight give` - Get Knight abilities
- `/knight remove` - Remove Knight abilities
- `/knight abilities` - View Knight abilities
- `/hornet give` - Get Hornet abilities
- `/hornet remove` - Remove Hornet abilities
- `/hornet abilities` - View Hornet abilities
- `/hkmc reload` - Reload all configurations (admin only)
- `/hkmc spawn <boss>` - Spawn any of the 25 bosses (admin only)
- `/hkmc list` - List all available bosses with status
- `/nosk` - Spawns a Nosk entity at your location (requires `hollowknightmc.nosk` permission)

### Tab Completion
All commands now support full tab completion for easy usage.

## Configuration

The split configuration system allows you to customize:

### Main Configuration (config.yml)
- Global plugin settings
- Scoreboard configuration
- Addon system settings
- Visual effects multipliers

### Knight Configuration (Knight.yml)
- Health and movement stats
- All Knight ability toggles and parameters
- Scoreboard settings
- Ability cooldowns and costs
- Soul system parameters
- Combat damage values
- Visual effect toggles

### Hornet Configuration (Hornet.yml)
- Health and movement stats
- All Hornet ability toggles and parameters
- Silk system parameters
- Combat damage values
- Visual effect toggles

### Boss Configuration (Bosses.yml)
- Individual boss enable/disable for each boss
- Health, speed, and damage values
- Spawn chances and behavior
- Fully configurable stats per boss

### Nosk Entity
- Health and speed values
- Ability cooldowns and ranges
- Shapeshift frequency and types

## Building

This project uses Maven. To build:

```bash
mvn clean package
```

The compiled JAR will be in the `target/` directory.

## Permissions

- `hollowknightmc.*` - All permissions (default: op)
- `hollowknightmc.use` - Allows using basic plugin features (default: true)
- `hollowknightmc.knight` - Allows using Knight abilities (default: true)
- `hollowknightmc.hornet` - Allows using Hornet abilities (default: true)
- `hollowknightmc.nosk` - Allows spawning Nosk entities (default: op)
- `hollowknightmc.admin` - Allows admin commands for the plugin (default: op)

## Dependencies

- Bukkit/Spigot API
- LibsDisguises (required for shapeshifting feature)

## Creating Addon Abilities

To create custom abilities for the plugin:

1. Create a new Java project with HollowKnightMC as a dependency
2. Extend the `AddonAbility` class:

```java
public class MyCustomAbility extends AddonAbility {
    public MyCustomAbility() {
        super("MyAbility", "Custom ability description", "knight", 60, 10, "soul");
    }
    
    @Override
    public boolean execute(Player player) {
        // Your ability logic here
        return true;
    }
    
    @Override
    public boolean canUse(Player player) {
        // Check if ability can be used
        return true;
    }
    
    // Implement other required methods...
}
```

3. Compile to JAR and place in `plugins/HollowKnightMC/abilities/`
4. Restart server to load the addon

## Version 2.0.0 Features

- ✅ **Command-Based Abilities**: Players use `/knight give` or `/hornet give`
- ✅ **Addon System**: Full API for creating custom abilities
- ✅ **Enhanced Visual Effects**: Advanced particles, falling blocks, animations
- ✅ **Animated Scoreboard**: Real-time action display with color animations
- ✅ **Split Configuration**: Separate config files for better organization
- ✅ **NMS Boss Implementation**: All 25 bosses use proper NMS code
- ✅ **Enhanced Abilities**: More Knight and Hornet abilities with visual effects
- ✅ **Server Reload Protection**: Robust handling of server restarts
- ✅ **Complete Tab Completion**: All commands have proper tab completion
- ✅ **Working Scoreboard System**: Real-time stats and action display
- ✅ **Addon JAR Loading**: Automatic loading from abilities folder
- ✅ **Complete API System**: Full external plugin integration
- ✅ **JitPack Ready**: Easy dependency management
- ✅ **Production Ready**: Stable, tested, and optimized codebase