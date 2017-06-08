package niicta.trollslator.model.impl;

import android.support.annotation.NonNull;

import niicta.trollslator.model.HistoryKey;

/**
 * Created by niict on 06.06.2017.
 */

public class StandardHistoryKey implements HistoryKey {
    private String fromLang;
    private String toLang;
    private String text;
    private String translation;
    //ссылки на предыдущий и следующий (по времени) элементы
    private HistoryKey prev;
    private HistoryKey next;
    private boolean favorite;

    public StandardHistoryKey(String fromLang, String toLang, String text, String translation) {
        this.fromLang = fromLang;
        this.toLang = toLang;
        this.text = text;
        this.next = null;
        this.translation = translation;
        favorite = false;
    }

    @Override
    public boolean isFavorite() {
        return favorite;
    }

    //добавление элемента в избранное с установкой флагов изменения
    @Override
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public HistoryKey getPrev() {
        return prev;
    }

    @Override
    public void setPrev(HistoryKey prev) {
        this.prev = prev;
    }

    @Override
    public HistoryKey getNext() {
        return next;
    }

    @Override
    public void setNext(HistoryKey next) {
        this.next = next;
    }

    @Override
    public String getFromLang() {
        return fromLang;
    }

    @Override
    public String getToLang() {
        return toLang;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getTranslation() {
        return translation;
    }

    //описание логики сравнения элеемнтов для хранения и поиска их по дереву
    @Override
    public int compareTo(@NonNull HistoryKey o) {
        int result;
        if ((result = this.getText().compareToIgnoreCase(o.getText())) != 0)
            return result;
        else if ((result = this.getFromLang().compareToIgnoreCase(o.getFromLang())) != 0) {
            return result;
        } else if ((result = this.getToLang().compareToIgnoreCase(o.getToLang())) != 0) {
            return result;
        } else return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof StandardHistoryKey)) return false;
        return ((StandardHistoryKey) obj).compareTo(this) == 0;
    }


}
