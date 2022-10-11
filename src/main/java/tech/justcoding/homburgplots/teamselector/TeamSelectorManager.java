/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.teamselector;

import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.plots.Plot;

public class TeamSelectorManager {
    public static boolean hasTeamSelected(Player player) {
        return player.hasPermission("justcoding.homburg.plotselected");
    }
    public static void selectTeam(Player player, Plot plot) {
        Node selectedPlotNode = Node.builder("justcoding.homburg.plotselected")
                .value(true)
                .build();
        Node plotNode = Node.builder(plot.getPermissionName())
                .value(true)
                .build();
        User user = Main.luckPerms.getUserManager().getUser(player.getUniqueId());
        user.data().add(selectedPlotNode);
        user.data().add(plotNode);
    }
}
