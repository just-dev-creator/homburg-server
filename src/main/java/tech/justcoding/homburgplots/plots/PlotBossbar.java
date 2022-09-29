/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlotBossbar implements Listener {

    private static HashMap<Player, BossBar> playerBars = new HashMap<>();
    private static void updateBossbar(Player player) {
        Location location = player.getLocation();
        Plot plot = null;
        for (Plot iPlot : PlotRegistrar.plots) {
            if (iPlot.isLocationOnPlot(location)) {
                plot = iPlot;
                break;
            }
        }
        BossBar bar = playerBars.get(player);
        if (plot == null) {
            bar.setTitle(ChatColor.DARK_GRAY + "Wildnis");
            bar.setColor(BarColor.GREEN);
            bar.setStyle(BarStyle.SOLID);
        } else {
            bar.setTitle(ChatColor.GRAY + "Plot: " + ChatColor.BLUE + plot.getName());
            if (plot.canBuildOnPlot(player)) {
                bar.setColor(BarColor.YELLOW);
            } else {
                bar.setColor(BarColor.BLUE);
            }
            bar.setStyle(BarStyle.SEGMENTED_12);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerBars.put(event.getPlayer(), Bukkit.createBossBar(ChatColor.GRAY + "Daten werden geladen...", BarColor.WHITE, BarStyle.SEGMENTED_20));
        updateBossbar(event.getPlayer());
        playerBars.get(event.getPlayer()).addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        playerBars.get(event.getPlayer()).removePlayer(event.getPlayer());
        playerBars.remove(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getFrom().getBlockZ()) return;
        updateBossbar(event.getPlayer());
    }
}
