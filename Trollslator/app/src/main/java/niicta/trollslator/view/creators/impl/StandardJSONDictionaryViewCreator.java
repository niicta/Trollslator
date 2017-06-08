package niicta.trollslator.view.creators.impl;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import niicta.trollslator.events.Event;
import niicta.trollslator.events.EventTypes;
import niicta.trollslator.events.Impl.CreateDictionaryViewFromJSONRequestEvent;
import niicta.trollslator.events.Impl.NewDictionaryViewEvent;
import niicta.trollslator.events.eventbus.EventBus;
import niicta.trollslator.listeners.Listener;
import niicta.trollslator.view.creators.ViewCreator;

/**
 * Created by niict on 09.06.2017.
 */

public class StandardJSONDictionaryViewCreator implements ViewCreator<JSONObject> {
    private final int DEF_TEXT_DP_LEFT_MARGIN = 10;
    private final int TR_FIRST_TEXT_DP_LEFT_MARGIN = 15;
    private final int MEAN_TEXT_DP_LEFT_MARGIN = 20;
    private final int EX_TEXT_DP_LEFT_MARGIN = 25;
    private final int TEXT_SIZE = 17;
    private final LinearLayout.LayoutParams wrapContentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams defTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams meanTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams exTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private final LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    private Context context;
    private EventBus eventBus;

    public StandardJSONDictionaryViewCreator(EventBus eventBus, Context context){
        this.context = context;
        this.eventBus = eventBus;
        initListeners(eventBus);
    }

