/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command.vanilla;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.Command;
import eu.nagar.nconnect.api.command.CommandExecutor;
import eu.nagar.nconnect.api.command.CommandSender;
import eu.nagar.nconnect.api.extension.Extension;

import java.util.Collection;

public class ExtensionsExecutor implements CommandExecutor {
    private ProxyServer server;

    public ExtensionsExecutor(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        Collection<Extension> extensions = server.getExtensionManager().getExtensions();
        sender.sendMessage("Extensions (" + extensions.size() + "):");
        for (Extension extension : extensions) {
            sender.sendMessage(extension.getDescription().name() + " v" + extension.getDescription().version());
        }
    }
}
