/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol;

import eu.nagar.nconnect.server.net.protocol.codec.*;
import eu.nagar.nconnect.server.net.protocol.packet.*;

import java.util.HashMap;
import java.util.Map;

public class PacketRegister {
    public static final PacketRegister SERVERBOUND = new PacketRegister();
    public static final PacketRegister CLIENTBOUND = new PacketRegister();

    static  {
        SERVERBOUND.register(99, PacketInChat.class, new CodecInChat());
        SERVERBOUND.register(0, PacketInSetNick.class, new CodecInSetNick());
        SERVERBOUND.register(16, PacketInMouse.class, new CodecInMouse());

        CLIENTBOUND.register(99, PacketOutChat.class, new CodecOutChat());
        CLIENTBOUND.register(254, PacketOutStats.class, new CodecOutStats());
    }

    private Map<Integer, PacketCodec> codecById = new HashMap<>();
    private Map<Class<?>, PacketCodec> codecByClass = new HashMap<>();

    public PacketCodec getCodec(int packetId) {
        return codecById.get(packetId);
    }

    public PacketCodec getCodec(Class<?> packetClass) {
        return codecByClass.get(packetClass);
    }

    public void register(int packetId, Class<?> packetClass, PacketCodec packetCodec) {
        codecById.put(packetId, packetCodec);
        codecByClass.put(packetClass, packetCodec);
    }
}
