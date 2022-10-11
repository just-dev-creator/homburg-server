package tech.justcoding.homburgplots;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import tech.justcoding.homburgplots.commands.PingCommand;
import tech.justcoding.homburgplots.plots.PlotBossbar;
import tech.justcoding.homburgplots.plots.PlotBuildCheck;
import tech.justcoding.homburgplots.plots.PlotRegistrar;
import tech.justcoding.homburgplots.plots.SavePlotCommand;
import tech.justcoding.homburgplots.plots.SpawnPlotProtection;
import tech.justcoding.homburgplots.recipes.PlotTeleportCompass;
import tech.justcoding.homburgplots.recipes.RecipeManager;
import tech.justcoding.homburgplots.teamselector.TeamSelector;
import tech.justcoding.homburgplots.teamselector.TeamSelectorManager;
import tech.justcoding.homburgplots.utils.Config;

public final class Main extends JavaPlugin {

    public static LuckPerms luckPerms = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Config.registerConfiguration();
        PlotRegistrar.getPlotsFromConfig();
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
        registerCommands();
        registerListeners();
        RecipeManager.registerAllRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getCommand("manageplot").setExecutor(new SavePlotCommand());
        getCommand("ping").setExecutor(new PingCommand());
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlotBossbar(), this);
        pluginManager.registerEvents(new PlotBuildCheck(), this);
        pluginManager.registerEvents(new PlotTeleportCompass(), this);
        pluginManager.registerEvents(new SpawnPlotProtection(), this);
        pluginManager.registerEvents(new TeamSelector(), this);
    }

    public static String getPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.BLUE  + "Server" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getNetworkPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.BLUE  + "Network" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getErrorPrefix() {
        return  ChatColor.DARK_GRAY + "┃ " + ChatColor.DARK_RED  + "Server" + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY;
    }
    public static String getNoPlayer() {
        return getErrorPrefix() + "Lediglich Spieler können diese Aktion ausführen.";
    }
    public static String getNoPermission() {
        return getErrorPrefix() + "Dir fehlen die für die Ausführung dieser Aktion benötigten Rechte.";
    }
}
