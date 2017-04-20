package niicta.trollslator.view.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import niicta.trollslator.R;

/**
 * Created by niict on 06.04.2017.
 */

public class CustomArrayAdapter<T> extends ArrayAdapter {

    private final int mResource;
    private final Context mContext;

    public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        mResource = resource;
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        }

        String s = (String) getItem(position);
        ((TextView) convertView.findViewById(R.id.history_lang)).setText(s);


        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.drop_down_spinner_item, parent, false);
        }

        String s = (String) getItem(position);
        ((TextView) convertView.findViewById(R.id.history_lang)).setText(s);


        return convertView;
    }
}
