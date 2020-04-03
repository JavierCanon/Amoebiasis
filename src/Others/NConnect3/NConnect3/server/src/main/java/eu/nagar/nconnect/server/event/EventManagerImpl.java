/*
 * Copyright (c) 2016 - 2019  Nagar Group
 */

package eu.nagar.nconnect.server.event;

import eu.nagar.nconnect.api.event.Event;
import eu.nagar.nconnect.api.event.EventHandler;
import eu.nagar.nconnect.api.event.EventListener;
import eu.nagar.nconnect.api.event.EventManager;
import eu.nagar.nconnect.api.extension.Extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManagerImpl implements EventManager {
    private List<RegisteredEventHandler> eventHandlers = new ArrayList<>();

    @Override
    public void callEvent(Event event) {
        for (RegisteredEventHandler eventMethod : eventHandlers) {
            if (!eventMethod.getEvent().getSimpleName().equals(event.getClass().getSimpleName())) continue;

            try {
                eventMethod.getMethod().invoke(eventMethod.getObject(), event);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void registerEvents(Extension extension, EventListener listener) {
        Method[] listenerMethods = listener.getClass().getDeclaredMethods();
        for (Method possibleEventHandler : listenerMethods) {
            if (!possibleEventHandler.isAnnotationPresent(EventHandler.class)) {
                continue;
            }

            Class<?>[] methodArgs = possibleEventHandler.getParameterTypes();
            if (methodArgs.length == 0) {
                continue;
            }

            Class<?> eventTypeArg = methodArgs[0];
            EventHandler eventHandler = possibleEventHandler.getAnnotation(EventHandler.class);
            RegisteredEventHandler registeredEventHandler = new RegisteredEventHandler(
                    extension,
                    eventHandler,
                    listener,
                    possibleEventHandler,
                    eventTypeArg
            );

            eventHandlers.add(registeredEventHandler);
        }
        sortEventHandlers();
    }

    private void sortEventHandlers() {
        eventHandlers.sort(Comparator.comparing(RegisteredEventHandler::getPriority));
    }

    @Override
    public void unregisterEvents(Extension extension) {
        eventHandlers.removeIf(eventHandler -> eventHandler.getExtension() == extension);
    }

    @Override
    public void unregisterEvents() {
        eventHandlers.clear();
    }
}