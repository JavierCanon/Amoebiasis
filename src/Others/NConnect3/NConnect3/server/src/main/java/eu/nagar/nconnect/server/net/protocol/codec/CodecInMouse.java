/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.codec;

import eu.nagar.nconnect.api.net.protocol.Protocol;
import eu.nagar.nconnect.server.net.protocol.packet.PacketInMouse;
import eu.nagar.nconnect.server.net.protocol.PacketCodec;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CodecInMouse implements PacketCodec<PacketInMouse> {

    @Override
    public PacketInMouse decode(ByteBuffer buffer, Protocol protocol) {
        buffer.get();

        double mouseX = buffer.getInt();
        double mouseY = buffer.getInt();

        PacketInMouse payload = new PacketInMouse();

        payload.setMouseX(mouseX);
        payload.setMouseY(mouseY);

        return payload;
    }

    @Override
    public ByteBuffer encode(PacketInMouse payload, Protocol protocol) {
        ByteBuffer buffer = ByteBuffer.allocate(13);

        buffer.put((byte) 16);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt((int) payload.getMouseX());
        buffer.putInt((int) payload.getMouseY());

        return buffer;
    }
}
