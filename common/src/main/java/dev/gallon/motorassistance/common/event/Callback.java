package dev.gallon.motorassistance.common.event;

public interface Callback<T extends Event> {
    void callback(Event event);
}
