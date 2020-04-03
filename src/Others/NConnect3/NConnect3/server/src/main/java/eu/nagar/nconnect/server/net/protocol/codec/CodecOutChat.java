/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.codec;

import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;
import eu.nagar.nconnect.server.net.protocol.packet.PacketOutChat;
import eu.nagar.nconnect.server.util.BinaryUtils;

import java.awt.*;
import java.nio.ByteBuffer;

public class CodecOutChat implements PacketCodec<PacketOutChat> {
    @Override
    public PacketOutChat decode(ByteBuffer buffer, Protocol protocol) {
        PacketOutChat payload = new PacketOutChat();

        buffer.get();
        byte flags = buffer.get();
        int r = buffer.get() & 0xFF;
        int g = buffer.get() & 0xFF;
        int b = buffer.get() & 0xFF;

        payload.setColor(new Color(r, g, b));
        payload.setName(BinaryUtils.decode(buffer));
        payload.setMessage(BinaryUtils.decode(buffer));

        return payload;
    }

    @Override
    public ByteBuffer encode(PacketOutChat payload, Protocol protocol) {
        ByteBuffer buffer = ByteBuffer.allocate(255);

        buffer.put((byte) 0x63);
        buffer.put((byte) 0);
        buffer.put((byte) payload.getColor().getRed());
        buffer.put((byte) payload.getColor().getGreen());
        buffer.put((byte) payload.getColor().getBlue());

        buffer.put(BinaryUtils.encode(payload.getName())).put((byte) 0);
        buffer.put(BinaryUtils.encode(payload.getMessage())).put((byte) 0);

        return buffer;
    }
}
