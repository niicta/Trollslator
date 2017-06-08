package niicta.trollslator;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import niicta.trollslator.model.impl.StandardLanguageMapper;
import niicta.trollslator.model.impl.StandardLanguageSwitcher;
import niicta.trollslator.view.adapters.TranslateAutoCompleteAdapter.Translation;
import niicta.trollslator.model.impl.StandardHistory;
import niicta.trollslator.operations.Translator;
import niicta.trollslator.operations.runnable.UpdateFavoriteView;
import niicta.trollslator.operations.runnable.UpdateHistoryView;
import niicta.trollslator.parsers.impl.JSONDictionaryResponseParser;
import niicta.trollslator.parsers.impl.JSONGetLangsResponseParser;
import niicta.trollslator.view.adapters.CustomArrayAdapter;
import niicta.trollslator.view.adapters.TranslateAutoCompleteAdapter;
import niicta.trollslator.view.views.LangBlock;
import niicta.trollslator.view.views.TranslateAutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    LangBlock langBlock;
    TextView translation ;
    EditText historyEditText;
    LinearLayout translateLayout;
    LinearLayout favoriteLayout;
    LinearLayout historyLayout;
    RequestQueue queue;
    StandardHistory history;
    Handler handler;
    ProgressBar centralProgressBar;
    ImageView star;
    Animation textAppearing;
    Animation layoutAppearing;
    TranslateAutoCompleteTextView translateAutoCompleteTextView;
    TextView clearText;
    TextView lettersCount;
    TextView dictionaryApi;
    TextView dictionaryLink;
    LinearLayout about;
    LinearLayout clearHistory;
    LinearLayout historyTab;
    LinearLayout translateTab;
    LinearLayout favoriteTab;
    LinearLayout settingsTab;
    final String dictionaryLookUpUrl = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";
    final String translatorGetLangsUrl = "https://translate.yandex.net/api/v1.5/tr.json/getLangs";
    final String dictionaryKey = "dict.1.1.20170325T201719Z.0b595b9a58fcd7d1.731b280f13352e192a2e8c3dabbb7a858247d223";
    final String translatorKey = "trnsl.1.1.20170318T145326Z.877ccf5b59ea84ed.478cee91da88a30f8e3941dddb74badea73be942";
    private final String HISTORY_FILE_NAME = "history.txt";

    Runnable updateHistoryView;
    Runnable updateFavoriteView;


    @Override
    protected void onStop() {
        super.onStop();
        try {
            StandardHistory.serialize(openFileOutput(HISTORY_FILE_NAME, MODE_PRIVATE));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FileInputStream fis = null;
        try {
            //deleteFile(HISTORY_FILE_NAME);
            fis = openFileInput(HISTORY_FILE_NAME);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis != null){
            StandardHistory.deserialize(fis);

        }
        history = StandardHistory.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Создание Handler и очереди запросов для Volley
        handler = new Handler();
        queue = Volley.newRequestQueue(this);

        textAppearing = AnimationUtils.loadAnimation(this, R.anim.text_appearing);
        //инициализация панели выбора языка
        langBlock = (LangBlock) findViewById(R.id.langBlock);
        initLangBlock();
        langBlock.setOnFromItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //установка StandardLanguageSwitcher - сущность, содержащая актуальное направление перевода
                    String selected = parent.getSelectedItem().toString();
                    StandardLanguageSwitcher.getInstance().setFromLang(StandardLanguageMapper.getInstance().getLang(selected));
                translateAutoCompleteTextView.invokeFiltering();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        langBlock.setOnToItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //установка StandardLanguageSwitcher
                    String selected = parent.getSelectedItem().toString();
                    StandardLanguageSwitcher.getInstance().setToLang(StandardLanguageMapper.getInstance().getLang(selected));
                translateAutoCompleteTextView.invokeFiltering();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //поле для отображение перевода от Яндекс.Переводчика
        translation = (TextView) findViewById(R.id.translate_to);
        star = (ImageView)findViewById(R.id.star);
        //Layout для словарной статьи от Яндекс.Словаря
        translateLayout = (LinearLayout) findViewById(R.id.translate_layout);
        //бар загрузки статьи
        centralProgressBar = (ProgressBar) findViewById(R.id.centralProgressBar);
        //Layput'ы истории и избранного
        historyLayout = (LinearLayout) findViewById(R.id.history_layout);
        favoriteLayout = (LinearLayout) findViewById(R.id.favoriteLayout);

        about = (LinearLayout) findViewById(R.id.about);
        clearHistory = (LinearLayout) findViewById(R.id.clearHistory);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.about))
                        .setMessage(R.string.aboutString)
                        .setIcon(R.drawable.about)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.clearHistory) + '?')
                        .setIcon(R.drawable.binimagecolor_small)
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id) {
                                        history.clear();
                                        translateLayout.removeAllViews();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //работа с владками
        final TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("mainTab");
        tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.selector_translate_tab, null));
        tabSpec.setContent(R.id.mainTab);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("historyTab");
        tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.selector_history_tab, null));
        tabSpec.setContent(R.id.historyTab);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("favoriteTab");
        tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.selector_favorite_tab, null));
        tabSpec.setContent(R.id.favoriteTab);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("settingsTab");
        tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.selector_settings_tab, null));
        tabSpec.setContent(R.id.settingsTab);
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandardHistory.StandardHistoryKey element = history.getLast();
                boolean favorite = element.isFavorite();
                if (favorite) {
                    element.setFavorite(false);
                    ((ImageView) v).setImageResource(R.drawable.favoriteimageiconbw);
                } else {
                    element.setFavorite(true);
                    ((ImageView) v).setImageResource(R.drawable.favoriteimageiconcolor);
                }
            }
        });
        //Переопределенное поле с автозаполнением
        translateAutoCompleteTextView = (TranslateAutoCompleteTextView) findViewById(R.id.from_word);
        translateAutoCompleteTextView.setThreshold(1);
        //установка переопределенного адаптера
        translateAutoCompleteTextView.setAdapter(new TranslateAutoCompleteAdapter(getApplicationContext()));
        translateAutoCompleteTextView.setLoadingIndicator((ProgressBar) findViewById(R.id.progress_bar));
        //по клику на предлагаемый вариант запрашиваем словарную статьи и добавляем перевод в историю
        translateAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                star.setVisibility(View.GONE);
                translation.setText("");
                Translation t = (Translation) parent.getItemAtPosition(position);
                translation.setText(t.getTransate());
                history.addLast(t.getFromLang(), t.getToLang(), t.getText(), t.getTransate());
                star.setVisibility(View.VISIBLE);
                if (history.getLast() != null)
                    star.setImageResource(history.getLast().isFavorite() ? R.drawable.favoriteimageiconcolor : R.drawable.favoriteimageiconbw);
                translation.startAnimation(textAppearing);
                star.startAnimation(textAppearing);
                translateAutoCompleteTextView.setText(t.getText());

                translateLayout.removeAllViews();
                dictionaryApi.setVisibility(View.GONE);
                dictionaryLink.setVisibility(View.GONE);
                centralProgressBar.setVisibility(View.VISIBLE);
                setDictionaryEntry();

            }
        });
        lettersCount = (TextView) findViewById(R.id.lettersCount);
        //оповещения пользователя о максимальном размере текста
        translateAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > Translator.MAX_LETTERS_COUNT){
                    lettersCount.setTextColor(Color.RED);
                }
                else{
                    lettersCount.setTextColor(Color.GRAY);
                }
                lettersCount.setText(s.toString().length() + "/" + Translator.MAX_LETTERS_COUNT);
            }
        });
        clearText = (TextView) findViewById(R.id.clearText);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateAutoCompleteTextView.setText("");
            }
        });
        //поле поиска по истории
        historyEditText = (EditText) findViewById(R.id.historyEditText);
        //потоки для обновления отображения истории и избранного
        updateHistoryView = new UpdateHistoryView(getApplicationContext(), historyLayout, historyEditText);
        updateFavoriteView = new UpdateFavoriteView(getApplicationContext(), favoriteLayout);

        //переходы между вкладками нужно обработать
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("mainTab") && history.getLast() != null){
                    star.setImageResource(history.getLast().isFavorite() ? R.drawable.favoriteimageiconcolor : R.drawable.favoriteimageiconbw);
                }
                //отображаем представления истории и избранного, если они были изменены
                if (tabId.equals("historyTab") && history.isHistoryChanged()){
                    handler.post(updateHistoryView);

                }
                if (tabId.equals("favoriteTab") && history.isFavoriteChanged()){
                    handler.post(updateFavoriteView);

                }
                if (tabId.equals("mainTab")){
                    //инициируем выброс подсказки, т.к. при уходе с вкладки перевода она пропадает
                    translateAutoCompleteTextView.invokeFiltering();
                    if (history.getLast() != null)
                        star.setImageResource(history.getLast().isFavorite() ? R.drawable.favoriteimageiconcolor : R.drawable.favoriteimageiconbw);
                }
            }
        });

        //отображаем поиск по истории с помощью потока отображения истории
        //отправляем сообщение в Handler с задержкой, чтобы не нагружать память потоками и
        //распознать (с долей вероятности) момент, когда пользователь закончил печать
        //и хочет увидеть результат поиска
        historyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(updateHistoryView);
                handler.postDelayed(updateHistoryView, 750);
            }
        });

        dictionaryApi = (TextView) findViewById(R.id.dictionaryAPI);
        dictionaryApi.setVisibility(View.GONE);
        dictionaryLink = (TextView) findViewById(R.id.dictionaryLink);
        dictionaryLink.setVisibility(View.GONE);



    }



    private void initLangBlock(){
        langBlock.loading(true);
        queue.cancelAll(getResources().getString(R.string.langsJSON_tag));
        //используем Volley для загрузки поддерживаемых языков
        final StringRequest getLangsRequest =
                new StringRequest(Request.Method.POST, translatorGetLangsUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //парсинг ответа
                                JSONGetLangsResponseParser parser = new JSONGetLangsResponseParser();
                                ArrayList<String> keyStrings = new ArrayList<>();
                                ArrayList<String> uiStrings = new ArrayList<>();
                                int code = parser.parseResponse(response, keyStrings, uiStrings);
                                //если что-то пошло не так в процессе парсинга (что очень маловероятно), скажем об этом
                                if (code < 0){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.somethingWrong), Toast.LENGTH_LONG).show();
                                    langBlock.loading(false);
                                    return;
                                }
                                //получаем сущность-"переключатель" языков, содержащую актуальное направление перевода
                                StandardLanguageMapper container = StandardLanguageMapper.getInstance();
                                for (int i = 0; i < keyStrings.size(); i++){
                                    //устанавливаем соответсвия ключей языков с их ui представлениями
                                    container.setMapping(uiStrings.get(i), keyStrings.get(i));
                                }
                                //установка custom-адаптеров для выпадающего списка
                                CustomArrayAdapter<String> fromAdapter = new CustomArrayAdapter<>(getApplicationContext(), R.layout.drop_down_spinner_item, uiStrings);
                                CustomArrayAdapter<String> toAdapter = new CustomArrayAdapter<>(getApplicationContext(), R.layout.drop_down_spinner_item, uiStrings);
                                langBlock.setFromAdapter(fromAdapter);
                                langBlock.setToAdapter(toAdapter);
                                langBlock.loading(false);
                                langBlock.setFromSpinnerItem(StandardLanguageMapper.getInstance().getUiLang(getResources().getString(R.string.ui)));
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                //обрабатываем ошибки
                                //почему-то при невалидном ключе volley в коде ошибки пишет 403,
                                // однако я решил оставить обработку, как указано в документации,
                                //так как не знаю наверняка
                                if (error instanceof TimeoutError){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.notResponding), Toast.LENGTH_LONG).show();

                                } else if (error.networkResponse != null && error.networkResponse.statusCode == 401){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidKey), Toast.LENGTH_LONG).show();
                                } else if (error.networkResponse != null && error.networkResponse.statusCode == 402){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.blockedKey), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.somethingWrong), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                ){
                    //заполнение параметров для post-запроса
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("key", translatorKey);
                        //берем язык ui из ресурсов
                        paramsMap.put("ui", getResources().getString(R.string.ui));
                        return paramsMap;
                    }
                };
        queue.add(getLangsRequest);

    }

    //загрузка и отображение словарной статьи
    void setDictionaryEntry(){
        //отменяем предыдущие подобные запросы
        queue.cancelAll(getResources().getString(R.string.dictionaryJSON_tag));
        final StringRequest dictionaryRequest =
                new StringRequest(Request.Method.POST, dictionaryLookUpUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //парсинг ответа
                                JSONDictionaryResponseParser parser = new JSONDictionaryResponseParser();
                                centralProgressBar.setVisibility(View.GONE);
                                //установка отображения статьи
                                View dictionaryResult=  parser.parseResponse(response, getApplicationContext());
                                if (dictionaryResult != null) {
                                    translateLayout.addView(dictionaryResult);
                                    dictionaryResult.startAnimation(textAppearing);
                                    dictionaryApi.setVisibility(View.VISIBLE);
                                    dictionaryLink.setVisibility(View.VISIBLE);
                                }

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                centralProgressBar.setVisibility(View.GONE);
                                //обработка ошибок
                                //почему-то при невалидном ключе volley в коде ошибки пишет 403,
                                // однако я решил оставить обработку, как указано в документации,
                                //так как не знаю наверняка
                                if (error instanceof TimeoutError){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.notResponding), Toast.LENGTH_LONG).show();

                                } else if (error.networkResponse != null && error.networkResponse.statusCode == 401){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalidKey), Toast.LENGTH_LONG).show();
                                } else if (error.networkResponse != null && error.networkResponse.statusCode == 402){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.blockedKey), Toast.LENGTH_LONG).show();
                                } else if (error.networkResponse != null && error.networkResponse.statusCode == 403){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.dailyLimit), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                ){
                    //установка параметров post-запроса
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("key", dictionaryKey);
                        StringBuffer lang = new StringBuffer("");
                        lang.append(StandardLanguageSwitcher.getInstance().getFromLang()).append('-').append(StandardLanguageSwitcher.getInstance().getToLang());
                        paramsMap.put("lang", lang.toString());
                        paramsMap.put("text", translateAutoCompleteTextView.getText().toString());
                        paramsMap.put("ui", getResources().getString(R.string.ui));
                        return paramsMap;
                    }
                };
        queue.add(dictionaryRequest);

    }







}