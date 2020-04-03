/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.net.protocol.packet;

import eu.nagar.nconnect.server.net.protocol.Packet;
import eu.nagar.nconnect.server.net.protocol.PacketReceiver;

public class PacketInMouse extends Packet {
    private double mouseX, mouseY;

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getMouseX() {
        return mouseX;
    }

    @Override
    public void handle(PacketReceiver receiver) {
        receiver.handle(this);
    }
}
