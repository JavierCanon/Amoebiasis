/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.command;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder {
    private String name, permission, description;
    private List<String> aliases = new ArrayList<>();
    private CommandExecutor executor;

    public CommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public CommandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public CommandBuilder addAlias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public CommandBuilder setExecutor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public Command build() {
        return new Command(
                name,
                permission,
                description,
                aliases.toArray(new String[0]),
                executor
        );
    }
}
