/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots.playerplots;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.plots.Plot;
import tech.justcoding.homburgplots.plots.PlotRegistrar;
import tech.justcoding.homburgplots.utils.Config;

public class ClaimPlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Main.getNoPlayer());
            return false;
        }
        if (Config.getInt("playerplots.chunksused." + player.getUniqueId().toString(), 0) >=
                Config.getInt("playerplots.maxchunks", 6)) {
            player.sendMessage(Main.getErrorPrefix() + "Du kannst maximal " +
                    Config.getInt("playerplots.maxchunks", 6) + " Chunks besitzen. ");
            return false;
        }
        if (player.getWorld() != Bukkit.getWorld("world")) {
            player.sendMessage(Main.getErrorPrefix() + "Du kannst in dieser Dimension" +
                    " keine Chunks claimen! ");
            return false;
        }
        Chunk chunk = player.getLocation().getChunk();
        int chunkx = chunk.getX();
        int chunkz = chunk.getZ();
        int blockxmin = chunkx * 16 - 1; //-16
        int blockxmax = blockxmin + 17; // -1
        int blockzmin = chunkz * 16 - 1;
        int blockzmax = blockzmin + 17;

        Location location1 = new Location(Bukkit.getWorld("world"), blockxmin, 0, blockzmin);
        Location location2 = new Location(Bukkit.getWorld("world"), blockxmax, 0, blockzmax);

        Location spawnLocation = Bukkit.getWorld("world").getSpawnLocation().clone();
        spawnLocation.setY(0);
        if (location1.distance(spawnLocation) < 100 || location2.distance(spawnLocation) < 100) {
            player.sendMessage(Main.getErrorPrefix() + "Aus Sicherheitsgründen müssen Spielerplots mindestens " +
                    "100 Blöcke vom Spawn entfernt sein. ");
            return false;
        }

        Plot playerHomePlot = null;
        for (Plot iPlot : PlotRegistrar.plots) {
            if (iPlot.isClassicPlot() && iPlot.canBuildOnPlot(player)) {
                playerHomePlot = iPlot;
                break;
            }
        }

        for (Plot iPlot : PlotRegistrar.plots) {
            if (!iPlot.isClassicPlot()) continue;
            if (iPlot == playerHomePlot) continue;
            Location plotLocation1 = iPlot.getFrom();
            Location plotLocation2 = iPlot.getTo();
            if (plotLocation1.distance(location1) < 50 || plotLocation1.distance(location2) < 50 ||
                    plotLocation2.distance(location1) < 50 || plotLocation2.distance(location2) < 50) {
                if (playerHomePlot != null) {
                    Location homeLocation1 = playerHomePlot.getFrom();
                    Location homeLocation2 = playerHomePlot.getTo();
                    if (homeLocation1.distance(location1) < 50 || homeLocation1.distance(location2) < 50 ||
                            homeLocation2.distance(location1) < 50 || homeLocation2.distance(location2) < 50) {
                        continue;
                    }
                }
                player.sendMessage(Main.getErrorPrefix() + "Du kannst keine Chunks, die näher als 50 Blöcke " +
                        "an einem Plot eines anderen Stammes sind, claimen. ");
                return false;
            }
        }

        for (Plot plot : PlotRegistrar.plots) {
            Location locationcheck1 = location1.clone();
            locationcheck1.setX(location1.getX() - 1);
            Location locationcheck2 = location1.clone();
            locationcheck2.setX(location2.getX() - 1);
            if (!(plot instanceof PlayerPlot playerPlot)) {
                if (plot.isLocationOnPlot(locationcheck1) || plot.isLocationOnPlot(locationcheck2)) {
                    player.sendMessage(Main.getErrorPrefix() + "Ein Teil deines Plots wurde bereits durch " +
                            "jemand anderes geclaimt. ");
                    return false;
                }
            } else {
                if (locationcheck1.getChunk().equals(playerPlot.getChunk())) {
                    player.sendMessage(Main.getErrorPrefix() + "Ein Teil deines Plots wurde bereits durch " +
                            "jemand anderes geclaimt. ");
                    return false;
                }
            }
        }
        PlayerPlot plot = new PlayerPlot(blockxmin, blockzmin, blockxmax, blockzmax, player.getUniqueId());
        PlotRegistrar.plots.add(plot);
        int newCount = Config.getInt("playerplots.chunksused." + player.getUniqueId().toString()) + 1;
        Config.set("playerplots.chunksused." + player.getUniqueId().toString(), newCount);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Du kannst noch " +
                (Config.getInt("playerplots.maxchunks", 6) - newCount) + " Chunks claimen. "));
        player.sendMessage(Main.getPrefix() + "Das Plot wurde erfolgreich registriert. ");
        return true;
    }
}
