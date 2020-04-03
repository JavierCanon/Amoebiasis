/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.gameserver;

import eu.nagar.nconnect.api.server.GameServer;
import eu.nagar.nconnect.api.server.ServerManager;

import java.util.HashSet;
import java.util.Set;

public class ServerManagerImpl implements ServerManager {
    private Set<GameServer> servers = new HashSet<>();

    @Override
    public void registerServer(GameServer server) {
        this.servers.add(server);
    }

    @Override
    public Set<GameServer> getServers() {
        return servers;
    }

    @Override
    public GameServer getServer(String name) {
        for (GameServer server : servers) {
            if(server.getName().equalsIgnoreCase(name)) {
                return server;
            }
        }

        return null;
    }
}
