package niicta.trollslator.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by niict on 07.06.2017.
 */

public interface LanguageMapper extends SerializableModelObject{
    void setMapping(String[] uiLang, String[] lang);
    String getLang(String uiLang);
    String getUiLang(String Lang);
}
