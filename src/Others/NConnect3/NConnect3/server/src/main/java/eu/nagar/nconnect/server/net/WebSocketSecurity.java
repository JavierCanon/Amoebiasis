/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.HashMap;
import java.util.Map;

public class WebSocketSecurity {
    private static Map<String, Long> lastConnection = new HashMap<>();
    private static final int THROTTLE_TIME = 800;

    public boolean isThrottled(WebSocket socket, ClientHandshake handshake) {
        String address = socket.getRemoteSocketAddress().toString();
        if (handshake.hasFieldValue("X-Forwarded-For")) {
            address = handshake.getFieldValue("X-Forwarded-For");
        }

        if (!lastConnection.containsKey(address)) {
            lastConnection.put(address, System.currentTimeMillis());
            return false;
        }

        return System.currentTimeMillis() - lastConnection.get(address) < THROTTLE_TIME;
    }
}
