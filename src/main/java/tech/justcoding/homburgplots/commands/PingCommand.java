/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tech.justcoding.homburgplots.Main;

public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getNoPlayer());
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Main.getPrefix() + "Dein aktueller Ping beträgt " + ChatColor.BLUE + player.getPing() +
                    ChatColor.GRAY + ".");
            return true;
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (!player.hasPermission("justcoding.homburg.ping.other")) {
                player.sendMessage(Main.getPrefix() + "Dein aktueller Ping beträgt " + ChatColor.BLUE + player.getPing() +
                        ChatColor.GRAY + ".");
                return true;
            }
            if (target == null || !target.isOnline()) {
                player.sendMessage(Main.getErrorPrefix() + "Dieser Spieler ist zur Zeit nicht online!");
                return false;
            }
            player.sendMessage(Main.getPrefix() + "Der Ping von " + ChatColor.BLUE + target.getName() + ChatColor.GRAY +
                    " beträgt " + ChatColor.BLUE + target.getPing() + ChatColor.GRAY + ".");
            return true;
        }
    }
}
