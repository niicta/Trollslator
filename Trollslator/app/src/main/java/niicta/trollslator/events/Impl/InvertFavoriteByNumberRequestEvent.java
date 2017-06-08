package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;

/**
 * Created by niict on 07.06.2017.
 */

public class InvertFavoriteByNumberRequestEvent implements Event {
    private static final String EVENT_TYPE = "InvertFavoriteByNumberRequestEvent";

    private int number;

    public InvertFavoriteByNumberRequestEvent(){}

    public InvertFavoriteByNumberRequestEvent(int number){
        this.number = number;
    }

    @Override
    public String getType() {
        return EVENT_TYPE;
    }

    @Override
    public Integer getEventArgs() {
        return number;
    }
}
