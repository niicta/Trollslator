package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.model.LanguageMapper;
import niicta.trollslator.model.impl.StandardLanguageMapper;

/**
 * Created by niict on 08.06.2017.
 */

public class MappingChangedEvent implements Event {
    private static final String EVENT_TYPE = "MappingChangedEvent";

    private LanguageMapper changedMapper;

    public MappingChangedEvent(){}

    public MappingChangedEvent(LanguageMapper changedMapper) {
        this.changedMapper = changedMapper;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public LanguageMapper getEventArgs() {
        return changedMapper;
    }
}
