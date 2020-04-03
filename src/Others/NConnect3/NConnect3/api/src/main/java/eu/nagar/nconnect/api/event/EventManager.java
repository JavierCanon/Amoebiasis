/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event;

import eu.nagar.nconnect.api.extension.Extension;

public interface EventManager {
    /**
     * Post an event to the event bus.
     * @param event event
     */
    void callEvent(Event event);

    /**
     * Register events from a class
     * @param extension extension instance
     * @param listener class
     */
    void registerEvents(Extension extension, EventListener listener);

    /**
     * Unregister all events from a extension.
     */
    void unregisterEvents(Extension extension);

    /**
     * Unregister all events.
     */
    void unregisterEvents();
}
