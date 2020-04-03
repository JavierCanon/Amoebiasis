/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.server;

import eu.nagar.nconnect.api.event.Cancellable;
import eu.nagar.nconnect.api.event.Event;
import eu.nagar.nconnect.api.player.Player;

public class ServerStatsEvent extends Event implements Cancellable {
    private Player player;
    private String statsJson;
    private boolean isCancelled;

    public ServerStatsEvent(Player player, String statsJson) {
        this.player = player;
        this.statsJson = statsJson;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public String getStatsString() {
        return statsJson;
    }

    public void setStatsJson(String statsJson) {
        this.statsJson = statsJson;
    }
}
