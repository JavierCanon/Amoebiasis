/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.CommandManager;
import eu.nagar.nconnect.api.command.CommandResult;
import eu.nagar.nconnect.api.command.CommandSender;
import eu.nagar.nconnect.api.event.EventManager;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.extension.ExtensionManager;

import eu.nagar.nconnect.api.server.ServerManager;
import eu.nagar.nconnect.server.command.CommandManagerImpl;
import eu.nagar.nconnect.server.command.VanillaCommands;
import eu.nagar.nconnect.server.config.ConfigManager;
import eu.nagar.nconnect.server.event.EventManagerImpl;
import eu.nagar.nconnect.server.gameserver.ServerManagerImpl;
import eu.nagar.nconnect.server.net.WebSocketHandler;
import eu.nagar.nconnect.server.extension.ExtensionManagerImpl;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

public class NConnectServer implements ProxyServer, CommandSender {
    private static final String VERSION = "1.0.4";
    private static final Logger LOGGER = LoggerFactory.getLogger("NConnect");
    private boolean isRunning;

    private WebSocketHandler webSocketHandler;
    private ConfigManager configManager = new ConfigManager(this);
    private ServerManagerImpl serverManager = new ServerManagerImpl();
    private EventManagerImpl eventManager = new EventManagerImpl();
    private CommandManagerImpl commandManager = new CommandManagerImpl(this);
    private ExtensionManagerImpl extensionManager = new ExtensionManagerImpl(this);

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    public NConnectServer() {
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("(C) Nagar Group 2016 - 2019.");
        LOGGER.info("Starting NConnect 3 Advanced Packet Interceptor");
        LOGGER.info("Version: " + VERSION);
        LOGGER.info("--------------------------------------------------");

        new VanillaCommands().register(this);

        configManager.load();
        webSocketHandler = new WebSocketHandler(this);
        webSocketHandler.start();
        extensionManager.loadExtensions();
        extensionManager.enableExtensions();

        isRunning = true;
        LineReader lineReader = LineReaderBuilder.builder()
                .build();

        while (isRunning) {
            String input = lineReader.readLine(">");

            if (commandManager.dispatchCommand(input, this) == CommandResult.NOT_FOUND) {
                LOGGER.info("Invalid command. Please use 'help'.");
            }
        }
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public ExtensionManager getExtensionManager() {
        return extensionManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public Collection<Player> getConnectedPlayers() {
        return new HashSet<>(webSocketHandler.getConnectedPlayers());
    }

    @Override
    public void stop() {
        isRunning = false;
        try {
            webSocketHandler.stop(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        extensionManager.disableExtensions();
        System.exit(1);
    }

    @Override
    public void sendMessage(String message) {
        LOGGER.info(message);
    }

    @Override
    public void sendMessage(String message, Color color) {
        LOGGER.info(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
