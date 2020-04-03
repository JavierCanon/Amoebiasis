/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.extension;

import eu.nagar.nconnect.api.extension.Extension;
import eu.nagar.nconnect.api.extension.ExtensionDescription;
import eu.nagar.nconnect.api.extension.ExtensionManager;
import eu.nagar.nconnect.server.NConnectServer;
import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ExtensionManagerImpl implements ExtensionManager {
    private NConnectServer server;
    private File pluginDirectory;
    private static final Logger LOGGER = LoggerFactory.getLogger("Extension Manager");
    private Map<String, Extension> pluginsByName = new HashMap<>();

    public ExtensionManagerImpl(NConnectServer server) {
        this.server = server;
        this.pluginDirectory = new File(Paths.get("").toAbsolutePath().toString(), "extensions");
    }

    @Override
    public Extension getExtension(String name) {
        return pluginsByName.get(name);
    }

    @Override
    public Collection<Extension> getExtensions() {
        return pluginsByName.values();
    }

    @Override
    public void loadExtensions() {
        if (!pluginDirectory.exists()) {
            if(!pluginDirectory.mkdirs()) {
                LOGGER.error("Failed to create extensions directory.");
            }
            return;
        }

        File[] jarFiles = pluginDirectory.listFiles();
        if (jarFiles == null || jarFiles.length == 0) {
            LOGGER.info("No extensions to load.");
            return;
        }

        for (File jarFile : jarFiles) {
            if (!jarFile.getName().endsWith(".jar")) {
                continue;
            }
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
                JarInputStream inputStream = new JarInputStream(new FileInputStream(jarFile));
                while (true) {
                    JarEntry jarEntry = inputStream.getNextJarEntry();
                    if (jarEntry == null) {
                        break;
                    }
                    if (jarEntry.getName() == null || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = jarEntry.getName();
                    className = className
                            .replace("/", ".")
                            .substring(0, className.lastIndexOf(".class"));
                    Class<?> clazz = classLoader.loadClass(className);

                    if (Extension.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(ExtensionDescription.class)) {
                        this.loadExtension((Extension) clazz.newInstance());
                    }
                }
            } catch (FileNotFoundException ignored) {

            } catch (IOException | ClassNotFoundException | NoClassDefFoundError | IllegalAccessException | InstantiationException e) {
                LOGGER.info("Error occurred whilst loading extension " + jarFile.getName());
                e.printStackTrace();
            }
        }
    }

    private void loadExtension(Extension extension) {
        ExtensionDescription extensionDescription = extension.getClass().getAnnotation(ExtensionDescription.class);
        Logger logger = LoggerFactory.getLogger(extensionDescription.name());
        File pluginFolder = new File(pluginDirectory, extensionDescription.name());
        YamlFile yamlFile = new YamlFile(new File(pluginFolder, "config.yml"));

        extension.init(server, logger, extensionDescription, yamlFile, pluginFolder);
        pluginsByName.put(extensionDescription.name(), extension);
    }

    @Override
    public void enableExtensions() {
        for (Extension extension : pluginsByName.values()) {
            LOGGER.info("Enabling " + extension.getDescription().name() + " " + extension.getDescription().version() + ".");
            try {
                extension.onEnable();
            } catch (Exception error) {
                LOGGER.error("Error occurred whilst enabling " + extension.getDescription().name());
                error.printStackTrace();
            }
        }
    }

    @Override
    public void disableExtensions() {
        for (Extension extension : pluginsByName.values()) {
            LOGGER.info("Disabling " + extension.getDescription().name() + " " + extension.getDescription().version() + ".");
            try {
                extension.onDisable();
                server.getEventManager().unregisterEvents(extension);
            } catch (Exception error) {
                LOGGER.error("Error occurred whilst disabling " + extension.getDescription().name());
            }
        }
    }
}
