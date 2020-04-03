/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.extension;

import eu.nagar.nconnect.api.ProxyServer;
import eu.nagar.nconnect.api.command.CommandRegistrar;
import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;

import java.io.File;

public abstract class Extension implements CommandRegistrar {
    private ProxyServer server;
    private Logger logger;
    private ExtensionDescription description;
    private YamlFile yamlFile;
    private File dataFolder;

    protected Extension() {

    }

    public void init(ProxyServer server, Logger logger, ExtensionDescription description, YamlFile yamlFile, File dataFolder) {
        this.server = server;
        this.logger = logger;
        this.description = description;
        this.yamlFile = yamlFile;
        this.dataFolder = dataFolder;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public ProxyServer getServer() {
        return server;
    }

    /**
     * Get the plugin logger
     * @return logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Get the plugin description.
     * @return description
     */
    public ExtensionDescription getDescription() {
        return description;
    }

    /**
     * Get the plugin config file.
     * This file will need to be created by the plugin using YamlFile#createNewFile()
     * @return yaml file.
     */
    public YamlFile getConfig() {
        return yamlFile;
    }

    /**
     * Get the allocated plugin data folder.
     * @return plugin folder
     */
    public File getFolder() {
        return dataFolder;
    }
}
