/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.utils.Config;

import java.util.ArrayList;

public class PlotRegistrar {
    public static ArrayList<Plot> plots = new ArrayList<>();
    public static Plot spawnPlot = null;

    public static void getPlotsFromConfig() {
        for (ConfigurationSection configPlot : Config.getSections("Plots")) {
            Plot plot = new Plot(configPlot.getInt("from_x"), configPlot.getInt("from_z"),
                    configPlot.getInt("to_x"), configPlot.getInt("to_z"), configPlot.getString("displayName"));
            plots.add(plot);
            Bukkit.getLogger().info(Main.getPrefix() + "Loaded plot " + configPlot.getName());
            if (plot.getName().equalsIgnoreCase("spawn")) {
                spawnPlot = plot;
            }
        }
        Bukkit.getLogger().info(Main.getPrefix() + "Successfully loaded " + plots.size() + " plots into memory.");
    }
}
