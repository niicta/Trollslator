package niicta.trollslator.events.eventbus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.listeners.Listener;

/**
 * Created by niict on 06.06.2017.
 */

public class StandardEventBus implements EventBus {
    private Map<String, List<Listener>> eventMap = new HashMap<>();

    @Override
    public void addListener(String eventType, Listener listener) {
        List<Listener> listeners = eventMap.get(eventType);
        if (listeners == null){
            listeners = new ArrayList<>();
            eventMap.put(eventType, listeners);
        }
        listeners.add(listener);
    }

    @Override
    public void dispatchEvent(Event event) {
        DispatcherProcess dispatcherProcess = new DispatcherProcess(event);
        dispatcherProcess.run();
    }

    private class DispatcherProcess implements Runnable{
        private Event event;

        public DispatcherProcess(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            List<Listener> listeners = eventMap.get(event.getType());
            if (listeners != null)
                for (Listener listener : listeners)
                    listener.onEvent(event);
        }
    }
}
