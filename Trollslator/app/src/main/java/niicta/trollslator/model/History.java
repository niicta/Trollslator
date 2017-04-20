package niicta.trollslator.model;

import android.support.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by niict on 30.03.2017.
 */

//класс истории
//содержит в себе как историю непосредственно, так и позволяет работать с избранным, как подмножеством истории
public class History implements Serializable{


    private HistoryKey last;
    private boolean historyChanged;
    private boolean favoriteChanged;
    //для хранения элементов истоии используется красно-черное дерево для быстрого поиска нужных элементов
    private TreeMap<HistoryKey, HistoryKey> history;


    private static History instance = new History();

    //История хранит в себе информацию о том, что она была изменена
    //при запросе этой информации, считаем, что данные из истории считаны и состояние "изменена" надо сбросить
    public boolean isHistoryChanged() {
        if (historyChanged){
            historyChanged = false;
            return true;
        }
        return false;
    }
    //аналогично для избранного
    public boolean isFavoriteChanged() {
        if (favoriteChanged){
            favoriteChanged = false;
            return true;
        }
        return false;
    }

    private History(){
        history = new TreeMap<>();
    }

    public static History getInstance(){
        return instance;
    }

    //добавление нового элемента в конец истории
    public void addLast(String fromLang, String toLang, String fromText, String translation){
        //формируется непосредственно сам элемент
        HistoryKey newLast = new HistoryKey(fromLang, toLang, fromText.toLowerCase(), translation);
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
            find(newLast.getText(), newLast.getFromLang(), newLast.getToLang());
        }
        //меняем состояние изменения истории
        historyChanged = true;
    }

    public HistoryKey getLast() {
        return last;
    }


    //извлекаем определенный элемент и устанавливаем его как last
    public HistoryKey find(String text , String fromLang, String toLang){
        //находим элемент в истории
        HistoryKey element = history.get(new HistoryKey(fromLang, toLang, text, ""));
        if (element == null)
            return null;
        //проводим магические манипуляции со ссылками
        //стоит пояснить, что в истории каждый элемент, помимо того, что является элементом в дерегве, хранит ссылки на другие эелментв
        //причем они выстроены так, чтобы отображать непосредвтенно историю запросов
        //таким образов, в дереве хранятся элементы по алфавиту, а сами элементы можно выстроить
        //в двусвязный список по времени его добавления
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
        historyChanged = true;
        return element;
    }

    //получаем элемент по порядку
    public HistoryKey find(int number){
        HistoryKey element = last;
        for (int i =0; i < number && element != null; i++){
            element = element.prev;
        }
        return element;
    }
    //получаем списов пересекающихся с маской элементов
    public ArrayList<HistoryKey> find(String mask){
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

    //запись истории в поток
    public static void serialize(FileOutputStream fos){
        try {
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(instance);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //чтение истории из потока
    public static void deserialize(FileInputStream fis){
        try {
            ObjectInputStream is = new ObjectInputStream(fis);
            instance = (History) is.readObject();
            if (instance == null){
                instance = new History();
            }
            if (instance.history != null && instance.history.size() != 0) {
                instance.favoriteChanged = true;
                instance.historyChanged = true;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }

    //получение списка избранного
    public ArrayList<HistoryKey> getFavoriteList(){
        ArrayList<HistoryKey> result = new ArrayList<>();
        for (Map.Entry<HistoryKey, HistoryKey> entry : history.entrySet()){
            HistoryKey key = entry.getKey();
            if (key.isFavorite())
                result.add(key);
        }
        //так как список мы уже считали, то можно сбросить флаг изменения
        favoriteChanged = false;
        return result;
    }

    public void clear() {
        history = new TreeMap<>();
        last = null;
        historyChanged = true;
        favoriteChanged = true;
    }

    //описание элемента истории
    public class HistoryKey implements Comparable<HistoryKey>, Serializable{


        private String fromLang;
        private String toLang;
        private String text;
        private String translation;
        //ссылки на предыдущий и следующий (по времени) элементы
        private HistoryKey prev;
        private HistoryKey next;
        boolean isFavorite;

        HistoryKey(String fromLang, String toLang, String text, String translation) {
            this.fromLang = fromLang;
            this.toLang = toLang;
            this.text = text;
            this.next = null;
            this.translation = translation;
            isFavorite = false;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        //добавление элемента в избранное с установкой флагов изменения
        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
            favoriteChanged = true;
            historyChanged = true;
        }

        HistoryKey getPrev() {
            return prev;
        }

        void setPrev(HistoryKey prev) {
            this.prev = prev;
        }

        HistoryKey getNext() {
            return next;
        }

        void setNext(HistoryKey next) {
            this.next = next;
        }

        public String getFromLang() {
            return fromLang;
        }



        public String getToLang() {
            return toLang;
        }



        public String getText() {
            return text;
        }



        public String getTranslation() {
            return translation;
        }


        //описание логики сравнения элеемнтов для хранения и поиска их по дереву
        @Override
        public int compareTo(@NonNull HistoryKey o) {
            int result;
            if((result = this.getText().compareToIgnoreCase(o.getText())) != 0)
                return  result;
            else if ((result = this.getFromLang().compareToIgnoreCase(o.getFromLang())) != 0){
                return result;
            }
            else if((result = this.getToLang().compareToIgnoreCase(o.getToLang())) != 0){
                return result;
            }
            else return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || !(obj instanceof HistoryKey)) return  false;
            return ((HistoryKey) obj).compareTo(this) == 0;
        }
    }


}
