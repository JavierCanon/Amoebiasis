/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api;

import eu.nagar.nconnect.api.command.CommandManager;
import eu.nagar.nconnect.api.event.EventManager;
import eu.nagar.nconnect.api.player.Player;
import eu.nagar.nconnect.api.extension.ExtensionManager;
import eu.nagar.nconnect.api.server.ServerManager;
import org.slf4j.Logger;

import java.util.Collection;

public interface ProxyServer {
    /**
     * Get the server version
     * @return version
     */
    String getVersion();

    /**
     * Get the server logger
     * @return logger
     */
    Logger getLogger();

    /**
     * Get the server command manager.
     * @return command manager
     */
    CommandManager getCommandManager();

    /**
     * Get the server plugin manager
     * @return plugin manager
     */
    ExtensionManager getExtensionManager();

    /**
     * Get the event manager
     * @return event manager
     */
    EventManager getEventManager();

    /**
     * Get the game server manager
     * @return server manager
     */
    ServerManager getServerManager();

    /**
     * Get connected players
     * @return players
     */
    Collection<Player> getConnectedPlayers();

    /**
     * Stop the server
     */
    void stop();
}
