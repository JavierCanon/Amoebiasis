/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.player;

import eu.nagar.nconnect.api.player.Player;

public class PlayerDisconnectEvent extends PlayerEvent {
    private int closeCode;
    private String reason;

    public PlayerDisconnectEvent(Player player, int closeCode, String reason) {
        super(player);
        this.closeCode = closeCode;
        this.reason = reason;
    }

    public int getCloseCode() {
        return closeCode;
    }

    public String getReason() {
        return reason;
    }
}
