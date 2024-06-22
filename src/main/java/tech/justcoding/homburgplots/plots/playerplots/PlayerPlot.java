/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots.playerplots;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tech.justcoding.homburgplots.plots.Plot;
import tech.justcoding.homburgplots.plots.PlotRegistrar;
import tech.justcoding.homburgplots.utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerPlot extends Plot {
    UUID plotOwner;
    String configurationName;
    ArrayList<UUID> builders = new ArrayList<>();
    public PlayerPlot(int from_x, int from_z, int to_x, int to_z, UUID owner) {
        super(from_x, from_z, to_x, to_z, Bukkit.getPlayer(owner).getName());
        this.plotOwner = owner;
        this.saveToConfig();
    }

    public PlayerPlot(int from_x, int from_z, int to_x, int to_z, UUID owner, String configurationName) {
        super(from_x, from_z, to_x, to_z, Bukkit.getOfflinePlayer(owner).getName());
        this.plotOwner = owner;
        this.configurationName = configurationName;
        this.loadBuilders();
    }

    private void loadBuilders() {
        List<String> uuidStringList = Config.getStringList(configurationName + ".builders", new ArrayList<>());
        for (String uuidString : uuidStringList) {
            this.builders.add(UUID.fromString(uuidString));
        }
    }

    private void saveBuilders() {
        ArrayList<String> uuidStringList = new ArrayList<>();
        for (UUID uuid : this.builders) {
            uuidStringList.add(uuid.toString());
        }
        Config.set(configurationName + ".builders", uuidStringList);
    }

    public void saveToConfig() {
        String configurationKey = "Plots." + plotOwner.toString() + "+" + UUID.randomUUID().toString();
        ConfigurationSection configurationSection = Config.getSection(configurationKey);
        configurationSection.set("owner", plotOwner.toString());
        configurationSection.set("from_x", from_x);
        configurationSection.set("from_z", from_z);
        configurationSection.set("to_x", to_x);
        configurationSection.set("to_z", to_z);
        configurationSection.set("builders", new ArrayList<>());
        Config.set(configurationKey, configurationSection);
        configurationName = configurationKey;
    }

    public void delete() {
        Config.set(configurationName, null);
        PlotRegistrar.plots.remove(this);
    }

    public void addBuilder(UUID uuid) {
        builders.add(uuid);
        saveBuilders();
    }

    public void removeBuilder(UUID uuid) {
        builders.remove(uuid);
        saveBuilders();
    }

    @Override
    public boolean canBuildOnPlot(Player player) {
        return player.getUniqueId().equals(plotOwner) || builders.contains(player.getUniqueId())
                || player.hasPermission("justcoding.homburg.plot.all");
    }

    @Override
    public String getName() {
        OfflinePlayer player = Bukkit.getOfflinePlayer(plotOwner);
        return player.getName();
    }

    @Override
    public String getPermissionName() {
        return this.configurationName;
    }

    public UUID getPlotOwner() {
        return plotOwner;
    }

    @Override
    public boolean isClassicPlot() {
        return false;
    }

    public Chunk getChunk() {
        return new Location(Bukkit.getWorld("world") ,from_x, 0, from_z).getChunk();
    }
}
