/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.server;

import eu.nagar.nconnect.api.event.Event;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.server.GameServer;

public class ServerConnectedEvent extends Event {
    private Player player;
    private GameServer server;

    public ServerConnectedEvent(Player player, GameServer server) {
        this.player = player;
        this.server = server;
    }

    public Player getPlayer() {
        return player;
    }

    public GameServer getServer() {
        return server;
    }
}
