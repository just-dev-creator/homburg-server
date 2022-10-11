/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.justcoding.homburgplots.Main;

public class
PlotBuildCheck implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (Plot plot : PlotRegistrar.plots) {
            if (plot.canBuildOnPlot(event.getPlayer())) {
                event.getPlayer().sendMessage(Main.getPrefix() + "Du darfst auf dem Plot \"" + ChatColor.BLUE +
                        plot.getName() + ChatColor.GRAY + "\" bauen. ");
            }
        }
    }

    private boolean isAllowedToBuild(Player player, Block block) {
        Plot blockPlot = null;
        for (Plot plot : PlotRegistrar.plots) {
            if (plot.isLocationOnPlot(block.getLocation())) {
                blockPlot = plot;
                break;
            }
        }
        if (blockPlot == null) return true;
        return blockPlot.canBuildOnPlot(player);
    }

    private boolean isAllowedToBuild(Player player, Location location) {
        Plot blockPlot = null;
        for (Plot plot : PlotRegistrar.plots) {
            if (plot.isLocationOnPlot(location)) {
                blockPlot = plot;
                break;
            }
        }
        if (blockPlot == null) return true;
        return blockPlot.canBuildOnPlot(player);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!isAllowedToBuild(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.RED + "Das darfst du hier nicht!"
            ));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!isAllowedToBuild(event.getPlayer(), event.getBlockPlaced())) {
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.RED + "Das darfst du hier nicht!"
            ));
        }
    }

    @EventHandler
    public void onBlockUse(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!isAllowedToBuild(event.getPlayer(), event.getClickedBlock())) {
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.RED + "Das darfst du hier nicht!"
            ));
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.CHEST_BOAT &&
                event.getRightClicked().getType() != EntityType.MINECART_CHEST &&
                event.getRightClicked().getType() != EntityType.MINECART_FURNACE &&
                event.getRightClicked().getType() != EntityType.MINECART_HOPPER) return;
        if (!isAllowedToBuild(event.getPlayer(), event.getRightClicked().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.RED + "Das darfst du hier nicht!"
            ));
        }
    }

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (damager.getType() != EntityType.PLAYER) return;
        Player pdamager = (Player) damager;
        if (!isAllowedToBuild(pdamager, entity.getLocation())) {
            event.setCancelled(true);
            pdamager.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.RED + "Das darfst du hier nicht!"
            ));
        }
    }
}
