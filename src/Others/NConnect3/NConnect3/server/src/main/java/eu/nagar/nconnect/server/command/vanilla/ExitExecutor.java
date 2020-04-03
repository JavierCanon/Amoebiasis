/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command.vanilla;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.Command;
import eu.nagar.nconnect.api.command.CommandExecutor;
import eu.nagar.nconnect.api.command.CommandSender;

public class ExitExecutor implements CommandExecutor {
    private ProxyServer server;

    public ExitExecutor(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        server.stop();
    }

}
