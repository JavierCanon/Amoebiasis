/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net;

import eu.nagar.nconnect.api.event.player.PlayerConnectionEvent;
import eu.nagar.nconnect.api.event.player.PlayerDisconnectEvent;
import eu.nagar.nconnect.api.event.socket.SocketHandshakeEvent;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.server.NConnectServer;
import eu.nagar.nconnect.server.config.ConfigOption;
import eu.nagar.nconnect.server.player.PlayerImpl;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WebSocketHandler extends WebSocketServer {
    private NConnectServer server;
    private WebSocketSecurity security = new WebSocketSecurity();
    private Map<WebSocket, PlayerImpl> socketPlayerMap = new HashMap<>();

    public WebSocketHandler(NConnectServer server) {
        super(new InetSocketAddress(ConfigOption.SERVER_PORT.getValueInt()));
        setReuseAddr(true);
        setTcpNoDelay(true);
        this.server = server;
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        if (socketPlayerMap.size() > ConfigOption.SERVER_MAX_CONNECTIONS.getValueInt()) {
            server.getLogger().warn("Rejected handshake from " + conn.getRemoteSocketAddress().toString() + ". Reason: Server is full!");
            throw new InvalidDataException(1);
        }

        if (security.isThrottled(conn, request)) {
            server.getLogger().warn("Rejected handshake from " + conn.getRemoteSocketAddress().toString() + ". Reason: Connection throttled!");
            throw new InvalidDataException(1);
        }

        ServerHandshakeBuilder builder = super.onWebsocketHandshakeReceivedAsServer(conn, draft, request);
        builder.put("X-Forwarded-For", "*");
        builder.put("X-Real-IP", "*");

        SocketHandshakeEvent event = new SocketHandshakeEvent(conn, request, builder);
        server.getEventManager().callEvent(event);

        if (event.isCancelled()) {
            throw new InvalidDataException(1);
        }

        return builder;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        server.getLogger().info(webSocket.getRemoteSocketAddress().toString() + " <-OPEN--> PROXY");

        PlayerImpl player = new PlayerImpl(server, webSocket, clientHandshake);
        PlayerConnectionEvent playerConnectionEvent = new PlayerConnectionEvent(player);
        server.getEventManager().callEvent(playerConnectionEvent);

        if (playerConnectionEvent.isCancelled()) {
            return;
        }

        socketPlayerMap.put(webSocket, player);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String reason, boolean b) {
        server.getLogger().info(webSocket.getRemoteSocketAddress().toString() + " <-CLOSE-> PROXY");
        Player player = socketPlayerMap.get(webSocket);

        PlayerDisconnectEvent playerDisconnectEvent = new PlayerDisconnectEvent(player, i, reason);
        server.getEventManager().callEvent(playerDisconnectEvent);
        socketPlayerMap.remove(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer buffer) {
        PlayerImpl player = socketPlayerMap.get(webSocket);
        if (player == null) {
            webSocket.close();
            return;
        }

        if (buffer.limit() < 1) {
            return;
        }

        if (buffer.limit() > 512) {
            return;
        }

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        player.getUpstreamHandler().handle(buffer);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        socketPlayerMap.remove(webSocket);
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        server.getLogger().info("Server accepting connections on port " + getPort() + ".");
    }

    public Collection<PlayerImpl> getConnectedPlayers() {
        return socketPlayerMap.values();
    }
}
