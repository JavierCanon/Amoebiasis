/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.player;

import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.server.GameServer;
import eu.nagar.nconnect.api.util.Position;
import eu.nagar.nconnect.server.NConnectServer;
import eu.nagar.nconnect.server.net.connection.ServerConnection;
import eu.nagar.nconnect.server.net.connection.bridge.DownstreamHandler;
import eu.nagar.nconnect.server.net.connection.bridge.UpstreamHandler;
import eu.nagar.nconnect.server.net.protocol.Packet;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;
import eu.nagar.nconnect.server.net.protocol.PacketRegister;
import eu.nagar.nconnect.server.net.protocol.packet.PacketOutChat;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class PlayerImpl implements Player {
    private NConnectServer server;
    private String name = "";
    private String address;
    private Position mousePosition = new Position(0, 0);
    private WebSocket socket;
    private GameServer gameServer;
    private ServerConnection serverConnection;
    private DownstreamHandler downstreamHandler;
    private UpstreamHandler upstreamHandler;

    public PlayerImpl(NConnectServer server, WebSocket socket, ClientHandshake handshake) {
        this.server = server;
        this.socket = socket;

        if (handshake.hasFieldValue("X-Forwarded-For")) {
            this.address = handshake.getFieldValue("X-Forwarded-For");
        } else {
            this.address = socket.getRemoteSocketAddress().toString();
        }

        this.upstreamHandler = new UpstreamHandler(this);
        this.downstreamHandler = new DownstreamHandler(this);
        this.connectTo(server.getServerManager().getServers().iterator().next());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMousePosition(Position mousePosition) {
        this.mousePosition = mousePosition;
    }

    public DownstreamHandler getDownstreamHandler() {
        return downstreamHandler;
    }

    public UpstreamHandler getUpstreamHandler() {
        return upstreamHandler;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    public WebSocket getSocket() {
        return socket;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public Position getMousePosition() {
        return this.mousePosition;
    }

    @Override
    public void connectTo(GameServer server) {
        try {
            if (serverConnection != null && serverConnection.isOpen()) {
                try {
                    serverConnection.closeBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.serverConnection = new ServerConnection(this, server, new URI(server.getAddress()), new HashMap<String, String>() {{
                put("Cache-Control", "None");
                put("User-Agent", "Nagar (Niall Agar) Group Nconnect Server Upstream Connection N3 v1.3-A");
                put("X-Real-IP", address);
            }});
            this.serverConnection.connect();

            for (String motdLine : server.getMotd()) {
                this.sendMessage(motdLine);
            }
            this.gameServer = server;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameServer getGameServer() {
        return gameServer;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.LEGACY_6;
    }

    @Override
    public void kick() {
        serverConnection.close();
        socket.close();
    }

    public void sendPacket(Packet packet) {
        PacketCodec packetCodec = PacketRegister.CLIENTBOUND.getCodec(packet.getClass());
        if (socket.isOpen()) {
            socket.send(packetCodec.encode(packet, Protocol.LEGACY_6).array());
        }
    }

    @Override
    public void sendMessage(String message) {
        PacketOutChat chat = new PacketOutChat();
        chat.setName("SERVER");
        chat.setColor(Color.GRAY);
        chat.setMessage(message);
        sendPacket(chat);
    }

    @Override
    public void sendMessage(String message, Color color) {
        PacketOutChat chat = new PacketOutChat();
        chat.setName(message);
        chat.setColor(color);
        chat.setMessage("");
        sendPacket(chat);
    }

    @Override
    public boolean hasPermission(String permission) {
        //TODO Implement permissions system.
        return permission.equals("nconnect.help")
                || permission.equals("nconnect.server")
                || permission.equals("nconnect.plugins");
    }

    public NConnectServer getNConnectServer() {
        return server;
    }

}
