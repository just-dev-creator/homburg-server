package tech.justcoding.homburgproxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
        id = "homburgproxy",
        name = "homburgproxy",
        version = "1.0-SNAPSHOT"
)
public class Main {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }

    @Inject
    private Main(ProxyServer proxy, CommandManager manager) {
        manager.register("hub", new HubCommand(proxy));
        manager.register("l", new HubCommand(proxy));
        manager.register("lobby", new HubCommand(proxy));
        manager.register("leave", new HubCommand(proxy));
    }
}
