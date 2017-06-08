package niicta.trollslator.view.creators.factories.impl;

import android.content.Context;

import org.json.JSONObject;

import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.view.creators.ViewCreator;
import niicta.trollslator.view.creators.factories.ViewCreatorFactory;
import niicta.trollslator.view.creators.impl.StandardJSONDictionaryViewCreator;

/**
 * Created by niict on 09.06.2017.
 */

public class StandardViewCreatorFactory implements ViewCreatorFactory {
    @Override
    public ViewCreator<JSONObject> createFromJSONDictionaryViewCreator(EventBus eventBus, Context context) {
        return new StandardJSONDictionaryViewCreator(eventBus, context);
    }
}
