/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburglobby.selector;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.justcoding.homburglobby.utils.ItemBuilder;

public class Server {
    private String name;
    private String bungeeName;
    private String requiredPermission;
    private Material selectorMaterial;

    public Server(String name, String bungeeName, String requiredPermission, String selectorMaterial) {
        this.name = name;
        this.bungeeName = bungeeName;
        this.requiredPermission = requiredPermission;
        try {
            this.selectorMaterial = Material.valueOf(selectorMaterial);
        } catch (IllegalArgumentException e) {
            this.selectorMaterial = Material.STRUCTURE_VOID;
        }
    }

    public String getName() {
        return name;
    }

    public String getBungeeName() {
        return bungeeName;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public ItemStack getSelectorItem() {
        return new ItemBuilder(this.selectorMaterial)
                .setName(this.name)
                .addLoreLine(ChatColor.GRAY.toString() + ChatColor.ITALIC + "[Rechtsklick] Auf Server verbinden")
                .toItemStack();
    }
}
