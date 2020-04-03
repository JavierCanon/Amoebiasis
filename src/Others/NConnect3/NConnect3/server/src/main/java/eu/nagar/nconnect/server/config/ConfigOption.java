/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.config;

public enum ConfigOption {
    SERVER_PORT("nconnect.port", 1000),
    SERVER_MAX_CONNECTIONS("nconnect.connection-limit", 200),
    SERVER_MAX_CONNECTIONS_PER_IP("nconnect.connection-ip-limit", 20),
    SERVER_CONNECTION_THROTTLE("nconnect.connection-throttle", 1000);

    private String path;
    private Object value;
    private Object defaultValue;

    ConfigOption(String path, Object value) {
        this.path = path;
        this.defaultValue = value;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    public int getValueInt() {
        return (int) value;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
