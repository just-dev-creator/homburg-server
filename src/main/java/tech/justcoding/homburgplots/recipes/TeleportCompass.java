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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.utils.ItemBuilder;
import tech.justcoding.homburgplots.utils.Utils;

import java.util.ArrayList;

public class TeleportCompass implements Listener {
    private static ItemStack item = new ItemBuilder(Material.COMPASS)
            .setName(ChatColor.YELLOW + "Teleporter")
            .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Zu der gespeicherten " +
                    "Position teleportieren")
            .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Linksklick] Aktuelle Position " +
                    "speichern")
            .addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1)
            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
            .toItemStack();

    public static void registerRecipe() {
        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "specific_teleport_compass");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("GDG", "RER", "GDG");
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('D', Material.DIAMOND);

        Bukkit.addRecipe(recipe);
    }

    private ArrayList<Player> wantsTeleport = new ArrayList<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (event.getItem().getItemMeta().getLore() == null ||
                !event.getItem().getItemMeta().getLore().get(0).equals(item.getItemMeta().getLore().get(0))) return;
        if(event.getHand() != EquipmentSlot.HAND) return;
        event.setCancelled(true);
        NamespacedKey locationKey = new NamespacedKey(Main.getPlugin(Main.class), "location");
        NamespacedKey chargeLevel = new NamespacedKey(Main.getPlugin(Main.class), "chargelevel");
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
//            Find out on which plot the player is
            Player player = event.getPlayer();
            if (player.isSneaking()) {
                if (!meta.getPersistentDataContainer().has(chargeLevel, PersistentDataType.INTEGER)) return;
                int level = meta.getPersistentDataContainer().get(chargeLevel, PersistentDataType.INTEGER);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                        ChatColor.BLUE + "Aktuelles Charge-Level: " + level + " Benutzungen"
                ));
                return;
            }
            Location location = player.getLocation();
            meta.getPersistentDataContainer().set(locationKey, PersistentDataType.STRING, Utils.getStringLocation(
                    event.getPlayer().getLocation()));
            meta.getPersistentDataContainer().set(chargeLevel, PersistentDataType.INTEGER, 4);
            player.getInventory().getItemInMainHand().setItemMeta(meta);
            player.setCompassTarget(location);
            player.sendMessage(Main.getPrefix() + "Du hast deine aktuelle Position gespeichert!");
        } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            if (player.isSneaking()) {
                Inventory playerInventory = player.getInventory();
                if (!playerInventory.contains(Material.GOLD_INGOT)) {
                    player.sendMessage(Main.getErrorPrefix() + "Du kannst deinen Teleporter mit Gold aufladen. ");
                    return;
                }
                if (!meta.getPersistentDataContainer().has(chargeLevel, PersistentDataType.INTEGER)) return;
                int oldLevel = meta.getPersistentDataContainer().get(chargeLevel, PersistentDataType.INTEGER);
                if (oldLevel >= 4) {
                    player.sendMessage(Main.getErrorPrefix() + "Ein Teleporter kann lediglich für vier Benutzungen " +
                            "gecharged sein. ");
                    return;
                }
                Utils.reduceItemStackByAmount(player, Material.GOLD_INGOT, 1);
                int newLevel = oldLevel + 1;
                meta.getPersistentDataContainer().set(chargeLevel, PersistentDataType.INTEGER, newLevel);
                player.getInventory().getItemInMainHand().setItemMeta(meta);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN +
                        "Neues Charge-Level: " + newLevel));
                return;
            }
            if (!meta.getPersistentDataContainer().has(locationKey, PersistentDataType.STRING) ||
                    !meta.getPersistentDataContainer().has(chargeLevel, PersistentDataType.INTEGER)) {
                player.sendMessage(Main.getErrorPrefix() + "Speichere mit Rechtsklick eine Position in deinen magischen " +
                        "Kompass!");
                return;
            }
            int charge = meta.getPersistentDataContainer().get(chargeLevel, PersistentDataType.INTEGER);

            if (charge == 0) {
                player.sendMessage(Main.getErrorPrefix() + "Lade deinen Teleporter mit Gold auf, indem du " +
                        "gleichzeitig Shift und Rechtsklick drückst. ");
                return;
            }
            Location targetLocation = Utils.getLocationString(meta.getPersistentDataContainer().
                    get(locationKey, PersistentDataType.STRING));
            wantsTeleport.add(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "Du wirst in " +
                    "drei Sekunden teleportiert. "));
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                if (!wantsTeleport.contains(player)) return;
                meta.getPersistentDataContainer().set(chargeLevel, PersistentDataType.INTEGER, charge - 1);
                player.getInventory().getItemInMainHand().setItemMeta(meta);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED +
                        "Verbleibendes Charge-Level: " + (charge - 1) + " Benutzungen"));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,  1);
                player.spawnParticle(Particle.TOTEM, player.getLocation(), 20);
                player.teleport(targetLocation);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,  1);
                wantsTeleport.remove(player);
            }, 60);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;
        if (!wantsTeleport.contains(event.getPlayer())) return;
        Player player = event.getPlayer();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Du hast den " +
                "Teleport erfolgreich abgebrochen. "));
        wantsTeleport.remove(player);
    }
}
