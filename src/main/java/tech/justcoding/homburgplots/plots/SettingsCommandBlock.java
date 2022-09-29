package tech.justcoding.homburgplots.plots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.utils.ItemBuilder;

public class SettingsCommandBlock implements Listener {

    private Inventory getSettingsInventory(Plot plot) {
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Plot-Einstellungen");
        
        inventory.setItem(0, new ItemBuilder(Material.ACACIA_BUTTON)
        .setName(ChatColor.BLUE + "Andere Spieler dürfen mit Türen, Knöpfen usw. auf diesem Plot interagieren")
        .toItemStack());
        
        return inventory;
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getClickedBlock().getType().equals(Material.COMMAND_BLOCK)) return;
        // Check what plot the block is on
        Location blockLocation = event.getClickedBlock().getLocation();
        Plot plot = null;
        for (Plot iPlot : PlotRegistrar.plots) {
            if (iPlot.isLocationOnPlot(blockLocation)) {
                plot = iPlot;
                break;
            }
        }
        Player player = event.getPlayer();
        if (plot == null | !plot.canBuildOnPlot(event.getPlayer())) {
            player.sendMessage(Main.getErrorPrefix() + "Du kannst Einstellungen lediglich auf deinem " + 
            " eigenen Plot verändern!");
            return;
        }

    }
}
