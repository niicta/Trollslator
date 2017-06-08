package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;

/**
 * Created by niict on 07.06.2017.
 */

public class AddHistoryElementRequestEvent implements Event {

    private String fromLang;
    private String toLang;
    private String text;
    private String translation;

    public AddHistoryElementRequestEvent(String fromLang, String toLang, String text, String translation){
        this.fromLang = fromLang;
        this.toLang = toLang;
        this.text = text;
        this.translation = translation;
    }

    @Override
    public int getType() {
        return EventTypes.ADD_HISTORY_ELEMENT_REQUEST_EVENT_TYPE;
    }

    @Override
    public String[] getEventArgs() {
        return new String[]{fromLang, toLang, text, translation};
    }
}
