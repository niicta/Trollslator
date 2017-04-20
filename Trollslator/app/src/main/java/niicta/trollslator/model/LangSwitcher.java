package niicta.trollslator.model;

/**
 * Created by niict on 04.04.2017.
 */

//контейне, хранящий актуальное направление перевода
public class LangSwitcher {
    private static LangSwitcher instance = new LangSwitcher();
    private LangSwitcher(){

    }

    public static LangSwitcher getInstance() {
        return instance;
    }

    private String fromLang;
    private String toLang;

    public String getFromLang() {
        return fromLang;
    }

    public void setFromLang(String fromLang) {
        this.fromLang = fromLang;
    }

    public String getToLang() {
        return toLang;
    }

    public void setToLang(String toLang) {
        this.toLang = toLang;
    }
}
