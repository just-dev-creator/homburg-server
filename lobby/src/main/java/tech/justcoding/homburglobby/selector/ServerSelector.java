/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburglobby.selector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.justcoding.homburglobby.utils.ItemBuilder;
import tech.justcoding.homburglobby.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ServerSelector implements Listener {
    public static ArrayList<Server> servers = new ArrayList<>();

    public static Inventory getSelector(Player player) {
        List<Server> allowedServers = new ArrayList<>();
        for (Server server : servers) {
            if (player.hasPermission(server.getRequiredPermission())) {
                allowedServers.add(server);
            }
        }
        int inventorySize = 9;
        while (allowedServers.size() > inventorySize) {
            inventorySize = inventorySize + 9;
        }
        Inventory inventory = Bukkit.createInventory(null, inventorySize, ChatColor.BLUE + "Mit einem " +
                "Server verbinden");
        for (Server server : allowedServers) {
            inventory.addItem(server.getSelectorItem());
        }
        player.getInventory().clear();
        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.BLUE + "Mit einem " +
                "Server verbinden") || event.getCurrentItem() == null) return;
        event.setCancelled(true);
        for (Server server : servers) {
            if (event.getCurrentItem().equals(server.getSelectorItem()) &&
                    event.getWhoClicked().hasPermission(server.getRequiredPermission())) {
                connectPlayer((Player) event.getWhoClicked(), server);
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.BLUE + "Mit einem " +
                "Server verbinden")) return;
        setPlayerInventory((Player) event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setPlayerInventory(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getInventory().isEmpty()) setPlayerInventory(event.getPlayer());
    }

    private static void connectPlayer(Player player, Server server) {
        player.sendTitle(ChatColor.BLUE + "Du wirst verbunden...", ChatColor.GRAY + "Dies kann einen Moment dauern!",
                10, 70, 20);
        Utils.sendPlayerToServer(player, server.getBungeeName());
    }

    public static void setPlayerInventory(Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(4, new ItemBuilder(Material.SPYGLASS).
                setName(ChatColor.BLUE + "Server-Selector")
                .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Server-Menü öffnen")
                .toItemStack());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getItem() != null && event.getItem().equals(new ItemBuilder(Material.SPYGLASS).
                    setName(ChatColor.BLUE + "Server-Selector")
                    .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Server-Menü öffnen")
                    .toItemStack())) {
                event.getPlayer().openInventory(getSelector(event.getPlayer()));
            }
        }
    }
}
