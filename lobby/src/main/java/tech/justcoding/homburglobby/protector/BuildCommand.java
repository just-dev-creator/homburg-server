/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburglobby.protector;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.justcoding.homburglobby.Main;
import tech.justcoding.homburglobby.selector.ServerSelector;

import java.util.ArrayList;

public class BuildCommand implements CommandExecutor {
    public static ArrayList<Player> builders = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getNoPlayer());
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("lobby.commands.build")) {
            player.sendMessage(Main.getNoPermission());
            return false;
        }
        if (builders.contains(player)) {
            builders.remove(player);
            player.setGameMode(GameMode.ADVENTURE);
            ServerSelector.setPlayerInventory(player);
            player.sendMessage(Main.getPrefix() + "Du kannst jetzt nicht mehr bauen!");
        } else {
            builders.add(player);
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.sendMessage(Main.getPrefix() + "Du kannst jetzt bauen!");
        }
        return true;
    }
}
