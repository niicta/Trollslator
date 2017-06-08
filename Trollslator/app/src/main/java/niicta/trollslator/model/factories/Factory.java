package niicta.trollslator.model.factories;

import java.io.InputStream;
import java.io.OutputStream;

import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.model.History;
import niicta.trollslator.model.HistoryKey;
import niicta.trollslator.model.LanguageMapper;

/**
 * Created by niict on 06.06.2017.
 */

public interface Factory {
    History createHistory(InputStream is, EventBus eventBus);
    HistoryKey createHistoryKey(String fromLang, String toLang, String text, String translation);
    LanguageMapper createLanguageMapper(InputStream is, EventBus eventBus);
}
