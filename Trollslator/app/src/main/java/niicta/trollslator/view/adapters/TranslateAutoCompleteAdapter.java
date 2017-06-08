package niicta.trollslator.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import niicta.trollslator.R;
import niicta.trollslator.model.impl.StandardLanguageSwitcher;
import niicta.trollslator.operations.Translator;

/**
 * Created by niict on 04.04.2017.
 */

public class TranslateAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private List<Translation> translateResults;

    public TranslateAutoCompleteAdapter(Context context) {
        this.context = context;
        translateResults = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return  translateResults.size();
    }

    @Override
    public Translation getItem(int position) {
        return translateResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.translation_drop_item, parent, false);
        }

        Translation translation = getItem(position);
        ((TextView) convertView.findViewById(R.id.item_text)).setText(translation.getText());
        ((TextView) convertView.findViewById(R.id.item_translate)).setText(translation.getTransate());

        return convertView;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null){
                    List<Translation> translations = new ArrayList<>();
                    StandardLanguageSwitcher switcher = StandardLanguageSwitcher.getInstance();
                    try {
                        String fromLang = switcher.getFromLang();
                        String toLang = switcher.getToLang();
                        String translate = Translator.getInstance().translate(constraint.toString(), fromLang, toLang, context);
                        Translation t = new Translation(constraint.toString(), translate, fromLang, toLang);
                        translations.add(t);

                        filterResults.values = translations;
                        filterResults.count = translations.size();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }


                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count >0){
                    translateResults = (List<Translation>) results.values;
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    public class Translation
    {
        private String text;
        private String transate;
        private String fromLang;
        private String toLang;

        public String getText() {
            return text;
        }

        public String getTransate() {
            return transate;
        }

        public String getFromLang() {
            return fromLang;
        }

        public String getToLang() {
            return toLang;
        }

        Translation(String text, String transate, String fromLang, String toLang) {
            this.text = text;
            this.transate = transate;
            this.fromLang = fromLang;
            this.toLang = toLang;
        }
    }
}
