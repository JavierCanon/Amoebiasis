/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.player;

import eu.nagar.nconnect.api.event.Event;
import eu.nagar.nconnect.api.player.Player;

public class PlayerEvent extends Event {
    private Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
