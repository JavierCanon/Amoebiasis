/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol;

import eu.nagar.nconnect.api.net.protocol.Protocol;

import java.nio.ByteBuffer;

public interface PacketCodec<T extends Packet> {
    T decode(ByteBuffer buffer, Protocol protocol);
    ByteBuffer encode(T payload, Protocol protocol);
}
