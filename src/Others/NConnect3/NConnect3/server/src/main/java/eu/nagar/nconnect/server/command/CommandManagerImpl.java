/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.command;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.*;
import eu.nagar.nconnect.server.NConnectServer;

import java.awt.*;
import java.util.*;

public class CommandManagerImpl implements CommandManager {
    private NConnectServer server;
    private Multimap<CommandRegistrar, Command> registeredCommands = ArrayListMultimap.create();

    public CommandManagerImpl(NConnectServer server) {
        this.server = server;
    }

    @Override
    public void registerCommand(CommandRegistrar registrar, Command command) {
        registeredCommands.put(registrar, command);
    }

    @Override
    public Collection<Command> getCommands() {
        return registeredCommands.values();
    }

    @Override
    public Command getCommand(String name) {
        for (Command command : getCommands()) {
            if (command.getName().equals(name)) return command;
        }

        return null;
    }

    public CommandResult dispatchCommand(String commandString, CommandSender sender) {
        String[] commandParts = commandString.split(" ");
        Command command = getCommand(commandParts[0]);

        if (command == null) {
            return CommandResult.NOT_FOUND;
        }

        try {
            String[] args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
            if (!sender.hasPermission(command.getPermission())) {
                sender.sendMessage("You do not have permission to use this command.", Color.RED);
                return CommandResult.SUCCESS;
            }
            command.getExecutor().execute(command, sender, args);
        } catch (Exception error) {
            return CommandResult.ERROR;
        }

        return CommandResult.SUCCESS;
    }

    public ProxyServer getServer() {
        return server;
    }
}
