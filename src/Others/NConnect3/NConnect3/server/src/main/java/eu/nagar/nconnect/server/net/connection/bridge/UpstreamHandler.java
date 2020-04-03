/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.connection.bridge;

import eu.nagar.nconnect.api.command.CommandResult;
import eu.nagar.nconnect.api.event.packet.PacketReceivedEvent;
import eu.nagar.nconnect.api.event.player.game.PlayerChatEvent;
import eu.nagar.nconnect.api.event.player.game.PlayerMouseEvent;
import eu.nagar.nconnect.api.event.player.game.PlayerSetNickEvent;
import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.api.util.Position;
import eu.nagar.nconnect.server.net.protocol.*;
import eu.nagar.nconnect.server.net.protocol.packet.PacketInChat;
import eu.nagar.nconnect.server.net.protocol.packet.PacketInMouse;
import eu.nagar.nconnect.server.net.protocol.packet.PacketInSetNick;
import eu.nagar.nconnect.server.player.PlayerImpl;

import java.nio.ByteBuffer;

public class UpstreamHandler extends PacketReceiver {
    private PlayerImpl player;
    public UpstreamHandler(PlayerImpl player) {
        this.player = player;
    }

    @Override
    public void handle(ByteBuffer buffer) {
        PacketReceivedEvent packetReceivedEvent = new PacketReceivedEvent(player, buffer);
        player.getNConnectServer().getEventManager().callEvent(packetReceivedEvent);

        if (packetReceivedEvent.isCancelled()) {
            return;
        }

        PacketCodec packetCodec = PacketRegister.SERVERBOUND.getCodec(buffer.get(0) & 0xFF);
        if (packetCodec == null) {
            if (player.getServerConnection().isOpen()) {
                player.getServerConnection().send(buffer);
            }

            return;
        }

        Packet packet = packetCodec.decode(buffer, Protocol.LEGACY_6);

        if (packet != null) {
            handle(packet);
        }
    }

    @Override
    public void handle(Packet packet) {
        packet.handle(this);
    }

    @Override
    public void handle(PacketInChat packet) {
        PlayerChatEvent playerChatEvent = new PlayerChatEvent(player, packet.getMessage());
        player.getNConnectServer().getEventManager().callEvent(playerChatEvent);

        if (packet.getMessage().startsWith("/")) {
            CommandResult result = player.getNConnectServer().getCommandManager().dispatchCommand(packet.getMessage().substring(1), player);
            if (result != CommandResult.NOT_FOUND) {
                return;
            }
        }

        if (!playerChatEvent.isCancelled()) {
            packet.setMessage(playerChatEvent.getMessage());
            player.getServerConnection().sendPacket(packet);
        }
    }

    @Override
    public void handle(PacketInMouse packet) {
        PlayerMouseEvent playerMouseEvent = new PlayerMouseEvent(player, new Position((int) packet.getMouseX(), (int) packet.getMouseY()));
        player.getNConnectServer().getEventManager().callEvent(playerMouseEvent);

        if (!playerMouseEvent.isCancelled()) {
            packet.setMouseX(playerMouseEvent.getMousePosition().getX());
            packet.setMouseY(playerMouseEvent.getMousePosition().getY());

            player.setMousePosition(playerMouseEvent.getMousePosition());
            player.getServerConnection().sendPacket(packet);
        }
    }

    @Override
    public void handle(PacketInSetNick packet) {
        PlayerSetNickEvent playerSetNickEvent = new PlayerSetNickEvent(player, packet.getName());
        player.getNConnectServer().getEventManager().callEvent(playerSetNickEvent);

        if (!playerSetNickEvent.isCancelled()) {
            packet.setName(playerSetNickEvent.getNickname());
            player.setName(playerSetNickEvent.getNickname());
            player.getServerConnection().sendPacket(packet);
        }
    }
}
