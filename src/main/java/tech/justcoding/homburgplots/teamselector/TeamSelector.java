/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.teamselector;

import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.plots.Plot;
import tech.justcoding.homburgplots.plots.PlotRegistrar;
import tech.justcoding.homburgplots.utils.ItemBuilder;

import java.util.Random;

public class TeamSelector implements Listener {
    public static Inventory getSelector() {
        ItemStack book = new ItemBuilder(Material.BOOK)
                .setName(ChatColor.BLUE + "Hilfe")
                .addLoreLine(ChatColor.GRAY + "Willkommen auf dem offiziellen Bezirk-Homburg-Minecraft-Server! ")
                .addLoreLine(ChatColor.GRAY + "Es gibt zwei Kategorien für Bereiche: Die Wildnis und Plots. ")
                .addLoreLine(ChatColor.GRAY + "In der Wildnis kann jeder bauen und abbauen. ")
                .addLoreLine(ChatColor.GRAY + "Auf den Plots können lediglich Personen bauen, die ihnen zugehörig sind. ")
                .addLoreLine(ChatColor.GRAY + "Jeder Stamm hat ein Plot, das jeweils 4x4 Chunks groß ist. ")
                .addLoreLine(ChatColor.GRAY + "Wähle deinen Stamm rechts aus, um auf dem Plot bauen zu können. ")
                .addLoreLine(ChatColor.GRAY + "Du kannst diese Auswahl später nicht mehr ändern!")
                .addLoreLine(ChatColor.GRAY + "Auf Nachfrage können Admins aber deine Zugehörigkeit bearbeiten. ")
                .toItemStack();
        int inventorySize = 9;
        while (PlotRegistrar.plots.size() - 1 > inventorySize- 1) {
            inventorySize = inventorySize + 9;
        }
        Inventory inventory = Bukkit.createInventory(null, inventorySize, ChatColor.BLUE + "Team-Selector");
        inventory.setItem(0, book);
        for (Plot plot : PlotRegistrar.plots) {
            if (plot != PlotRegistrar.spawnPlot) {
                ItemStack item = new ItemBuilder(Material.PINK_GLAZED_TERRACOTTA)
                        .setName(ChatColor.GRAY + "Wähle " + ChatColor.BLUE + plot.getName())
                        .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Wähle diesen Stamm aus")
                        .toItemStack();
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(Main.class), "permission"),
                        PersistentDataType.STRING, plot.getPermissionName());
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
        }
        return inventory;
    }

    public static boolean hasTeamSelected(Player player) {
        return player.hasPermission("justcoding.homburg.plotselected");
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Team-Selector")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.BOOK)) {
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                String permission = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(Main.class), "permission"),
                        PersistentDataType.STRING);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  "lp user " + event.getWhoClicked().getName() + " permission set justcoding.homburg.plotselected true");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  "lp user " + event.getWhoClicked().getName() + " permission set " + permission + " true");
                event.getWhoClicked().sendMessage(Main.getPrefix() + "Du hast dich erfolgreich deinem Stamm zugeordnet! ");
                event.getWhoClicked().sendMessage(Main.getPrefix() + "Du findest die Koordinaten der Plots am Spawn. ");
                event.getWhoClicked().sendMessage(Main.getPrefix() + "Verklickt? Admins können deine Zuordnung ändern. ");
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!TeamSelectorManager.hasTeamSelected(event.getPlayer())) {
            event.getPlayer().openInventory(getSelector());
        }
    }
}
