/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburglobby.protector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.justcoding.homburglobby.Main;
import tech.justcoding.homburglobby.utils.Config;
import tech.justcoding.homburglobby.utils.Utils;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getNoPlayer());
            return false;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("lobby.commands.setspawn")) {
            player.sendMessage(Main.getNoPermission());
            return false;
        }
        Config.set("spawnLocation", Utils.getStringLocation(player.getLocation()));
        player.sendMessage(Main.getPrefix() + "Deine Position wurde erfolgreich abgespeichert! ");
        return true;
    }
}
