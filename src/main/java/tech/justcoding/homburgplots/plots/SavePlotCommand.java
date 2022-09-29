/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgplots.plots;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tech.justcoding.homburgplots.Main;
import tech.justcoding.homburgplots.utils.Config;

import java.util.HashMap;

public class SavePlotCommand implements CommandExecutor {
    public static HashMap<Player, Location> loc1 = new HashMap<>();
    public static HashMap<Player, Location> loc2 = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getNoPlayer());
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("save1")) {
                loc1.put(player, player.getLocation());
                sender.sendMessage(Main.getPrefix() + "Deine aktuelle Position wurde erfolgreich in Slot eins " +
                        "gespeichert. ");
            } else if (args[0].equalsIgnoreCase("save2")) {
                loc2.put(player, player.getLocation());
                sender.sendMessage(Main.getPrefix() + "Deine aktuelle Position wurde erfolgreich in Slot zwei " +
                        "gespeichert. ");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("savePlot")) {
                String plotName = args[1];
                Location location1 = loc1.get(player);
                Location location2 = loc2.get(player);
                int from_x;
                int from_z;
                int to_x;
                int to_z;
                if (location1.getBlockX() > location2.getBlockX()) {
                    from_x = location2.getBlockX();
                    to_x = location1.getBlockX();
                } else {
                    from_x = location1.getBlockX();
                    to_x = location2.getBlockX();
                }
                if (location1.getBlockZ() > location2.getBlockZ()) {
                    from_z = location2.getBlockZ();
                    to_z = location1.getBlockZ();
                } else {
                    from_z = location1.getBlockZ();
                    to_z = location2.getBlockZ();
                }
                String configurationKey = "Plots." + plotName.replace(" ", "_").replace(".", "__");

                ConfigurationSection configurationSection = Config.getSection(configurationKey);
                configurationSection.set("displayName", plotName);
                configurationSection.set("from_x", from_x);
                configurationSection.set("from_z", from_z);
                configurationSection.set("to_x", to_x);
                configurationSection.set("to_z", to_z);
                Config.set(configurationKey, configurationSection);
                PlotRegistrar.plots.add(new Plot(
                        from_x,
                        from_z,
                        to_x,
                        to_z,
                        plotName
                ));
                sender.sendMessage(Main.getPrefix() + "Das ausgewählte Plot wurde unter dem Config-Key " +
                        ChatColor.BLUE + configurationKey + ChatColor.GRAY + " gespeichert. ");
            }
        }
        return false;
    }
}
