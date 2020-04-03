/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event;

public interface Cancellable {
    /**
     * Set the cancelled state for this event.
     * @param cancel cancel
     */
    void setCancelled(boolean cancel);

    /**
     * Event cancelled.
     * @return cancelled
     */
    boolean isCancelled();
}
