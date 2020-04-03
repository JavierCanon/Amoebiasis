/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.command;

public interface CommandExecutor {
    /**
     * Called when command has been executed
     * @param command command executed
     * @param sender command sender
     * @param args provided command args.
     */
    void execute(Command command, CommandSender sender, String[] args);
}
