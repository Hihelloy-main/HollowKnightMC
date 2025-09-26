package com.hihelloy.hollowKnightMC.managers;

import com.hihelloy.hollowKnightMC.HollowKnightMC;
import com.hihelloy.hollowKnightMC.api.abilities.AddonAbility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonManager {
    private final HollowKnightMC plugin;
    private final Map<String, AddonAbility> registeredAbilities;
    private final Map<UUID, List<AddonAbility>> playerAbilities;

    public AddonManager(HollowKnightMC plugin) {
        this.plugin = plugin;
        this.registeredAbilities = new HashMap<>();
        this.playerAbilities = new HashMap<>();
    }

    public void loadAddons() {
        if (!plugin.getConfig().getBoolean("Addons.Enabled", true)) {
            return;
        }

        File addonFolder = new File(plugin.getDataFolder(), "abilities");
        if (!addonFolder.exists()) {
            addonFolder.mkdirs();
            plugin.getLogger().info("Created abilities folder for addon JARs");
            return;
        }

        File[] jarFiles = addonFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            plugin.getLogger().info("No addon JARs found in abilities folder");
            return;
        }

        for (File jarFile : jarFiles) {
            try {
                loadAddonFromJar(jarFile);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load addon from " + jarFile.getName() + ": " + e.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + registeredAbilities.size() + " addon abilities");
    }

    private void loadAddonFromJar(File jarFile) throws Exception {
        try (JarFile jar = new JarFile(jarFile)) {
            URL[] urls = {jarFile.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls, plugin.getClass().getClassLoader());

            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.').replace(".class", "");
                    try {
                        Class<?> clazz = classLoader.loadClass(className);
                        if (AddonAbility.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                            AddonAbility ability = (AddonAbility) clazz.getDeclaredConstructor().newInstance();
                            registerAbility(ability);
                            plugin.getLogger().info("Loaded addon ability: " + ability.getName());
                        }
                    } catch (Exception e) {
                        // Skip classes that can't be loaded as abilities
                    }
                }
            }
        }
    }

    public boolean registerAbility(AddonAbility ability) {
        if (registeredAbilities.containsKey(ability.getName())) {
            plugin.getLogger().warning("Ability " + ability.getName() + " is already registered!");
            return false;
        }

        registeredAbilities.put(ability.getName(), ability);
        return true;
    }

    public List<AddonAbility> getPlayerAbilities(Player player) {
        String playerClass = plugin.getClassSelectionManager().getPlayerClass(player);
        if (playerClass == null) {
            return new ArrayList<>();
        }

        List<AddonAbility> abilities = new ArrayList<>();
        for (AddonAbility ability : registeredAbilities.values()) {
            if (ability.getPlayerClass().equals("both") || ability.getPlayerClass().equals(playerClass)) {
                abilities.add(ability);
            }
        }
        return abilities;
    }

    public boolean triggerAbility(Player player, String abilityName) {
        AddonAbility ability = registeredAbilities.get(abilityName);
        if (ability == null) {
            return false;
        }

        String playerClass = plugin.getClassSelectionManager().getPlayerClass(player);
        if (playerClass == null) {
            return false;
        }

        if (!ability.getPlayerClass().equals("both") && !ability.getPlayerClass().equals(playerClass)) {
            return false;
        }

        if (!ability.canUse(player)) {
            return false;
        }

        return ability.execute(player);
    }

    public AddonAbility getAbility(String name) {
        return registeredAbilities.get(name);
    }

    public Collection<AddonAbility> getAllAbilities() {
        return registeredAbilities.values();
    }

    public void shutdown() {
        registeredAbilities.clear();
        playerAbilities.clear();
    }
}