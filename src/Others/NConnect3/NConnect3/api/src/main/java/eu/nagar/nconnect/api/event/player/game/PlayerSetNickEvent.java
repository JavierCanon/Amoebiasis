/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.player.game;

import eu.nagar.nconnect.api.event.Cancellable;
import eu.nagar.nconnect.api.event.player.PlayerEvent;
import eu.nagar.nconnect.api.player.Player;

public class PlayerSetNickEvent extends PlayerEvent implements Cancellable {
    private String nickname;
    private boolean cancelled;

    public PlayerSetNickEvent(Player player, String nickname) {
        super(player);
        this.nickname = nickname;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