    private int getDP(int requiredValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, requiredValue, context.getResources().getDisplayMetrics());
    }

    public void createView(JSONObject source){
        LinearLayout dictionaryResult = new LinearLayout(context);
        dictionaryResult.setLayoutParams(wrapContentParams);
        dictionaryResult.setOrientation(LinearLayout.VERTICAL);
        try {
            JSONArray defArray = source.getJSONArray("def");
            for (int i = 0; i < defArray.length(); i++) {
                LinearLayout defLine = new LinearLayout(context);
                defLine.setLayoutParams(lineParams);
                defTextViewParams.setMarginStart(getDP(DEF_TEXT_DP_LEFT_MARGIN));
                JSONObject defObject = defArray.getJSONObject(i);

                TextView text = new TextView(context);
                text.setText(defObject.getString("text"));
                text.setTextColor(Color.BLACK);
                text.setLayoutParams(defTextViewParams);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                TextView ts = new TextView(context);
                ts.setText('[' + defObject.getString("ts") + ']');
                ts.setTextColor(Color.GRAY);
                ts.setLayoutParams(defTextViewParams);
                ts.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                TextView pos = new TextView(context);
                pos.setText(defObject.getString("pos"));
                pos.setTextColor(Color.RED);
                pos.setLayoutParams(defTextViewParams);
                pos.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                defLine.addView(text);
                defLine.addView(ts);
                defLine.addView(pos);

                dictionaryResult.addView(defLine);
                if (!defObject.isNull("tr")){
                    JSONArray trArray = defObject.getJSONArray("tr");
                    for (int j = 0; j < trArray.length(); j++){
                        JSONObject trObject = trArray.getJSONObject(j);
                        LinearLayout trLine = new LinearLayout(context);
                        trLine.setLayoutParams(wrapContentParams);
                        trLine.setOrientation(LinearLayout.HORIZONTAL);

                        if(!trObject.isNull("text")) {
                            TextView trText = new TextView(context);

                            LinearLayout.LayoutParams trTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            trTextParams.setMarginStart(getDP(TR_FIRST_TEXT_DP_LEFT_MARGIN));
                            trText.setText((j + 1) + ".  " + trObject.getString("text"));
                            trText.setTextColor(Color.BLACK);
                            trText.setLayoutParams(trTextParams);
                            trText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                            trLine.addView(trText);
                        }
                        if(!trObject.isNull("gen")) {
                            TextView trGen = new TextView(context);
                            trGen.setText(" " + trObject.getString("gen"));
                            trGen.setTextColor(Color.BLACK);
                            trGen.setLayoutParams(wrapContentParams);
                            trGen.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                            trLine.addView(trGen);
                        }
                        if (!trObject.isNull("syn")){
                            JSONArray synArray = trObject.getJSONArray("syn");
                            StringBuffer synBuffer = new StringBuffer("");

                            TextView synText = new TextView(context);

                            for (int k = 0; k < synArray.length(); k++) {
                                JSONObject synObject = synArray.getJSONObject(k);
                                if (!synObject.isNull("text")) {
                                    synBuffer.append(", ").append(synObject.getString("text"));

                                }
                                if(!synObject.isNull("gen")) {
                                    synBuffer.append(' ').append(synObject.getString("gen"));
                                }
                            }

                            synText.setText(synBuffer.toString());
                            synText.setTextColor(Color.BLACK);
                            synText.setLayoutParams(wrapContentParams);
                            synText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                            trLine.addView(synText);
                        }
                        dictionaryResult.addView(trLine);
                        if (!trObject.isNull("mean")){
                            LinearLayout meanLine = new LinearLayout(context);
                            meanLine.setLayoutParams(wrapContentParams);
                            meanLine.setOrientation(LinearLayout.HORIZONTAL);

                            JSONArray meanArray = trObject.getJSONArray("mean");
                            StringBuffer means = new StringBuffer("");
                            means.append('(');
                            means.append(meanArray.getJSONObject(0).getString("text"));
                            for (int k = 1; k < meanArray.length(); k++){
                                JSONObject meanObject = meanArray.getJSONObject(k);
                                means.append(", " + meanObject.getString("text"));
                            }
                            means.append(')');
                            meanTextViewParams.setMarginStart(getDP(MEAN_TEXT_DP_LEFT_MARGIN));

                            TextView meanText = new TextView(context);
                            meanText.setText(means.toString());
                            meanText.setTextColor(Color.RED);
                            meanText.setLayoutParams(meanTextViewParams);
                            meanText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                            meanLine.addView(meanText);
                            dictionaryResult.addView(meanLine);
                        }
                        if (!trObject.isNull("ex")){
                            LinearLayout exLine = new LinearLayout(context);
                            exLine.setLayoutParams(wrapContentParams);
                            exLine.setOrientation(LinearLayout.HORIZONTAL);

                            JSONArray exArray = trObject.getJSONArray("ex");

                            StringBuffer examples = new StringBuffer("");
                            for (int k = 0; k < exArray.length(); k++){
                                JSONObject exObject = exArray.getJSONObject(k);
                                examples.append(exObject.getString("text"));
                                if (!exObject.isNull("tr")){
                                    JSONArray exTrArray = exObject.getJSONArray("tr");
                                    examples.append(" - ").append(exTrArray.getJSONObject(0).getString("text"));
                                    //looking for another examples
                                    for (int l = 1; l < exTrArray.length(); l++){
                                        JSONObject exTrObject = exTrArray.getJSONObject(l);
                                        examples.append(", ").append(exTrObject.getString("text"));
                                    }
                                }
                                if (k != exArray.length() - 1)
                                    examples.append('\n');
                            }
                            exTextViewParams.setMarginStart(getDP(EX_TEXT_DP_LEFT_MARGIN));

                            TextView exText = new TextView(context);
                            exText.setText(examples.toString());
                            exText.setLayoutParams(exTextViewParams);
                            exText.setTextColor(Color.GRAY);
                            exText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);

                            exLine.addView(exText);
                            dictionaryResult.addView(exLine);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Impossible to parse JSON\n" + source);
        }
        notifyAboutNewView(dictionaryResult);
    }

    private void initListeners(EventBus eventBus) {
        eventBus.addListener(EventTypes.CREATE_DICTIONARY_VIEW_FROM_JSON_EVENT_TYPE,
                new Listener() {
                    @Override
                    public void onEvent(Event event) {
                        if (event != null || EventTypes.CREATE_DICTIONARY_VIEW_FROM_JSON_EVENT_TYPE == event.getType()){
                            JSONObject source = ((CreateDictionaryViewFromJSONRequestEvent)event).getEventArgs();
                            createView(source);
                        }
                    }
                });
    }

    private void notifyAboutNewView(View view){
        eventBus.dispatchEvent(new NewDictionaryViewEvent(view));
    }
}
