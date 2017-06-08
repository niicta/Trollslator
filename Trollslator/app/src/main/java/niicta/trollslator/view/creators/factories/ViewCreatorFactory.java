package niicta.trollslator.view.creators.factories;

import android.content.Context;

import org.json.JSONObject;

import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.view.creators.ViewCreator;

/**
 * Created by niict on 08.06.2017.
 */

public interface ViewCreatorFactory {
    ViewCreator<JSONObject> createFromJSONDictionaryViewCreator(EventBus eventBus, Context context);
}
