/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command.vanilla;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.Command;
import eu.nagar.nconnect.api.command.CommandExecutor;
import eu.nagar.nconnect.api.command.CommandSender;
import eu.nagar.nconnect.api.player.Player;

import java.awt.*;
import java.util.Arrays;

public class BroadcastExecutor implements CommandExecutor {
    private ProxyServer server;

    public BroadcastExecutor(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        if (!sender.hasPermission("nconnect.broadcast")) {
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        for (Player player : server.getConnectedPlayers()) {
            player.sendMessage(message, Color.RED);
        }
    }
}
