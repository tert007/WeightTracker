package com.greenkey.weighttracker.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.WeightRecord;
import com.greenkey.weighttracker.app.CircularProgressBar;
import com.greenkey.weighttracker.settings.SettingsActivity;
import com.greenkey.weighttracker.statistics.StatisticsActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String NOT_INITIALIZED_VALUE = "--";

    private Realm realm;
    private CircularProgressBar circularProgressBar;
    private TextView currentWeightTextView;
    private TextView weightUnitTextView;
    private WeightRecord currentWeightRecord;
    private WeightRecord firstWeightRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        circularProgressBar = (CircularProgressBar)findViewById(R.id.main_current_weight_progress_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        final RealmResults<WeightRecord> records = realm.where(WeightRecord.class).findAll();
        if (! records.isEmpty()) {
            firstWeightRecord = records.first();
            currentWeightRecord = records.last();
        } else {
            currentWeightRecord = null;
        }

        currentWeightTextView = (TextView) findViewById(R.id.main_current_weight_text_view);
        weightUnitTextView = (TextView) findViewById(R.id.main_weight_unit_text_view);

        final Button updateCurrentWeightButton = (Button) findViewById(R.id.main_update_current_weight_button);
        updateCurrentWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.set_current_weight);

                final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.set_current_weight_dialog, null);

                final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_current_weight_dialog_first_number_picker);
                final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_current_weight_dialog_second_number_pickrer);

                firstNumberPicker.setMinValue(1);
                firstNumberPicker.setMaxValue(999);

                secondNumberPicker.setMinValue(0);
                secondNumberPicker.setMaxValue(9);

                if (currentWeightRecord != null) {
                    firstNumberPicker.setValue(currentWeightRecord.getFistPartOfValue());
                    secondNumberPicker.setValue(currentWeightRecord.getSecondPartOfValue());
                }

                builder.setView(setCurrentWeightView);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        realm.beginTransaction();
                        WeightRecord weightRecord = realm.createObject(WeightRecord.class);

                        String value = firstNumberPicker.getValue() + "." + secondNumberPicker.getValue();
                        weightRecord.setValue(Float.valueOf(value));

                        weightRecord.setDate(System.currentTimeMillis());

                        realm.copyToRealm(weightRecord);
                        realm.commitTransaction();

                        currentWeightRecord = weightRecord;
                        currentWeightTextView.setText(currentWeightRecord.getValueByString());
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsManager.setGoalWeight(30);
        int index = SettingsManager.getWeightUnitIndex();
        String[] units = getResources().getStringArray(R.array.weight_units_short_name);
        if (currentWeightRecord != null) {
            float firstDiff = Math.abs(SettingsManager.getGoalWeight() - firstWeightRecord.getValue());
            float currDiff = Math.abs(SettingsManager.getGoalWeight() - currentWeightRecord.getValue());
            float progress = 100 - ((currDiff / firstDiff) * 100);
            if(progress > 0){
                circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
            }
            else{
                circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.reject_red));
            }
            circularProgressBar.setProgress((int)progress);
            circularProgressBar.setMainText(currentWeightRecord.getValueByString());
            circularProgressBar.setAdditionalText(units[index]);
        } else {
            currentWeightTextView.setText(NOT_INITIALIZED_VALUE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maim_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.statistics:
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
