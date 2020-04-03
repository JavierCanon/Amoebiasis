/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.event;

import eu.nagar.nconnect.api.event.EventHandler;
import eu.nagar.nconnect.api.event.EventListener;
import eu.nagar.nconnect.api.event.EventPriority;
import eu.nagar.nconnect.api.extension.Extension;

import java.lang.reflect.Method;

public class RegisteredEventHandler {
    private Extension extension;
    private EventPriority priority;
    private EventListener instance;
    private Method method;
    private Class<?> event;

    RegisteredEventHandler(Extension extension, EventHandler listener, EventListener instance, Method method, Class<?> event) {
        this.extension = extension;
        this.priority = listener.priority();
        this.instance = instance;
        this.method = method;
        this.event = event;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public EventListener getObject() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public Extension getExtension() {
        return extension;
    }

    public Class<?> getEvent() {
        return event;
    }
}