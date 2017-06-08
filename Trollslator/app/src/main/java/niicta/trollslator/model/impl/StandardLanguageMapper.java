package niicta.trollslator.model.impl;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;
import niicta.trollslator.events.Impl.MappingChangedEvent;
import niicta.trollslator.events.Impl.NewLangMappingEvent;
import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.listeners.Listener;
import niicta.trollslator.model.LanguageMapper;

/**
 * Created by niict on 05.04.2017.
 */

//сущность, хранящая ключи и ui-отображения языков
public class StandardLanguageMapper implements LanguageMapper{

    //именно тут хранятся пары вида ключ-ui_значение в отношении 1 к 1
    //Todo можно заменить на хэшмапу
    private ArrayList<Pair> langs;
    private EventBus eventBus;

    private StandardLanguageMapper(){}

    public StandardLanguageMapper(EventBus eventBus){
        this(null, eventBus);
    }

    public StandardLanguageMapper(InputStream is, EventBus eventBus){
        this.langs = new ArrayList<>();
        this.eventBus = eventBus;
        deserialize(is);
        initListeners(this.eventBus);
        notifyAboutChanges();
    }

    private class Pair implements Comparable<Pair>, Serializable{
        private String keyLang;
        private String uiLang;

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

        public void setKeyLang(String keyLang) {
            this.keyLang = keyLang;
        }

        public void setUiLang(String uiLang) {
            this.uiLang = uiLang;
        }

        @Override
        public int compareTo(@NonNull Pair o) {
            int result;
//            if ((result = this.getKeyLang().compareToIgnoreCase(o.getKeyLang())) != 0) {
//                return result;
//            } else if ((result = this.getUiLang().compareToIgnoreCase(o.getUiLang())) != 0) {
//                return result;
//            }
//            return 0;
            result = this.getKeyLang().compareToIgnoreCase(o.getKeyLang());
            return result;
        }
    }

    @Override
    public void setMapping(String[] keys, String[] uiLangs){
        for (int i = 0; i < keys.length; i++) {
            Pair p = new Pair(keys[i], uiLangs[i]);
            for (int j = 0; j < langs.size(); j++) {
                if (langs.get(j).compareTo(p) == 0) {
                    langs.get(j).setUiLang(uiLangs[i]);
                    return;
                }
            }
            langs.add(p);
        }
        notifyAboutChanges();
    }

    //методы получения соответствующих значений
    @Override
    public String getLang(String uiLang){
        for (int i = 0; i < langs.size(); i++){
            if (langs.get(i).getUiLang().equals(uiLang))
                return langs.get(i).getKeyLang();
        }
        return null;
    }

    @Override
    public String getUiLang(String Lang){
        for (int i = 0; i < langs.size(); i++){
            if (langs.get(i).getKeyLang().equals(Lang))
                return langs.get(i).getUiLang();
        }
        return null;
    }

    @Override
    public void serialize(OutputStream fos) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deserialize(InputStream fis) {
        if (fis != null) {
            try {
                ObjectInputStream is = new ObjectInputStream(fis);
                StandardLanguageMapper container = (StandardLanguageMapper) is.readObject();
                if (container == null) {
                    container = new StandardLanguageMapper();
                }
                this.langs = container.langs;
            } catch (IOException | ClassNotFoundException e) {
                //TODO e.printStackTrace();

            }
        }
    }

    private void notifyAboutChanges(){
        eventBus.dispatchEvent(new MappingChangedEvent(this));
    }


    private void initListeners(EventBus eventBus) {
        eventBus.addListener(EventTypes.NEW_LANG_MAPPING_EVENT_TYPE,
                new Listener() {
                    @Override
                    public void onEvent(Event event) {
                        if (event != null && EventTypes.NEW_LANG_MAPPING_EVENT_TYPE == event.getType()) {
                            String[][] eventArgs = ((NewLangMappingEvent) event).getEventArgs();
                            setMapping(eventArgs[0], eventArgs[1]);
                        }
                        else {
                            throw new RuntimeException("Unsupported Event " + event);
                        }
                    }
                });
    }
}
