/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.net.protocol;

public enum Protocol {
    LEGACY_5(5),
    LEGACY_6(6);

    private int protocolId;

    Protocol(int protocolId) {
        this.protocolId = protocolId;
    }

    public int getId() {
        return protocolId;
    }
}
