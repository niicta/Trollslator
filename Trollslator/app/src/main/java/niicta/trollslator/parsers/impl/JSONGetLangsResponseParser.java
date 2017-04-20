package niicta.trollslator.parsers.impl;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by niict on 06.04.2017.
 */

//данный парсер на основе JSON объекта строит адаптеры для языкового блока
public class JSONGetLangsResponseParser {

    public int parseResponse(String response, ArrayList<String> keyStrings, ArrayList<String> uiStrings) {
        JSONObject langs = null;
        JSONObject baseObjest = null;
        try {
            baseObjest = new JSONObject(response);
            langs = baseObjest.getJSONObject("langs");
            Iterator keys = langs.keys();
            while (keys.hasNext()){
                String key = (String)keys.next();
                String uiLang = langs.getString(key);
                keyStrings.add(key);
                uiStrings.add(uiLang);
            }
            return 1;
        } catch (JSONException e) {
            return -1;
        }
    }
}
