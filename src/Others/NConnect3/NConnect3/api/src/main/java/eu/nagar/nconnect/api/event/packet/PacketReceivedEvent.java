/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.event.packet;

import eu.nagar.nconnect.api.event.Cancellable;
import eu.nagar.nconnect.api.event.Event;
import eu.nagar.nconnect.api.player.Player;

import java.nio.ByteBuffer;

public class PacketReceivedEvent extends Event implements Cancellable {
    private int packetId;
    private Player player;
    private ByteBuffer buffer;
    private boolean cancelled;

    public PacketReceivedEvent(Player player, ByteBuffer buffer) {
        this.player = player;
        this.packetId = buffer.get(0);
        this.buffer = buffer;
    }

    public Player getPlayer() {
        return player;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public int getPacketId() {
        return packetId;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
