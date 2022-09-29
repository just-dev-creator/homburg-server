package tech.justcoding.homburgplots.plots;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class SpawnPlotProtection implements Listener {
    @EventHandler
    public void onBlockChangeEntity(EntityChangeBlockEvent event) {
        Location location = event.getBlock().getLocation();
        if (PlotRegistrar.spawnPlot.isLocationOnPlot(location)) {
            if (event.getEntityType().equals(EntityType.PLAYER)) {
                if (PlotRegistrar.spawnPlot.canBuildOnPlot((Player) event.getEntity())) {
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (PlotRegistrar.spawnPlot.isLocationOnPlot(event.getLocation())) {
            event.setCancelled(true);
        }
    }
}
