/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command.vanilla;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.Command;
import eu.nagar.nconnect.api.command.CommandExecutor;
import eu.nagar.nconnect.api.command.CommandSender;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.server.GameServer;

public class ServerExecutor implements CommandExecutor {
    private ProxyServer server;

    public ServerExecutor(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command");
            return;
        }

        if (args.length == 0) {
            listServers(sender);
            return;
        }

        String serverName = args[0];
        GameServer gameServer = server.getServerManager().getServer(serverName);

        if (server == null) {
            listServers(sender);
            return;
        }

        sender.sendMessage("Connecting to " + gameServer.getName());
        Player player = (Player) sender;
        player.connectTo(gameServer);
    }

    private void listServers(CommandSender sender) {
        sender.sendMessage("Available servers: ");

        for (GameServer gserver : server.getServerManager().getServers()) {
            sender.sendMessage(gserver.getName());
        }
    }
}
