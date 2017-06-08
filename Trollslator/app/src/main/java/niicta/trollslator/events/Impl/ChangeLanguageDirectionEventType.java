package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;

/**
 * Created by niict on 08.06.2017.
 */

public class ChangeLanguageDirectionEventType implements Event{

    private String from;
    private String to;

    public ChangeLanguageDirectionEventType(String from, String to){
        this.from = from;
        this.to = to;
    }

    @Override
    public int getType() {
        return EventTypes.CHANGE_LANGUAGE_DIRECTION_EVENT_TYPE;
    }

    @Override
    public String[] getEventArgs() {
        return new String[]{from, to};
    }
}
