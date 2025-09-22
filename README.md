[![GitHub release](https://img.shields.io/github/v/release/Hihelloy-main/HollowKnightMC?style=flat-square)](https://github.com/Hihelloy-main/HollowKnightMC/releases)
[![Github Downloads](https://img.shields.io/github/downloads/Hihelloy-main/HollowKnightMC/total.svg)](https://github.com/Hihelloy-main/HollowKnightMC/releases)

# HollowKnightMC Plugin

A comprehensive Minecraft Bukkit/Spigot plugin that adds complete Hollow Knight and Silksong content to your server.

## Features

### Knight & Hornet Player Abilities

#### Knight Abilities
- **Dash**: Quick movement ability with invulnerability frames
- **Wall Jump**: Jump off walls for enhanced mobility  
- **Double Jump**: Additional jump in mid-air
- **Soul System**: Gain soul from combat, use for abilities
- **Focus**: Spend soul to heal yourself
- **Vengeful Spirit**: Ranged soul projectile attack (Left Click)
- **Custom Stats**: Configurable health, speed, damage, and size
- **Visual Effects**: Particles and sounds for all abilities
- **Scoreboard**: Real-time display of stats and abilities

#### Hornet Abilities
- **Dash**: Fast agile dash with shorter cooldown
- **Needle Throw**: Ranged silk-based projectile (Left Click)
- **Silk Trap**: Place web traps (Right Click)
- **Silk System**: Gain silk from combat, regenerates over time
- **Enhanced Mobility**: Higher speed and jump than Knight
- **Custom Stats**: Configurable health, speed, damage, and size
- **Visual Effects**: Silk particles and unique sounds

### Complete Boss Collection
The plugin includes **25 bosses** from both Hollow Knight and Silksong:

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

- `/nosk` - Spawns a Nosk entity at your location (requires `hollowknightmc.nosk` permission)
- `/knight` - Toggle Knight abilities on/off
- `/knight [toggle|soul|reload]` - Knight management commands
- `/hornet` - Toggle Hornet abilities on/off  
- `/hornet [toggle|silk|reload]` - Hornet management commands
- `/hkmc spawn <boss>` - Spawn any of the 25 bosses
- `/hkmc list` - List all available bosses with status
- `/hkmc reload` - Reload all configurations

### Tab Completion
All commands now support full tab completion for easy usage.

## Configuration

The expanded `config.yml` file allows you to customize:

### Knight & Hornet Abilities
- Health and movement stats
- All ability toggles and parameters
- Scoreboard settings
- Ability cooldowns and costs
- Soul/Silk system parameters
- Combat damage values
- Visual effect toggles

### All 25 Bosses
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

- `hollowknightmc.nosk` - Allows spawning Nosk entities (default: op)
- `hollowknightmc.knight` - Allows using Knight abilities (default: true)
- `hollowknightmc.hornet` - Allows using Hornet abilities (default: true)
- `hollowknightmc.admin` - Allows admin commands for the plugin (default: op)

## Dependencies

- Bukkit/Spigot API
- LibsDisguises (required for shapeshifting feature)

## Version 2.0.0 Features

- ✅ Fixed all command tab completion
- ✅ Working scoreboard system
- ✅ Fixed PlayerJoinEvent handling
- ✅ Restored NoskSpawner class
- ✅ Corrected plugin.yml structure
- ✅ Added complete API system
- ✅ JitPack integration ready
- ✅ All 25 bosses from both games
- ✅ Both Knight and Hornet player types
- ✅ Vengeful Spirit now uses left-click
- ✅ Enhanced configuration system
- ✅ Production-ready codebase
