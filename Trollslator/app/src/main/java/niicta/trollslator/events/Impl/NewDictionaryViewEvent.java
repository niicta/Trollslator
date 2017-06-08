package niicta.trollslator.events.Impl;

import android.view.View;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;

/**
 * Created by niict on 09.06.2017.
 */

public class NewDictionaryViewEvent implements Event {

    private View view;

    public NewDictionaryViewEvent(View view) {
        this.view = view;
    }

    @Override
    public int getType() {
        return EventTypes.NEW_DICTIONARY_VIEW_EVENT;
    }

    @Override
    public Object getEventArgs() {
        return view;
    }
}
