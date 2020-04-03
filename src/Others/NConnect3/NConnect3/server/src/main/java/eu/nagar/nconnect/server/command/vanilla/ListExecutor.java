/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command.vanilla;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.Command;
import eu.nagar.nconnect.api.command.CommandExecutor;
import eu.nagar.nconnect.api.command.CommandSender;
import eu.nagar.nconnect.api.player.Player;

public class ListExecutor implements CommandExecutor {
    private ProxyServer server;

    public ListExecutor(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        for (Player player : server.getConnectedPlayers()) {
            String info = player.getName() +
                    " " +
                    player.getAddress() +
                    " " +
                    player.getGameServer().getName();
            sender.sendMessage(info);
        }
    }

}
