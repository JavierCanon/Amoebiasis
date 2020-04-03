/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command.vanilla;

import eu.nagar.nconnect.api.command.Command;
import eu.nagar.nconnect.api.command.CommandExecutor;
import eu.nagar.nconnect.api.command.CommandManager;
import eu.nagar.nconnect.api.command.CommandSender;

public class HelpExecutor implements CommandExecutor {
    private CommandManager commandManager;

    public HelpExecutor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute(Command command, CommandSender sender, String[] args) {
        sender.sendMessage("Showing command help");
        for (Command cmd : commandManager.getCommands()) {
            sender.sendMessage("/" + cmd.getName() + " " + cmd.getDescription());
        }
    }
}
