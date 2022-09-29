/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburglobby.selector;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import tech.justcoding.homburglobby.Main;
import tech.justcoding.homburglobby.utils.Config;

public class ServerLoader {
    public static void loadServers() {
        for (ConfigurationSection serverConfig : Config.getSections("Servers")) {
            Server server = new Server(serverConfig.getString("name"), serverConfig.getString("bungeeName"),
                    serverConfig.getString("requiredPermission"), serverConfig.getString("material"));
            ServerSelector.servers.add(server);
            Bukkit.getLogger().info(Main.getPrefix() + "Loaded server " + serverConfig.getName());
        }
        Bukkit.getLogger().info(Main.getPrefix() + "Successfully loaded " + ServerSelector.servers.size() + " servers into memory.");
    }
}
