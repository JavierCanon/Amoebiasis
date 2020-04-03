/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.config;

import eu.nagar.nconnect.api.server.GameServer;
import eu.nagar.nconnect.server.gameserver.GameServerImpl;
import eu.nagar.nconnect.server.NConnectServer;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigManager {
    private NConnectServer server;
    private YamlFile config;

    public ConfigManager(NConnectServer server) {
        this.server = server;
    }

    public void load() {
        config = new YamlFile("config.yml");
        try {
            if(!config.exists()) {
                server.getLogger().info("Creating new configuration file.");
                config.createNewFile(true);
                InputStreamReader inputStream = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("config.yml"));
                config.createNewFile(true);
                config.options().copyDefaults(true);
                config.setDefaults(YamlConfiguration.loadConfiguration(inputStream));
                config.saveWithComments();
            }

            config.load();
            server.getLogger().info("Loading NConnect Configuration File.");
            loadSettings();
            server.getLogger().info("Loading upstream servers from configuration.");
            loadServers();
        } catch (Exception ex) {
            server.getLogger().error("Failed to load configuration file.");
            ex.printStackTrace();
        }
    }

    private void loadSettings() {
        boolean needsSave = false;
        for (ConfigOption option : ConfigOption.values()) {
            if (!config.isSet(option.getPath())) {
                needsSave = true;
                config.set(option.getPath(), option.getDefaultValue());
            }
            option.setValue(config.get(option.getPath()));
            server.getLogger().info("Setting " + option.name() + ": " + option.getValue());
        }

        if (needsSave) {
            try {
                config.save();
                server.getLogger().info("Saving updated configuration file.");
            } catch (IOException e) {
                server.getLogger().error("Failed to save configuration file.");
                e.printStackTrace();
            }
        }
    }

    private void loadServers() {
        ConfigurationSection serversSection = config.getConfigurationSection("servers");
        for (String serverName : serversSection.getKeys(false))  {
            ConfigurationSection serverSection =  serversSection.getConfigurationSection(serverName);
            String address = serverSection.getString("address");
            List<String> motd = serverSection.getStringList("motd");

            GameServer server = new GameServerImpl(serverName, address, motd);
            this.server.getServerManager().registerServer(server);
            this.server.getLogger().info("Loaded game server '" + serverName + "'.");
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
