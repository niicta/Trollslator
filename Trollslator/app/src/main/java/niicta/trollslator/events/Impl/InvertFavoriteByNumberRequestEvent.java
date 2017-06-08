package niicta.trollslator.events.Impl;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;

/**
 * Created by niict on 07.06.2017.
 */

public class InvertFavoriteByNumberRequestEvent implements Event {

    private int number;

    public InvertFavoriteByNumberRequestEvent(int number){
        this.number = number;
    }

    @Override
    public int getType() {
        return EventTypes.INVERT_FAVORITE_BY_NUMBER_REQUEST_EVENT_TYPE;
    }

    @Override
    public Integer getEventArgs() {
        return number;
    }
}
