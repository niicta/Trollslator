package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;
import niicta.trollslator.model.LanguageSwitcher;

/**
 * Created by niict on 08.06.2017.
 */

public class LanguageSwitcherChangedEvent implements Event {

    private LanguageSwitcher switcher;

    public LanguageSwitcherChangedEvent(LanguageSwitcher switcher){
        this.switcher = switcher;
    }

    @Override
    public int getType() {
        return EventTypes.LANGUAGE_SWITCHER_CHANGED_EVENT_TYPE;
    }

    @Override
    public LanguageSwitcher getEventArgs() {
        return switcher;
    }
}
