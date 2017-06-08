package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.model.LanguageSwitcher;

/**
 * Created by niict on 08.06.2017.
 */

public class LanguageSwitcherChangedEvent implements Event {
    private static final String EVENT_TYPE = "LanguageSwitcherChangedEvent";

    private LanguageSwitcher switcher;

    public LanguageSwitcherChangedEvent(){}

    public LanguageSwitcherChangedEvent(LanguageSwitcher switcher){
        this.switcher = switcher;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public LanguageSwitcher getEventArgs() {
        return switcher;
    }
}
