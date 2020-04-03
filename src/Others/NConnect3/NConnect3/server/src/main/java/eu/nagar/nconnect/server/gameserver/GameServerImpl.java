/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.gameserver;

import eu.nagar.nconnect.api.server.GameServer;

import java.util.List;

public class GameServerImpl implements GameServer {
    private String name, address;
    private List<String> motd;

    public GameServerImpl(String name, String address, List<String> motd) {
        this.name = name;
        this.address = address;
        this.motd = motd;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public List<String> getMotd() {
        return motd;
    }
}
