package niicta.trollslator.view.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import niicta.trollslator.operations.Translator;

/**
 * Created by niict on 04.04.2017.
 */

public class TranslateAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    private static final int TEXT_CHANGED_MESSAGE = 10;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;

    private int delay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar loadingIndicator;

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            TranslateAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1 );

        }
    };

    public TranslateAutoCompleteTextView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setLoadingIndicator(ProgressBar loadingIndicator){
        this.loadingIndicator = loadingIndicator;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (text.length() > Translator.MAX_LETTERS_COUNT || text.length() == 0) return;
        if (loadingIndicator != null){
            loadingIndicator.setVisibility(View.VISIBLE);
        }
        handler.removeMessages(TEXT_CHANGED_MESSAGE);
        handler.sendMessageDelayed(handler.obtainMessage(TEXT_CHANGED_MESSAGE, text), delay);
    }

    public void invokeFiltering(){
        String text = getText().toString();
        setText("");
        setText(text);
    }

    @Override
    public void onFilterComplete(int count) {
        if (loadingIndicator != null){
            loadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
}
