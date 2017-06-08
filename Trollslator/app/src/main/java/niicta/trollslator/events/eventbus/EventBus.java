package niicta.trollslator.events.eventbus;

import niicta.trollslator.events.Event;
import niicta.trollslator.listeners.Listener;

/**
 * Created by niict on 06.06.2017.
 */

public interface EventBus {
    void addListener(int eventType, Listener listener);
    void dispatchEvent(Event event);
}
