package niicta.trollslator.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import niicta.trollslator.events.eventbus.EventBus;

/**
 * Created by niict on 06.06.2017.
 */

public interface History extends SerializableModelObject {
    List<HistoryKey> getFavoriteList();
    void addLast(String fromLang, String toLang, String fromText, String translation);
    boolean isInitialized();
    HistoryKey getLast();
    TreeMap<HistoryKey, HistoryKey> getHistory();
    void invertFavorite(int number);
    boolean verifyFavorite(int number);
    HistoryKey exctract(String text, String fromLang, String toLang);
    HistoryKey find(int number);
    List<HistoryKey> find(String mask);
    void clear();
}
