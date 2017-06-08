package niicta.trollslator.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.Impl.ChangeLanguageDirectionEventType;
import niicta.trollslator.events.Impl.LanguageSwitcherChangedEvent;
import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.listeners.Listener;
import niicta.trollslator.model.LanguageSwitcher;

/**
 * Created by niict on 04.04.2017.
 */

//контейне, хранящий актуальное направление перевода
public class StandardLanguageSwitcher implements LanguageSwitcher{
    private String fromLang;
    private String toLang;
    private EventBus eventBus;


    private StandardLanguageSwitcher(){}

    public StandardLanguageSwitcher(EventBus eventBus){
        this(null, eventBus);
    }

    public StandardLanguageSwitcher(InputStream is, EventBus eventBus) {
        this.eventBus = eventBus;
        initListeners(eventBus);
        deserialize(is);
        notifyAboutChanges();
    }

    private void notifyAboutChanges() {
        eventBus.dispatchEvent(new LanguageSwitcherChangedEvent(this));
    }

    private void initListeners(EventBus eventBus) {
        final String changeLanguageDirectionEventType = new ChangeLanguageDirectionEventType().getType();
        eventBus.addListener(changeLanguageDirectionEventType,
                new Listener() {
                    @Override
                    public void onEvent(Event event) {
                        if (event != null && changeLanguageDirectionEventType.equals(event.getType())) {
                            String[] eventArgs = ((ChangeLanguageDirectionEventType) event).getEventArgs();
                            setFromLang(eventArgs[0]);
                            setToLang(eventArgs[1]);
                        }
                        else {
                            throw new RuntimeException("Unsupported Event " + event);
                        }
                    }
                });
    }

    @Override
    public String getFromLang() {
        return fromLang;
    }

    @Override
    public void setFromLang(String fromLang) {
        if (fromLang != null || !"".equals(fromLang)) {
            this.fromLang = fromLang;
            notifyAboutChanges();
        }
    }

    @Override
    public String getToLang() {
        return toLang;
    }

    @Override
    public void setToLang(String toLang) {
        if (toLang != null || !"".equals(toLang)) {
            this.toLang = toLang;
            notifyAboutChanges();
        }
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
                StandardLanguageSwitcher switcher = (StandardLanguageSwitcher) is.readObject();
                if (switcher == null) {
                    switcher = new StandardLanguageSwitcher();
                }
                this.toLang = switcher.getToLang();
                this.fromLang = switcher.getFromLang();
            } catch (IOException | ClassNotFoundException e) {
                //TODO e.printStackTrace();

            }
        }
    }
}
