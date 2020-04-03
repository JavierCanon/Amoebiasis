/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.codec;

import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;
import eu.nagar.nconnect.server.net.protocol.packet.PacketOutStats;
import eu.nagar.nconnect.server.util.BinaryUtils;

import java.nio.ByteBuffer;

public class CodecOutStats implements PacketCodec<PacketOutStats> {
    @Override
    public PacketOutStats decode(ByteBuffer buffer, Protocol protocol) {
        PacketOutStats payload = new PacketOutStats();
        buffer.get();

        payload.setStats(BinaryUtils.decode(buffer));
        return payload;
    }

    @Override
    public ByteBuffer encode(PacketOutStats payload, Protocol protocol) {
        ByteBuffer buffer = ByteBuffer.allocate(256);

        buffer.put((byte) 254);
        buffer.put(BinaryUtils.encode(payload.getStats()));

        return buffer;
    }
}
