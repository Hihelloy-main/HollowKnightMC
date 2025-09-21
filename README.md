# HollowKnightMC Plugin

A Minecraft Bukkit/Spigot plugin that adds Hollow Knight inspired mobs to your server.

## Features

### Knight Player Abilities
- **Dash**: Quick movement ability with invulnerability frames
- **Wall Jump**: Jump off walls for enhanced mobility  
- **Double Jump**: Additional jump in mid-air
- **Crystal Dash**: Charged dash with longer distance
- **Super Dash**: Extended dash ability
- **Soul System**: Gain soul from combat, use for abilities
- **Focus**: Spend soul to heal yourself
- **Vengeful Spirit**: Ranged soul projectile attack
- **Shade Soul**: Upgraded piercing projectile
- **Howling Wraiths**: Area-of-effect soul attack
- **Custom Stats**: Configurable health, speed, damage, and size
- **Visual Effects**: Particles and sounds for all abilities
- **Scoreboard**: Real-time display of stats and abilities

### Boss Entities
The plugin includes 10 iconic Hollow Knight bosses:
1. **Hornet** - Fast, agile fighter with needle throws
2. **Shadow Knight** - Dark mirror with void abilities
3. **Mantis Lord** - Precise warrior with boomerang claws
4. **Soul Master** - Floating mage with soul attacks
5. **Dung Defender** - Bouncing tank with dung balls
6. **Broken Vessel** - Infected vessel with balloon summons
7. **Crystal Guardian** - Laser-shooting crystal entity
8. **Watcher Knight** - Rolling armored guardian
9. **Traitor Lord** - Powerful mantis with shockwave attacks
10. **Grimm** - Master of nightmares with fire abilities

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

## Installation

1. Download LibsDisguises plugin and install it on your server
2. Compile this plugin using Maven: `mvn clean package`
3. Place the generated JAR file in your server's `plugins` folder
4. Restart your server
5. Configure the plugin using `plugins/HollowKnightMC/config.yml`

## Commands

- `/nosk` - Spawns a Nosk entity at your location (requires `hollowknightmc.nosk` permission)
- `/knight` - Toggle Knight abilities on/off
- `/knight soul` - Check your current soul amount
- `/knight reload` - Reload configuration (admin only)
- `/hkmc spawn <boss>` - Spawn a specific boss
- `/hkmc list` - List all available bosses
- `/hkmc reload` - Reload all configurations

## Configuration

The `config.yml` file allows you to customize:

### Knight Abilities
- Health and movement stats
- All ability toggles and parameters
- Scoreboard settings
- Ability cooldowns and costs
- Soul system parameters
- Combat damage values
- Visual effect toggles

### Nosk Entity
- Health and speed values
- Ability cooldowns
- Attack ranges and damage
- Shapeshift frequency

### Boss Configuration
- Individual boss enable/disable
- Health, speed, and damage values
- Ability-specific parameters
- Spawn chances and behavior

## Building

This project uses Maven. To build:

```bash
mvn clean package
```

The compiled JAR will be in the `target/` directory.

## Permissions

- `hollowknightmc.nosk` - Allows spawning Nosk entities (default: op)
- `hollowknightmc.knight` - Allows using Knight abilities (default: true)
- `hollowknightmc.knight.admin` - Allows admin commands (default: op)
- `hollowknightmc.admin` - Allows admin commands for the plugin (default: op)

## Dependencies

- Bukkit/Spigot API
- LibsDisguises (required for shapeshifting feature)