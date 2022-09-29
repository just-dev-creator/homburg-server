/*
 * Copyright (c) 2022 justcoding.tech.
 * All rights reserved.
 * You may not copy, modify, decompile or distribute this code without prior written notice from the author.
 */

package tech.justcoding.homburgproxy;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class HubCommand implements RawCommand {
    private final ProxyServer proxy;

    public HubCommand(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();
        Optional<RegisteredServer> server = this.proxy.getServer("lobby");
        if (!server.isPresent()) {
            player.sendMessage(Component.text("Es existiert aktuell kein freier Lobby-Server"));
            return;
        }
        player.createConnectionRequest(server.get()).fireAndForget();
    }
}
