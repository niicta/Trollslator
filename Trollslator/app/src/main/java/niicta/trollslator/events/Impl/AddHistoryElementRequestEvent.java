package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;

/**
 * Created by niict on 07.06.2017.
 */

public class AddHistoryElementRequestEvent implements Event {
    private static final String EVENT_TYPE = "AddHistoryElementRequestEvent";

    private String fromLang;
    private String toLang;
    private String text;
    private String translation;

    public AddHistoryElementRequestEvent(){
    }

    public AddHistoryElementRequestEvent(String fromLang, String toLang, String text, String translation){
        this.fromLang = fromLang;
        this.toLang = toLang;
        this.text = text;
        this.translation = translation;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public String[] getEventArgs() {
        return new String[]{fromLang, toLang, text, translation};
    }
}
