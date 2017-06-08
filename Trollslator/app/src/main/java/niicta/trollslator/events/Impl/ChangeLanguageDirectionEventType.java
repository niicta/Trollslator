package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;

/**
 * Created by niict on 08.06.2017.
 */

public class ChangeLanguageDirectionEventType implements Event{
    private static final String EVENT_TYPE = "ChangeLanguageDirectionEventType";
    private String from;
    private String to;

    public ChangeLanguageDirectionEventType(){}

    public ChangeLanguageDirectionEventType(String from, String to){
        this.from = from;
        this.to = to;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public String[] getEventArgs() {
        return new String[]{from, to};
    }
}
