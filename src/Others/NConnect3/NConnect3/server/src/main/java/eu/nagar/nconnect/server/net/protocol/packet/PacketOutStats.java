/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.packet;

import eu.nagar.nconnect.server.net.protocol.Packet;
import eu.nagar.nconnect.server.net.protocol.PacketReceiver;

public class PacketOutStats extends Packet {
    private String stats;

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getStats() {
        return stats;
    }

    @Override
    public void handle(PacketReceiver receiver) {
        receiver.handle(this);
    }
}
