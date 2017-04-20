package niicta.trollslator.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by niict on 05.04.2017.
 */

//сущность, хранящая ключи и ui-отображения языков
public class LangContainer {
    private static LangContainer instance = new LangContainer();
    public static LangContainer getInstance(){
        return instance;
    }
    //именно тут хранятся пары вида ключ-ui_значение в отношении 1 к 1
    private ArrayList<Pair> langs;

    private LangContainer(){
        langs = new ArrayList<>();


    }


    private class Pair implements Comparable<Pair> {
        String keyLang;
        String uiLang;

        Pair(String fromLang, String toLang) {
            this.keyLang = fromLang;
            this.uiLang = toLang;
        }

        String getKeyLang() {
            return keyLang;
        }

        String getUiLang() {
            return uiLang;
        }

        @Override
        public int compareTo(@NonNull Pair o) {
            int result;
            if ((result = this.getKeyLang().compareToIgnoreCase(o.getKeyLang())) != 0) {
                return result;
            } else if ((result = this.getUiLang().compareToIgnoreCase(o.getUiLang())) != 0) {
                return result;
            }
            return 0;
        }
    }

    public void setMapping(String uiLang, String lang){
        Pair p = new Pair(lang, uiLang);
        langs.add(p);

    }

    //методы получение соответствующих значений

    public String getLang(String uiLang){
        for (int i = 0; i < langs.size(); i++){
            if (langs.get(i).getUiLang().equals(uiLang))
                return langs.get(i).getKeyLang();
        }
        return null;
    }

    public String getUiLang(String Lang){
        for (int i = 0; i < langs.size(); i++){
            if (langs.get(i).getKeyLang().equals(Lang))
                return langs.get(i).getUiLang();
        }
        return null;
    }


}
