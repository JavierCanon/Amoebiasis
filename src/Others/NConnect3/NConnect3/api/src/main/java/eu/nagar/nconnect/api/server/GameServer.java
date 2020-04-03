/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.server;

import java.util.List;

public interface GameServer {
    /**
     * Server name
     * @return name
     */
    String getName();

    /**
     * Server websocket address
     * @return address
     */
    String getAddress();

    /**
     * Server message of the day/welcome
     * @return motd
     */
    List<String> getMotd();
}
