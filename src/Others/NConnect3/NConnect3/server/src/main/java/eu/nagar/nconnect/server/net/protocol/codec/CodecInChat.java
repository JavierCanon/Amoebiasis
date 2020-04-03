/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.codec;

import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;
import eu.nagar.nconnect.server.net.protocol.packet.PacketInChat;
import eu.nagar.nconnect.server.util.BinaryUtils;

import java.nio.ByteBuffer;

public class CodecInChat implements PacketCodec<PacketInChat> {
    @Override
    public PacketInChat decode(ByteBuffer buffer, Protocol protocol) {
        PacketInChat payload = new PacketInChat();

        buffer.get();
        buffer.get();
        payload.setMessage(BinaryUtils.decode(buffer));

        return payload;
    }

    @Override
    public ByteBuffer encode(PacketInChat payload, Protocol protocol) {
        ByteBuffer buffer = ByteBuffer.allocate(255);

        buffer.put((byte) 99);
        buffer.put((byte) 0);
        buffer.put(BinaryUtils.encode(payload.getMessage())).put((byte) 0);

        return buffer;
    }
}
