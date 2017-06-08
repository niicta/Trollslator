package niicta.trollslator.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;
import niicta.trollslator.events.Impl.AddHistoryElementRequestEvent;
import niicta.trollslator.events.Impl.HistoryChangedEvent;
import niicta.trollslator.events.Impl.InvertFavoriteByNumberRequestEvent;
import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.listeners.Listener;
import niicta.trollslator.model.History;
import niicta.trollslator.model.HistoryKey;
import niicta.trollslator.model.factories.ModelFactory;
import niicta.trollslator.model.factories.ModelFactoryProvider;

/**
 * Created by niict on 30.03.2017.
 */

//класс истории
//содержит в себе как историю непосредственно,
// так и позволяет работать с избранным, как подмножеством истории
public class StandardHistory implements History{
    //для хранения элементов истоии используется красно-черное дерево для быстрого поиска нужных элементов
    private TreeMap<HistoryKey, HistoryKey> history;
    private HistoryKey last;
    private ModelFactory historyKeyFactory;
    private EventBus eventBus;
    private boolean initialized;

    private StandardHistory(){};

    public StandardHistory(EventBus eventBus, InputStream inputStream){
        this(ModelFactoryProvider.getInstance().provideFactory(), eventBus, inputStream);
    }

    public StandardHistory(ModelFactory historyKeyFactory, EventBus eventBus, InputStream inputStream){
        this.eventBus = eventBus;
        this.historyKeyFactory = historyKeyFactory;
        this.history = new TreeMap<>();
        deserialize(inputStream);
        this.initialized = true;
        initListeners(this.eventBus);
        notifyAboutChanges();
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public HistoryKey getLast() {
        return last;
    }

    @Override
    public TreeMap<HistoryKey, HistoryKey> getHistory(){
        return history;
    }

    //получение списка избранного
    @Override
    public List<HistoryKey> getFavoriteList(){
        ArrayList<HistoryKey> result = new ArrayList<>();
        for (Map.Entry<HistoryKey, HistoryKey> entry : history.entrySet()){
            HistoryKey key = entry.getKey();
            if (key.isFavorite())
                result.add(key);
        }
        return result;
    }

    //запись истории в поток
    @Override
    public void serialize(OutputStream fos){
        try {
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.flush();
            os.close();
            this.initialized = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //чтение истории из потока
    @Override
    public void deserialize(InputStream fis){
        if (fis != null) {
            try {
                ObjectInputStream is = new ObjectInputStream(fis);
                StandardHistory history = (StandardHistory) is.readObject();
                if (history == null) {
                    history = new StandardHistory();
                }
                this.last = history.getLast();
                this.history = history.getHistory();
            } catch (IOException | ClassNotFoundException e) {
                //TODO e.printStackTrace();

            }
        }
    }

    //добавление нового элемента в конец истории
    @Override
    public void addLast(String fromLang, String toLang, String fromText, String translation){
        //формируется непосредственно сам элемент
        HistoryKey newLast = (HistoryKey)historyKeyFactory.createHistoryKey(fromLang, toLang, fromText.toLowerCase(), translation);
        //если в истории такого нет - просто добавляем в конец
        if (!history.containsKey(newLast)) {
            if (last != null) {
                newLast.setPrev(this.last);
                last.setNext(newLast);
            }
            last = newLast;
            this.history.put(newLast, newLast);
        }
        //иначе продвигаем элемент в начало истории вызовом сответствующего метода
        else{
            extract(newLast.getText(), newLast.getFromLang(), newLast.getToLang());
        }
        notifyAboutChanges();
    }

    @Override
    public void invertFavorite(int number){
        HistoryKey historyKey = find(number);
        historyKey.setFavorite(!historyKey.isFavorite());
        notifyAboutChanges();
    }

    @Override
    public boolean verifyFavorite(int number){
        HistoryKey historyKey = find(number);
        return historyKey.isFavorite();
    }

    //извлекаем определенный элемент и устанавливаем его как last
    @Override
    public HistoryKey extract(String text, String fromLang, String toLang){
        //находим элемент в истории
        HistoryKey element = history.get((HistoryKey)historyKeyFactory.createHistoryKey(fromLang, toLang, text, ""));
        if (element == null)
            return null;
        //проводим магические манипуляции со ссылками
        //стоит пояснить, что в истории каждый элемент, помимо того, что является элементом в дереве, хранит ссылки на другие эелменты
        //причем они выстроены так, чтобы отображать непосредвтенно историю запросов
        //таким образов, в дереве хранятся элементы по алфавиту, а сами элементы можно выстроить
        //в двусвязный список по времени добавления
        if (last.equals(element)) return element;
        last.setNext(element);
        element.getNext().setPrev(element.getPrev());
        HistoryKey elementPrev = element.getPrev();
        if (elementPrev != null) {
            elementPrev.setNext(element.getNext());
        }
        element.setPrev(last);
        element.setNext(null);
        last = element;
        notifyAboutChanges();
        return element;
    }

    //получаем элемент по порядковому номеру
    @Override
    public HistoryKey find(int number){
        HistoryKey element = last;
        for (int i =0; i < number && element != null; i++){
            element = element.getPrev();
        }
        return element;
    }

    //получаем список подходящих под маску элементов
    @Override
    public List<HistoryKey> find(String mask){
        ArrayList<HistoryKey> overlaps = new ArrayList<>();
        HistoryKey temp = last;
        if (!"".equals(mask)) {
            mask = mask.toLowerCase();
            while (temp != null) {
                if (temp.getText().toLowerCase().contains(mask) || temp.getTranslation().toLowerCase().contains(mask)) {
                    overlaps.add(temp);
                }
                temp = temp.getPrev();
            }
        }
        //если маска - пустая строка - просто возвращаем всю историю
        else{
            while(temp != null){
                overlaps.add(temp);
                temp = temp.getPrev();
            }
        }
        return overlaps;
    }

    @Override
    public void clear() {
        history = new TreeMap<>();
        last = null;
        notifyAboutChanges();
    }

    private void initListeners(final EventBus eventBus) {

        eventBus.addListener(EventTypes.ADD_HISTORY_ELEMENT_REQUEST_EVENT_TYPE,
                new Listener() {
                    @Override
                    public void onEvent(Event event) {
                        if (event != null && EventTypes.ADD_HISTORY_ELEMENT_REQUEST_EVENT_TYPE == event.getType()){
                            String[] eventArgs = ((AddHistoryElementRequestEvent)event).getEventArgs();
                            addLast(eventArgs[0], eventArgs[1], eventArgs[2], eventArgs[4]);
                        }
                        else {
                            throw new RuntimeException("Unsupported Event " + event);
                        }
                    }
                });
        eventBus.addListener(EventTypes.INVERT_FAVORITE_BY_NUMBER_REQUEST_EVENT_TYPE,
                new Listener() {
                    @Override
                    public void onEvent(Event event) {
                        if (event != null && EventTypes.INVERT_FAVORITE_BY_NUMBER_REQUEST_EVENT_TYPE == event.getType()){
                            int eventArgs = ((InvertFavoriteByNumberRequestEvent)event).getEventArgs();
                            invertFavorite(eventArgs);
                        }
                        else {
                            throw new RuntimeException("Unsupported Event " + event);
                        }
                    }
                });
    }

    private void notifyAboutChanges(){
        eventBus.dispatchEvent(new HistoryChangedEvent(this));
    }
}
