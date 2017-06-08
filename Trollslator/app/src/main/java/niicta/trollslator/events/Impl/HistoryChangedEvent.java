package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.model.History;

/**
 * Created by niict on 06.06.2017.
 */

public class HistoryChangedEvent implements Event {
    private static final String EVENT_TYPE = "HistoryChangedEvent";

    private History history;

    public HistoryChangedEvent(History history) {
        this.history = history;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public History getEventArgs() {
        return history;
    }
}
