/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.teamselector;

import org.bukkit.entity.Player;

public class TeamSelectorManager {
    public static boolean hasTeamSelected(Player player) {
        return player.hasPermission("justcoding.homburg.plotselected");
    }
}
