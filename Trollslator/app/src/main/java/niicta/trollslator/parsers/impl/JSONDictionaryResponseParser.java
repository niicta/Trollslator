package niicta.trollslator.parsers.impl;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import niicta.trollslator.model.History;

/**
 * Created by nikita on 26.03.17.
 */

//данный парсер на основе JSON объекта строит словарную статью
public class JSONDictionaryResponseParser {
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

    public View parseResponse(String response, Context context) {
        JSONObject baseobject = null;
        LinearLayout dictionaryResult = new LinearLayout(context);
        dictionaryResult.setLayoutParams(wrapContentParams);
        dictionaryResult.setOrientation(LinearLayout.VERTICAL);

       try {
            baseobject = new JSONObject(response);
            JSONArray defArray = baseobject.getJSONArray("def");
            for (int i = 0; i < defArray.length(); i++) {
                LinearLayout defLine = new LinearLayout(context);
                defLine.setLayoutParams(lineParams);
               
                defTextViewParams.setMarginStart((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, DEF_TEXT_DP_LEFT_MARGIN, context.getResources().getDisplayMetrics()));

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
                            trText.setText((j + 1) + ".  " + trObject.getString("text"));
                            LinearLayout.LayoutParams trTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            trTextParams.setMarginStart((int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, TR_FIRST_TEXT_DP_LEFT_MARGIN, context.getResources().getDisplayMetrics()));
                            trText.setLayoutParams(trTextParams);
                            trText.setTextColor(Color.BLACK);
                            trText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
                            trLine.addView(trText);
                        }
                        if(!trObject.isNull("gen")) {
                            TextView trGen = new TextView(context);
                            trGen.setText(" " + trObject.getString("gen"));
                            trGen.setLayoutParams(wrapContentParams);
                            trGen.setTextColor(Color.BLACK);
                            trGen.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
                            trLine.addView(trGen);
                        }




                        if (!trObject.isNull("syn")){
                            JSONArray synArray = trObject.getJSONArray("syn");
                            TextView synText = new TextView(context);
                            StringBuffer synBuffer = new StringBuffer("");
                            StringBuffer genBuffer = new StringBuffer("");

                            synText.setLayoutParams(wrapContentParams);
                            synText.setTextColor(Color.BLACK);
                            synText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
                            trLine.addView(synText);



                            for (int k = 0; k < synArray.length(); k++) {
                                JSONObject synObject = synArray.getJSONObject(k);
                                if (!synObject.isNull("text")) {

                                    //synText.setText(",  " + synObject.getString("text"));
                                    synBuffer.append(", ").append(synObject.getString("text"));

                                }
                                if(!synObject.isNull("gen")) {

                                    //synGen.setText(" " + synObject.getString("gen"));
                                    synBuffer.append(' ').append(synObject.getString("gen"));

                                }
                            }
                            synText.setText(synBuffer.toString());
                        }
                        dictionaryResult.addView(trLine);
                        if (!trObject.isNull("mean")){
                            LinearLayout meanLine = new LinearLayout(context);
                            meanLine.setOrientation(LinearLayout.HORIZONTAL);
                            meanLine. setLayoutParams(wrapContentParams);

                            JSONArray meanArray = trObject.getJSONArray("mean");
                            StringBuffer means = new StringBuffer("");
                            means.append('(');
                            means.append(meanArray.getJSONObject(0).getString("text"));
                            for (int k = 1; k < meanArray.length(); k++){
                                JSONObject meanObject = meanArray.getJSONObject(k);
                                means.append(", " + meanObject.getString("text"));
                            }
                            means.append(')');

                            meanTextViewParams.setMarginStart((int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, MEAN_TEXT_DP_LEFT_MARGIN, context.getResources().getDisplayMetrics()));
                            TextView meanText = new TextView(context);
                            meanText.setText(means.toString());
                            meanText.setLayoutParams(meanTextViewParams);
                            meanText.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
                            meanText.setTextColor(Color.RED);

                            meanLine.addView(meanText);
                            dictionaryResult.addView(meanLine);
                        }

                        if (!trObject.isNull("ex")){
                            LinearLayout exLine = new LinearLayout(context);
                            exLine.setOrientation(LinearLayout.HORIZONTAL);
                            exLine.setLayoutParams(wrapContentParams);

                            JSONArray exArray = trObject.getJSONArray("ex");

                            StringBuffer examples = new StringBuffer("");

                            for (int k = 0; k < exArray.length(); k++){
                                JSONObject exObject = exArray.getJSONObject(k);
                                examples.append(exObject.getString("text"));
                                //Check if "tr" existing
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

                            exTextViewParams.setMarginStart((int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, EX_TEXT_DP_LEFT_MARGIN, context.getResources().getDisplayMetrics()));
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
            e.printStackTrace();
           return null;
        }
        return dictionaryResult;
    }
}
