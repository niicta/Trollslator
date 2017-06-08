package niicta.trollslator.model.factories.impl;

import java.io.InputStream;

import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.model.History;
import niicta.trollslator.model.HistoryKey;
import niicta.trollslator.model.LanguageMapper;
import niicta.trollslator.model.factories.ModelFactory;
import niicta.trollslator.model.impl.StandardHistory;
import niicta.trollslator.model.impl.StandardHistoryKey;
import niicta.trollslator.model.impl.StandardLanguageMapper;

/**
 * Created by niict on 06.06.2017.
 */

public class StandardModelFactory implements ModelFactory {

    @Override
    public History createHistory(InputStream is, EventBus eventBus) {
        return new StandardHistory(this, eventBus, is);
    }

    @Override
    public HistoryKey createHistoryKey(String fromLang, String toLang, String text, String translation) {
        return new StandardHistoryKey(fromLang, toLang, text, translation);
    }

    @Override
    public LanguageMapper createLanguageMapper(InputStream is, EventBus eventBus) {
        return new StandardLanguageMapper(is, eventBus);
    }
}
