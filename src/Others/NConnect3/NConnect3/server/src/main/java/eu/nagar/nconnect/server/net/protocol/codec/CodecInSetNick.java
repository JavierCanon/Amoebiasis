/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.codec;

import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;
import eu.nagar.nconnect.server.net.protocol.packet.PacketInSetNick;
import eu.nagar.nconnect.server.util.BinaryUtils;

import java.nio.ByteBuffer;

public class CodecInSetNick implements PacketCodec<PacketInSetNick> {
    @Override
    public PacketInSetNick decode(ByteBuffer buffer, Protocol protocol) {
        PacketInSetNick packet = new PacketInSetNick();
        buffer.get();
        packet.setName(BinaryUtils.decode(buffer));
        return packet;
    }

    @Override
    public ByteBuffer encode(PacketInSetNick payload, Protocol protocol) {
        ByteBuffer buffer = ByteBuffer.allocate(255);
        buffer.put((byte) 0);
        buffer.put(BinaryUtils.encode(payload.getName()));

        return buffer;
    }
}
