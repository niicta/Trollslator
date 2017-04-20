package niicta.trollslator.operations;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.ParserConfigurationException;

import niicta.trollslator.R;
import niicta.trollslator.model.LangSwitcher;
import niicta.trollslator.parsers.impl.XMLTranslateResponseParser;

/**
 * Created by nikita on 22.03.17.
 */

//Класс, в=непосредв=ственно взаимодействующий с сервисом Яндекс.Переводчик для получения перевода
public class Translator {

    //ограничение на длину переводимого текста
    public static final int MAX_LETTERS_COUNT = 10000;
    private static Translator instance = new Translator();

    private Translator() {
        lastText = "";
        lastTranslate = "";
        lastToLang = "";
        lastFromLang = "";

    }

    private final String PATH = "https://translate.yandex.net/api/v1.5/tr/translate?key="+"trnsl.1.1.20170318T145326Z.877ccf5b59ea84ed.478cee91da88a30f8e3941dddb74badea73be942";

    //Примитивный кэш
    private String lastText;
    private String lastFromLang;
    private String lastToLang;
    private String lastTranslate;



    public static Translator getInstance(){
        return instance;
    }

    public String translate(String text, String fromLang, String toLang, Context context) throws IOException, ParserConfigurationException, SAXException {

        if ("".equals(text)) return "";
        //проверка на наличие результата в кеше
        if (lastFromLang.equals(fromLang) && lastToLang.equals(toLang) && lastText.equals(text))
            return lastTranslate;


        //строи параметры запроса
        String urlParameters = new StringBuilder()
                .append("text=").append(URLEncoder.encode(text, "UTF8"))
                .append("&lang=").append(LangSwitcher.getInstance().getFromLang())
                .append('-').append(LangSwitcher.getInstance().getToLang()).toString();
        //перевод параметров в массив байтов
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        //формируем запрос и соединяемся с сервером
        URL url = new URL(PATH);
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty( "Charset", "utf-8");
        connection.setUseCaches(false);
        connection.setRequestProperty( "Content-Length", Integer.toString(postDataLength));
        DataOutputStream wr = new DataOutputStream(
                connection.getOutputStream ());
        wr.writeBytes (urlParameters);
        wr.flush ();
        wr.close ();
        int code = 0;
        try {
            code = connection.getResponseCode();
        } catch (Exception e){
            return String.valueOf(code);
        }
        //если что-то пошло не так, оповестим и закончим перевод
        if (code != 200) {
            String message = "";
            switch (code) {
                case 401: message = context.getResources().getString(R.string.invalidKey); break;
                case 402: message = context.getResources().getString(R.string.blockedKey); break;
                case 404: message = context.getResources().getString(R.string.dailyLimit); break;
                case 413: message = context.getResources().getString(R.string.countLimit); break;
                case 422: message = context.getResources().getString(R.string.cantTranslate); break;
                case 501: message = context.getResources().getString(R.string.illegalLang); break;

            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return "";
        }
        //парсинг ответа
        XMLTranslateResponseParser parser = new XMLTranslateResponseParser();
        lastText = text;
        lastFromLang = fromLang;
        lastToLang = toLang;
        lastTranslate = parser.parseResponse(connection.getInputStream());
        return lastTranslate;

    }
}

