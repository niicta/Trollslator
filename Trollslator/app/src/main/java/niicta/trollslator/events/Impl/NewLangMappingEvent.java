package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;

/**
 * Created by niict on 08.06.2017.
 */

public class NewLangMappingEvent implements Event {

    private String[] keys;
    private String[] uiLangs;

    public NewLangMappingEvent(String[] keys, String[] uiLangs){
        this.keys = keys;
        this.uiLangs = uiLangs;
    }

    @Override
    public int getType() {
        return EventTypes.NEW_LANG_MAPPING_EVENT_TYPE;
    }

    @Override
    public String[][] getEventArgs() {
        return new String[][]{keys, uiLangs};
    }
}
