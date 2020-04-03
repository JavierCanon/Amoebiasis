/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.server;

import eu.nagar.nconnect.api.event.Event;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.server.GameServer;

public class ServerDisconnectedEvent extends Event {
    private Player player;
    private GameServer server;
    private int closeCode;
    private String reason;

    public ServerDisconnectedEvent(Player player, GameServer server, int closeCode, String reason) {
        this.player = player;
        this.server = server;
        this.closeCode = closeCode;
        this.reason = reason;
    }

    public Player getPlayer() {
        return player;
    }

    public GameServer getServer() {
        return server;
    }

    public int getCloseCode() {
        return closeCode;
    }

    public String getReason() {
        return reason;
    }
}
