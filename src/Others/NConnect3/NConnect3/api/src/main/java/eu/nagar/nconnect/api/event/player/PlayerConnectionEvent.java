/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.player;

import eu.nagar.nconnect.api.event.Cancellable;
import eu.nagar.nconnect.api.player.Player;

public class PlayerConnectionEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;

    public PlayerConnectionEvent(Player player) {
        super(player);
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
