# HollowKnightMC Plugin

A Minecraft Bukkit/Spigot plugin that adds Hollow Knight inspired mobs to your server.

## Features

### Knight Player Abilities
- **Dash**: Quick movement ability with invulnerability frames
- **Wall Jump**: Jump off walls for enhanced mobility  
- **Soul System**: Gain soul from combat, use for abilities
- **Focus**: Spend soul to heal yourself
- **Vengeful Spirit**: Ranged soul projectile attack
- **Custom Stats**: Configurable health, speed, damage, and size
- **Visual Effects**: Particles and sounds for all abilities

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

## Configuration

The `config.yml` file allows you to customize:

### Knight Abilities
- Health and movement stats
- Ability cooldowns and costs
- Soul system parameters
- Combat damage values
- Visual effect toggles

### Nosk Entity
- Health and speed values
- Ability cooldowns
- Attack ranges and damage
- Shapeshift frequency

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

## Dependencies

- Bukkit/Spigot API
- LibsDisguises (required for shapeshifting feature)