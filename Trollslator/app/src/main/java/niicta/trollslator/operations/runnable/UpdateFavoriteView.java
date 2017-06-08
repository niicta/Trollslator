package niicta.trollslator.operations.runnable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import niicta.trollslator.R;
import niicta.trollslator.model.impl.StandardHistory;

/**
 * Created by niict on 11.04.2017.
 */

//поток для "сборки" отображения избранного
public class UpdateFavoriteView implements Runnable {
    private LinearLayout favoriteLayout;
    private Context context;
    private StandardHistory history;

    public UpdateFavoriteView(Context context, LinearLayout favoriteLayout) {
        this.favoriteLayout = favoriteLayout;
        this.context = context;

    }

    @Override
    public void run() {
        history = StandardHistory.getInstance();
        favoriteLayout.removeAllViews();
        final ArrayList<StandardHistory.StandardHistoryKey> favoriteKeys = history.getFavoriteList();
        //в цикле просто создаем View и устанвливаем их в соответствии с текущим элементом
        for (int i = 0; i < favoriteKeys.size(); i++) {
            final int number = i;
            StandardHistory.StandardHistoryKey key = favoriteKeys.get(i);
            View favoriteList = LayoutInflater.from(context).inflate(R.layout.history_element, favoriteLayout, false);
            ((TextView) favoriteList.findViewById(R.id.history_text)).setText(key.getText());
            ((TextView) favoriteList.findViewById(R.id.history_translate)).setText(key.getTranslation());
            ((TextView) favoriteList.findViewById(R.id.history_lang)).setText(new StringBuilder().append(key.getFromLang()).append("-").append(key.getToLang()).toString());
            ImageView star = (ImageView) favoriteList.findViewById(R.id.star);

            if (key.isFavorite()) {
                star.setImageResource(R.drawable.favoriteimageiconcolor);
            } else {
                star.setImageResource(R.drawable.favoriteimageiconbw);
            }
            star.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StandardHistory.StandardHistoryKey element = history.find(number);
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
            favoriteLayout.addView(favoriteList);
            favoriteList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.text_appearing));

        }
    }
}
