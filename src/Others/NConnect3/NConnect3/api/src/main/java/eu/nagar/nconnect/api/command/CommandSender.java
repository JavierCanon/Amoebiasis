/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.command;

import java.awt.*;

public interface CommandSender {
    /**
     * Send a message
     * @param message message
     */
    void sendMessage(String message);

    /**
     * Send a colored message
     * @param message message
     * @param color message color
     */
    void sendMessage(String message, Color color);

    /**
     * Check if sender has permission
     * @param permission permission
     * @return boolean
     */
    boolean hasPermission(String permission);
}
