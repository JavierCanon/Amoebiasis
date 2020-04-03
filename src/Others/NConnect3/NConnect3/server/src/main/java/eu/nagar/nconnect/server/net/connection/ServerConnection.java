/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.connection;

import eu.nagar.nconnect.api.event.server.ServerConnectedEvent;
import eu.nagar.nconnect.api.event.server.ServerDisconnectedEvent;
import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.api.server.GameServer;
import eu.nagar.nconnect.server.net.protocol.Packet;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;
import eu.nagar.nconnect.server.net.protocol.PacketRegister;
import eu.nagar.nconnect.server.player.PlayerImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

public class ServerConnection extends WebSocketClient {
    private PlayerImpl player;
    private GameServer server;

    public ServerConnection(PlayerImpl player, GameServer server, URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
        this.player = player;
        this.server = server;

        setReuseAddr(true);
        setTcpNoDelay(true);
    }

    public void sendPacket(Packet packet) {
        PacketCodec packetCodec = PacketRegister.SERVERBOUND.getCodec(packet.getClass());
        if (this.isOpen()) {
            this.send(packetCodec.encode(packet, Protocol.LEGACY_6).array());
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        ServerConnectedEvent serverConnectedEvent = new ServerConnectedEvent(player, server);
        player.getNConnectServer().getEventManager().callEvent(serverConnectedEvent);
        player.getNConnectServer().getLogger().info(player.getSocket().getRemoteSocketAddress().toString() + " <-------> PROXY <--OPEN-> " + server.getName().toUpperCase());
    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onMessage(ByteBuffer buffer) {
        player.getDownstreamHandler().handle(buffer);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        ServerDisconnectedEvent serverDisconnectedEvent = new ServerDisconnectedEvent(player, server, i, s);
        player.getNConnectServer().getEventManager().callEvent(serverDisconnectedEvent);

        if (player.getSocket() == null || player.getSocket().getRemoteSocketAddress() == null) {
            return;
        }

        player.getNConnectServer().getLogger().info(player.getSocket().getRemoteSocketAddress().toString() + " <-------> PROXY <-CLOSE-> " + server.getName().toUpperCase());
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
