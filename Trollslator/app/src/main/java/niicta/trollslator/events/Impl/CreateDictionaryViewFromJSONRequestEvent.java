package niicta.trollslator.events.Impl;

import org.json.JSONObject;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;

/**
 * Created by niict on 09.06.2017.
 */

public class CreateDictionaryViewFromJSONRequestEvent implements Event {
    private JSONObject jsonObject;

    public CreateDictionaryViewFromJSONRequestEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public int getType() {
        return EventTypes.CREATE_DICTIONARY_VIEW_FROM_JSON_EVENT_TYPE;
    }

    @Override
    public JSONObject getEventArgs() {
        return jsonObject;
    }
}
