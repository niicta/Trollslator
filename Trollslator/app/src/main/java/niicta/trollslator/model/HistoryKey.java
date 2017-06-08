package niicta.trollslator.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by niict on 06.06.2017.
 */

public interface HistoryKey extends Comparable<HistoryKey>, Serializable {
    HistoryKey getPrev();
    void setPrev(HistoryKey prev);
    HistoryKey getNext();
    void setNext(HistoryKey next);
    boolean isFavorite();
    void setFavorite(boolean favorite);
    String getFromLang();
    String getToLang();
    String getText();
    String getTranslation();
}
