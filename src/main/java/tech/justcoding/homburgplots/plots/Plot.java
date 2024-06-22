/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Plot {
    protected int from_x;
    protected int from_z;
    protected int to_x;
    protected int to_z;

    protected String name;
    private String permissionName;

    public Plot(int from_x, int from_z, int to_x, int to_z, String name) {
        this.from_x = from_x;
        this.from_z = from_z;
        this.to_x = to_x;
        this.to_z = to_z;
        this.name = name;
        this.permissionName = "justcoding.homburg.plot." + name.toLowerCase().replace(" ", "_");
    }

    public boolean canBuildOnPlot(Player player) {
        return player.hasPermission(permissionName) || player.hasPermission("justcoding.homburg.plot.all");
    }

    public boolean isLocationOnPlot(Location location) {
        return location.getBlockX() > this.from_x && location.getBlockZ() > this.from_z &&
                location.getBlockX() < this.to_x && location.getBlockZ() < this.to_z && location.getWorld().getName()
                .equalsIgnoreCase("world");
    }

    public String getName() {
        return name;
    }


    public String getPermissionName() {
        return permissionName;
    }

    public Location getFrom() {
        return new Location(Bukkit.getWorld("world"), from_x, 0, from_x);
    }

    public Location getTo() {
        return new Location(Bukkit.getWorld("world"), to_x, 0, to_z);
    }

    public boolean isClassicPlot() {
        return true;
    }
}
