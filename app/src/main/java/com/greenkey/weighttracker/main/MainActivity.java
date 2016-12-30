package com.greenkey.weighttracker.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.WeightHelper;
import com.greenkey.weighttracker.WeightRecord;
import com.greenkey.weighttracker.app.CircularProgressBar;
import com.greenkey.weighttracker.settings.SettingsActivity;
import com.greenkey.weighttracker.statistics.StatisticsActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SettingsManager.SettingsObserver{

    private static final String NOT_INITIALIZED_VALUE = "--";

    private Realm realm;
    private CircularProgressBar circularProgressBar;

    private float desireWeight;
    private int weightUnitIndex;

    private WeightRecord currentWeightRecord;
    private WeightRecord firstWeightRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        circularProgressBar = (CircularProgressBar)findViewById(R.id.main_current_weight_progress_bar);
        circularProgressBar.setTextColor(ContextCompat.getColor(this,R.color.grey));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        final Button updateCurrentWeightButton = (Button) findViewById(R.id.main_update_current_weight_button);
        updateCurrentWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.set_current_weight);

                final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.set_weight_dialog, null);

                final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_weight_dialog_first_number_picker);
                final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_weight_dialog_second_number_pickrer);

                firstNumberPicker.setMinValue(1);
                firstNumberPicker.setMaxValue(999);

                secondNumberPicker.setMinValue(0);
                secondNumberPicker.setMaxValue(9);

                if (currentWeightRecord != null) {
                    float convertedValue = WeightHelper.convert(currentWeightRecord.getValue(), weightUnitIndex);

                    int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
                    int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

                    firstNumberPicker.setValue(firstPartOfValue);
                    secondNumberPicker.setValue(secondPartOfValue);
                }

                builder.setView(setCurrentWeightView);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        realm.beginTransaction();
                        WeightRecord weightRecord = realm.createObject(WeightRecord.class);

                        String value = firstNumberPicker.getValue() + "." + secondNumberPicker.getValue();

                        float reconvertedValue = WeightHelper.reconvert(Float.valueOf(value), weightUnitIndex);
                        weightRecord.setValue(reconvertedValue);

                        weightRecord.setDate(System.currentTimeMillis());

                        realm.copyToRealm(weightRecord);
                        realm.commitTransaction();

                        currentWeightRecord = weightRecord;
                        if (firstWeightRecord == null) {
                            firstWeightRecord = weightRecord;
                        }

                        updateProgressBar();
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

        SettingsManager.subscribe(this);
    }

    @Override
    public void update(float desireWeight, int weightUnitIndex) {
        Log.d("SETTINGS", "UPDATE_SETTINGS_MAIN");

        this.desireWeight = desireWeight;
        this.weightUnitIndex = weightUnitIndex;

        updateProgressBar();
    }

    private void updateProgressBar() {
        String[] units = getResources().getStringArray(R.array.weight_units_short_name);

        realm = Realm.getDefaultInstance();

        final RealmResults<WeightRecord> records = realm.where(WeightRecord.class).findAll();
        if ( ! records.isEmpty()) {
            firstWeightRecord = records.first();
            currentWeightRecord = records.last();
        } else {
            firstWeightRecord = null;
            currentWeightRecord = null;
        }

        ////sdfsdf
        //SettingsManager.setGoalWeight(30);

        if (currentWeightRecord != null && firstWeightRecord != null) {
            //currentWeightTextView.setText(WeightHelper.convertByString(currentWeightRecord.getValue(), weightUnitIndex));
            float firstDiff = Math.abs(SettingsManager.getGoalWeight() - firstWeightRecord.getValue());
            float currDiff = Math.abs(SettingsManager.getGoalWeight() - currentWeightRecord.getValue());

            if(currentWeightRecord.getValue() > SettingsManager.getGoalWeight() && currentWeightRecord.getValue() > firstWeightRecord.getValue()){
                circularProgressBar.setProgress(100);
                circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
            }
            else if(currentWeightRecord.getValue() < SettingsManager.getGoalWeight() && currentWeightRecord.getValue() < firstWeightRecord.getValue()){
                circularProgressBar.setProgress(100);
                circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
            }
            else {
                float progress = 100 - ((currDiff / firstDiff) * 100);
                if(progress > 0){
                    circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
                }
                else{
                    circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.reject_red));
                }
                circularProgressBar.setProgress((int)progress);
            }

            circularProgressBar.setMainText(WeightHelper.convertByString(currentWeightRecord.getValue(), weightUnitIndex));
            circularProgressBar.setAdditionalText(units[weightUnitIndex]);
        } else {
            circularProgressBar.setMainText(NOT_INITIALIZED_VALUE);
            circularProgressBar.setAdditionalText(units[weightUnitIndex]);
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
