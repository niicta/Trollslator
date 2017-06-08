package niicta.trollslator.model;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by niict on 08.06.2017.
 */

public interface LanguageSwitcher extends SerializableModelObject {
    String getFromLang();
    void setFromLang(String fromLang);
    String getToLang();
    void setToLang(String toLang);
}
