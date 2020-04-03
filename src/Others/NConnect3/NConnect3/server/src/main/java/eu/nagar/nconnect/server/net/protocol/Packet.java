/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol;

public abstract class Packet {
    public abstract void handle(PacketReceiver receiver);
}
