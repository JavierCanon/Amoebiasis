/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.player.game;

import eu.nagar.nconnect.api.event.Cancellable;
import eu.nagar.nconnect.api.event.player.PlayerEvent;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.util.Position;

public class PlayerMouseEvent extends PlayerEvent implements Cancellable {
    private Position mousePosition;
    private boolean cancelled;

    public PlayerMouseEvent(Player player, Position mousePosition) {
        super(player);
        this.mousePosition = mousePosition;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public Position getMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(Position mousePosition) {
        this.mousePosition = mousePosition;
    }
}
