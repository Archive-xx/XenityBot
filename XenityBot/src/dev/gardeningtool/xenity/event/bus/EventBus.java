package dev.gardeningtool.xenity.event.bus;

import dev.gardeningtool.xenity.event.Event;
import dev.gardeningtool.xenity.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gardening_Tool
 *
 * Used for handling events. No need to add any async implementations
 * as it's already executed on the JDA MainWS-ReadThread
 */
public class EventBus {

    /**
     * The registered listeners
     */
    private List<Listener> listeners;

    public EventBus() {
        listeners = new ArrayList<>();
    }

    /**
     * Passing the event to the listeners
     * @param event The event being called
     */
    public void handleEvent(Event event) {
        listeners.forEach(listener -> listener.onEvent(event));
    }

    /**
     * Subscribing a listener to the event bus
     * @param listener
     * @param listener
     */
    public void subscribe(Listener listener) {
        listeners.add(listener);
    }
}
