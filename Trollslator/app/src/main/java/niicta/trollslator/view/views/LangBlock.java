package niicta.trollslator.view.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import niicta.trollslator.R;

/**
 * Created by niict on 05.04.2017.
 */

public class LangBlock extends LinearLayout {
    Context context;
    Spinner spinnerFrom;
    Spinner spinnerTo;
    ProgressBar progressBar;
    TextView switchView;
    ArrayAdapter<String> emptyAdapter;

    private boolean fromSpinnerWasSwitched;
    private boolean toSpinnerWasSwitched;

    public LangBlock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.head_spinners, this, true);
        String[] strings = new String[] {};
        List<String> stub = new ArrayList<>(Arrays.asList(strings));
        emptyAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stub);

        initViews();
    }

    private void switchSpinnersItems(){
        String fromItem = (String) spinnerFrom.getSelectedItem();
        String toItem = (String) spinnerTo.getSelectedItem();
        spinnerFrom.setSelection(getIndex(spinnerTo, toItem));
        spinnerTo.setSelection(getIndex(spinnerFrom, fromItem));
    }

    public void setFromSpinnerItem(String key){
        spinnerFrom.setSelection(getIndex(spinnerFrom, key));
    }

    private int getIndex(Spinner spinner, String s){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(s)) {
                index = i;
                break;
            }

        }
        return index;
    }

    private void initViews(){
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        progressBar = (ProgressBar) findViewById(R.id.spinnersProgressBar);
        switchView = (TextView) findViewById(R.id.switchTextView);
        switchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSpinnersItems();
            }
        });
    }

    public void loading(boolean loading){
        if (loading){
            progressBar.setVisibility(View.VISIBLE);
            switchView.setVisibility(View.GONE);
            spinnerFrom.setAdapter(emptyAdapter);
            spinnerTo.setAdapter(emptyAdapter);
        }
        else{
            progressBar.setVisibility(View.GONE);
            switchView.setVisibility(View.VISIBLE);
        }
    }

    public void setToSpinnerEnabled(boolean enabled){
        spinnerTo.setEnabled(enabled);
    }

    public boolean isFromSpinnerWasSwitched() {
        return fromSpinnerWasSwitched;
    }

    public void setFromSpinnerWasSwitched(boolean fromSpinnerWasSwitched) {
        this.fromSpinnerWasSwitched = fromSpinnerWasSwitched;
    }

    public boolean isToSpinnerWasSwitched() {
        return toSpinnerWasSwitched;
    }

    public void setToSpinnerWasSwitched(boolean toSpinnerWasSwitched) {
        this.toSpinnerWasSwitched = toSpinnerWasSwitched;
    }

    public void setOnFromItemSelectedListener(AdapterView.OnItemSelectedListener l){
        spinnerFrom.setOnItemSelectedListener(l);
    }

    public void setOnToItemSelectedListener(AdapterView.OnItemSelectedListener l){
        spinnerTo.setOnItemSelectedListener(l);
    }

    public void setFromAdapter(SpinnerAdapter adapter){
        spinnerFrom.setAdapter(adapter);
    }

    public void setToAdapter(SpinnerAdapter adapter){
        spinnerTo.setAdapter(adapter);
    }
}
