/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.recipes;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.plots.Plot;
import tech.justcoding.homburgplots.plots.PlotRegistrar;
import tech.justcoding.homburgplots.utils.ItemBuilder;
import tech.justcoding.homburgplots.utils.Utils;

public class EmergencyCompass implements Listener {
    private static ItemStack item = new ItemBuilder(Material.COMPASS)
            .setName(ChatColor.RED + "Emergency-Kompass")
            .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Zu deinem Plot " +
                    "teleportieren")
            .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Linksklick] Aktuelle Position " +
                    "speichern")
            .addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
            .toItemStack();

    public static void registerRecipe() {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "emergency_compass");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("RTR", "DGD", "RTR");
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        recipe.setIngredient('D', Material.DIAMOND);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (!event.getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) return;
        event.setCancelled(true);
        NamespacedKey locationKey = new NamespacedKey(Main.getPlugin(Main.class), "location");
        NamespacedKey ownerKey = new NamespacedKey(Main.getPlugin(Main.class), "owner");
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
//            Find out on which plot the player is
            Player player = event.getPlayer();
            Location location = player.getLocation();
            Plot plot = null;
            for (Plot iPlot : PlotRegistrar.plots) {
                if (iPlot.isLocationOnPlot(location)) {
                    plot = iPlot;
                    break;
                }
            }
            if (plot == null) {
                player.sendMessage(Main.getErrorPrefix() + "Du kannst dieses Item nur auf Plots benutzen! ");
                return;
            }
            if (!plot.canBuildOnPlot(player)) {
                player.sendMessage(Main.getErrorPrefix() + "Du kannst dieses Item nur auf Plots benutzen, auf denen " +
                        "du bauen kannst! ");
                return;
            }
            meta.getPersistentDataContainer().set(locationKey, PersistentDataType.STRING, Utils.getStringLocation(
                    event.getPlayer().getLocation()));
            meta.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, player.getUniqueId().toString());
            player.getInventory().getItemInMainHand().setItemMeta(meta);
            player.setCompassTarget(location);
            player.sendMessage(Main.getPrefix() + "Du hast deine aktuelle Position gespeichert!");
        } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            if (!meta.getPersistentDataContainer().has(locationKey, PersistentDataType.STRING) ||
                    !meta.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                player.sendMessage(Main.getErrorPrefix() + "Speichere mit Rechtsklick eine Position in deinen magischen " +
                        "Kompass!");
                return;
            }
            if (!meta.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING).
                    equals(player.getUniqueId().toString())) {
                player.sendMessage(Main.getErrorPrefix() + "Du kannst den Emergency-Kompass nur verwenden, wenn du " +
                        "die Position selber eingespeichert hast!");
                return;
            }
            Location targetLocation = Utils.getLocationString(meta.getPersistentDataContainer().
                    get(locationKey, PersistentDataType.STRING));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,  1);
            player.spawnParticle(Particle.TOTEM, player.getLocation(), 60);
            player.teleport(targetLocation);
            player.spawnParticle(Particle.TOTEM, player.getLocation(), 20);
            player.getInventory().remove(event.getItem());
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Glück " +
                    "gehabt!"));
        }
    }
}
