/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.api.extension;

import java.util.Collection;

public interface ExtensionManager {
    /**
     * Get specified plugin by it's name
     * @param name plugin name.
     * @return plugin instance.
     */
    Extension getExtension(String name);

    /**
     * Get all loaded plugins
     * @return collection of loaded plugins
     */
    Collection<Extension> getExtensions();

    /**
     * Load plugins.
     */
    void loadExtensions();

    /**
     * Enable all loaded plugins.
     */
    void enableExtensions();

    /**
     * Disable all loaded plugins.
     */
    void disableExtensions();
}
