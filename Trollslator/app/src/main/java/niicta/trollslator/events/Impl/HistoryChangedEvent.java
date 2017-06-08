package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;
import niicta.trollslator.model.History;

/**
 * Created by niict on 06.06.2017.
 */

public class HistoryChangedEvent implements Event {

    private History history;

    public HistoryChangedEvent(History history) {
        this.history = history;
    }

    @Override
    public int getType() {
        return EventTypes.HISTORY_CHANGED_EVENT_TYPE;
    }

    @Override
    public History getEventArgs() {
        return history;
    }
}
