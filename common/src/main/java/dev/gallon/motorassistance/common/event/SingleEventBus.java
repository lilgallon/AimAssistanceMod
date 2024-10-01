package dev.gallon.motorassistance.common.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleEventBus {
    public static SingleEventBus INSTANCE = new SingleEventBus();

    static Map<Class<? extends Event>, List<Callback<? extends Event>>> callbacksByEvent = new HashMap<>();

    static public <T extends Event> void listen(Class<T> event, Callback<T> callback) {
        if (!callbacksByEvent.containsKey(event)) {
            callbacksByEvent.put(event, new ArrayList<>());
        }
        callbacksByEvent.get(event).add(callback);
    }

    static public <T extends Event> void publish(T event) {
        List<Callback<? extends Event>> callbacks = callbacksByEvent.get(event.getClass());
        if (callbacks != null) {
            callbacks.forEach(listener -> listener.callback(event));
        }
    }
}

