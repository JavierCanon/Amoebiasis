/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.server;

import java.util.Collection;

public interface ServerManager {
    /**
     * Register an upstream game server
     * @param server upstream server.
     */
    void registerServer(GameServer server);

    /**
     * Get all registered game servers.
     * @return collection of registered servers
     */
    Collection<GameServer> getServers();

    /**
     * Find a server by it's id.
     * @param name server name
     * @return game server.
     */
    GameServer getServer(String name);
}
