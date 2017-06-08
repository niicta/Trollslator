package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;
import niicta.trollslator.model.LanguageMapper;
import niicta.trollslator.model.impl.StandardLanguageMapper;

/**
 * Created by niict on 08.06.2017.
 */

public class MappingChangedEvent implements Event {

    private LanguageMapper changedMapper;

    public MappingChangedEvent(LanguageMapper changedMapper) {
        this.changedMapper = changedMapper;
    }

    @Override
    public int getType() {
        return EventTypes.MAPPING_CHANGED_EVENT_TYPE;
    }

    @Override
    public LanguageMapper getEventArgs() {
        return changedMapper;
    }
}
