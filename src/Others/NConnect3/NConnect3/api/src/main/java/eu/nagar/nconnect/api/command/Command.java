/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.command;

public class Command {
    private String name;
    private String permission;
    private String description;
    private String[] aliases;
    private CommandExecutor executor;

    public Command(String name, String permission, String description, String[] aliases, CommandExecutor executor) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.aliases = aliases;
        this.executor = executor;
    }

    /**
     * Get the command name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get (if specified) aliases for the command.
     * @return aliases
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Get the permission for this command.
     * @return permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Get the description for this command.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the command executor.
     * @return command executor.
     */
    public CommandExecutor getExecutor() {
        return executor;
    }
}
