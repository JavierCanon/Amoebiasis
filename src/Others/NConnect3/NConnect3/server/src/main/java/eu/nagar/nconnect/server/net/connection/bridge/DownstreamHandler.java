/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.connection.bridge;

import eu.nagar.nconnect.api.event.server.ServerStatsEvent;
import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.server.net.protocol.*;
import eu.nagar.nconnect.server.net.protocol.packet.PacketOutChat;
import eu.nagar.nconnect.server.net.protocol.packet.PacketOutStats;
import eu.nagar.nconnect.server.player.PlayerImpl;

import java.nio.ByteBuffer;

public class DownstreamHandler extends PacketReceiver {
    private PlayerImpl player;

    public DownstreamHandler(PlayerImpl player) {
        this.player = player;
    }

    @Override
    public void handle(ByteBuffer buffer) {
        PacketCodec packetCodec = PacketRegister.CLIENTBOUND.getCodec(buffer.get(0));
        if (packetCodec == null) {
            if (player.getSocket().isOpen()) {
                player.getSocket().send(buffer.array());
            }
            return;
        }

        Packet packet = packetCodec.decode(buffer, Protocol.LEGACY_6);

        if (packet != null) {
            player.getDownstreamHandler().handle(packet);
        }
    }

    @Override
    public void handle(Packet packet) {
        super.handle(packet);
    }

    @Override
    public void handle(PacketOutChat packet) {
        player.sendPacket(packet);
    }

    @Override
    public void handle(PacketOutStats packet) {
        ServerStatsEvent serverStatsEvent = new ServerStatsEvent(player, packet.getStats());
        player.getNConnectServer().getEventManager().callEvent(serverStatsEvent);
        if (!serverStatsEvent.isCancelled()) {
            packet.setStats(serverStatsEvent.getStatsString());
            player.sendPacket(packet);
        }
    }
}
