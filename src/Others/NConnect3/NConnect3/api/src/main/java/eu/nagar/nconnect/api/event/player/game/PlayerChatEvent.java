/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.player.game;

import eu.nagar.nconnect.api.event.Cancellable;
import eu.nagar.nconnect.api.event.player.PlayerEvent;
import eu.nagar.nconnect.api.player.Player;

public class PlayerChatEvent extends PlayerEvent implements Cancellable {
    private String message;
    private boolean cancelled;

    public PlayerChatEvent(Player player, String message) {
        super(player);
        this.message = message;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
