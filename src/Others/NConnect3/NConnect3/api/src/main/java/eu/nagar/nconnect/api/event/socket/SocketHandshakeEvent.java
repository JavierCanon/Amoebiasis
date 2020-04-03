/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.socket;


import eu.nagar.nconnect.api.event.Cancellable;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

public class SocketHandshakeEvent extends SocketEvent implements Cancellable {
    private String address;
    private ClientHandshake clientHandshake;
    private ServerHandshakeBuilder serverHandshakeBuilder;
    private boolean isCancelled;

    public SocketHandshakeEvent(WebSocket socket, ClientHandshake clientHandshake, ServerHandshakeBuilder serverHandshakeBuilder) {
        super(socket);
        this.clientHandshake = clientHandshake;
        this.serverHandshakeBuilder = serverHandshakeBuilder;

        this.address = socket.getRemoteSocketAddress().toString();
        if (clientHandshake.hasFieldValue("X-Forwarded-For")) {
            this.address = clientHandshake.getFieldValue("X-Forwarded-For");
        }
    }

    public ClientHandshake getClientHandshake() {
        return clientHandshake;
    }

    public ServerHandshakeBuilder getServerHandshakeBuilder() {
        return serverHandshakeBuilder;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
}
