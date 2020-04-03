/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.socket;

import eu.nagar.nconnect.api.event.Event;
import org.java_websocket.WebSocket;

public class SocketEvent extends Event {
    private WebSocket socket;

    public SocketEvent(WebSocket socket) {
        this.socket = socket;
    }

    public WebSocket getWebSocket() {
        return socket;
    }
}
