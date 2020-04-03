/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol;

import eu.nagar.nconnect.server.net.protocol.packet.*;

import java.nio.ByteBuffer;

public abstract class PacketReceiver {
    public abstract void handle(ByteBuffer buffer);

    public void handle(Packet packet) {
        packet.handle(this);
    }

    public void handle(PacketInChat packet) {

    }

    public void handle(PacketInMouse packet) {

    }

    public void handle(PacketInSetNick packet) {

    }

    public void handle(PacketOutChat packet) {

    }

    public void handle(PacketOutStats packet) {

    }

}
