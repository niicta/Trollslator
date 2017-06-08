package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;

/**
 * Created by niict on 08.06.2017.
 */

public class NewLangMappingEvent implements Event {
    private static final String EVENT_TYPE = "NewLangMappingEvent";

    private String[] keys;
    private String[] uiLangs;

    public NewLangMappingEvent(){}

    public NewLangMappingEvent(String[] keys, String[] uiLangs){
        this.keys = keys;
        this.uiLangs = uiLangs;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public String[][] getEventArgs() {
        return new String[][]{keys, uiLangs};
    }
}
