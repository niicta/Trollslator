package niicta.trollslator.operations.runnable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import niicta.trollslator.R;
import niicta.trollslator.model.History;

/**
 * Created by niict on 11.04.2017.
 */
//поток для "сборки" отображения истории, аналогичен UpdateFavoriteView
public class UpdateHistoryView implements Runnable {
    private Context context;
    private LinearLayout historyLayout;
    private EditText historyEditText;
    private History history;

    public UpdateHistoryView(Context context, LinearLayout historyLayout, EditText historyEditText) {
        this.context = context;
        this.historyLayout = historyLayout;
        this.historyEditText = historyEditText;

    }


    @Override
    public void run() {
        history = History.getInstance();
        historyLayout.removeAllViews();
        final ArrayList<History.HistoryKey> historyKeys = history.find(historyEditText.getText().toString());

        for (int i = 0; i < historyKeys.size(); i++) {
            final int number = i;
            History.HistoryKey key = historyKeys.get(i);
            View historyList = LayoutInflater.from(context).inflate(R.layout.history_element, historyLayout, false);
            ((TextView) historyList.findViewById(R.id.history_text)).setText(key.getText());
            ((TextView) historyList.findViewById(R.id.history_translate)).setText(key.getTranslation());
            ((TextView) historyList.findViewById(R.id.history_lang)).setText(new StringBuilder().append(key.getFromLang()).append("-").append(key.getToLang()).toString());
            ImageView star = (ImageView) historyList.findViewById(R.id.star);

            if (key.isFavorite()) {
                star.setImageResource(R.drawable.favoriteimageiconcolor);
            } else {
                star.setImageResource(R.drawable.favoriteimageiconbw);
            }
            star.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    History.HistoryKey element = history.find(number);
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
            historyLayout.addView(historyList);
            historyList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.text_appearing));

        }
    }
}
