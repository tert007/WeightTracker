package com.greenkey.weighttracker.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.WeightHelper;
import com.greenkey.weighttracker.WeightRecord;
import com.greenkey.weighttracker.app.CircularProgressBar;
import com.greenkey.weighttracker.settings.SettingsActivity;
import com.greenkey.weighttracker.statistics.StatisticsActivity;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements SettingsManager.SettingsObserver{

    private static final String NOT_INITIALIZED_VALUE = "--";

    private Realm realm;

    private String[] units;

    private CircularProgressBar circularProgressBar;

    private TextView desireWeightTextView;
    private TextView desireWeightUnitTextView;

    private TextView startWeightTextView;
    private TextView lastWeightTextView;
    //private TextView desireWeightTextView;

    private TextView startWeightUnitTextView;
    private TextView lastWeightUnitTextView;
    //private TextView desireWeightUnitTextView;

    private float desireWeight;
    private int weightUnitIndex;

    private WeightRecord lastWeightRecord;
    private WeightRecord firstWeightRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        units = getResources().getStringArray(R.array.weight_units_short_name);

        circularProgressBar = (CircularProgressBar)findViewById(R.id.circular_progress_bar);
        circularProgressBar.setProgressWidth(15);
        circularProgressBar.showPrimaryText(true);
        circularProgressBar.showSecondaryText(true);

        startWeightTextView = (TextView) findViewById(R.id.main_started_weight_text_view);
        lastWeightTextView = (TextView) findViewById(R.id.main_current_weight_text_view);
        desireWeightTextView = (TextView) findViewById(R.id.main_desire_weight_text_view);

        startWeightUnitTextView = (TextView) findViewById(R.id.main_started_weight_unit_text_view);
        lastWeightUnitTextView = (TextView) findViewById(R.id.main_current_weight_unit_text_view);
        desireWeightUnitTextView = (TextView) findViewById(R.id.main_desire_weight_unit_text_view);

        desireWeightTextView = (TextView) findViewById(R.id.main_desire_weight_text_view);
        desireWeightUnitTextView = (TextView) findViewById(R.id.main_desire_weight_unit_text_view);

        //circularProgressBar.setProgressColor(ContextCompat.getColor(this, R.color.reject_red));
        //circularProgressBar.setMaxProgress(4);
        //circularProgressBar.setProgress(0);
        //circularProgressBar.setTextColor(ContextCompat.getColor(this,R.color.grey));

        circularProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle(R.string.set_current_weight);

                final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.set_weight_dialog, null);

                final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.desire_weight_dialog_first_number_picker);
                final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.desire_weight_dialog_second_number_pickrer);

                firstNumberPicker.setMinValue(1);
                firstNumberPicker.setMaxValue(999);

                secondNumberPicker.setMinValue(0);
                secondNumberPicker.setMaxValue(9);

                if (lastWeightRecord != null) {
                    float convertedValue = WeightHelper.convert(lastWeightRecord.getValue(), weightUnitIndex);

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

                        lastWeightRecord = weightRecord;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        final View desireWeightView = findViewById(R.id.main_desire_weight_view);
        desireWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.desire_weight_dialog, null);

                final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.desire_weight_dialog_first_number_picker);
                final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.desire_weight_dialog_second_number_pickrer);

                firstNumberPicker.setMinValue(1);
                firstNumberPicker.setMaxValue(999);

                secondNumberPicker.setMinValue(0);
                secondNumberPicker.setMaxValue(9);

                final float convertedValue = WeightHelper.convert(desireWeight, weightUnitIndex);

                final int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
                final int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

                firstNumberPicker.setValue(firstPartOfValue);
                secondNumberPicker.setValue(secondPartOfValue);

                builder.setView(setCurrentWeightView);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final float value = Float.valueOf(firstNumberPicker.getValue() + "." + secondNumberPicker.getValue());
                        final float reconvertedValue = WeightHelper.reconvert(value, weightUnitIndex);

                        SettingsManager.setGoalWeight(reconvertedValue);
                        dialog.dismiss();
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

        final View startedWeightView = findViewById(R.id.main_started_weight_view);
        startedWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.started_weight_dialog, null);

                final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.started_weight_dialog_first_number_picker);
                final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.started_weight_dialog_second_number_pickrer);

                firstNumberPicker.setMinValue(1);
                firstNumberPicker.setMaxValue(999);

                secondNumberPicker.setMinValue(0);
                secondNumberPicker.setMaxValue(9);

                //final float convertedValue = WeightHelper.convert(desireWeight, weightUnitIndex);

                //final int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
                //final int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

                //firstNumberPicker.setValue(firstPartOfValue);
                //secondNumberPicker.setValue(secondPartOfValue);

                builder.setView(setCurrentWeightView);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
/*
                        final float value = Float.valueOf(firstNumberPicker.getValue() + "." + secondNumberPicker.getValue());
                        final float reconvertedValue = WeightHelper.reconvert(value, weightUnitIndex);

                        SettingsManager.setGoalWeight(reconvertedValue);
                        */
                        dialog.dismiss();
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

        //if (desireWeight == 0) {
        //    setDesireWeightDialog();
        //}
    }

    private void updateProgressBar() {

        desireWeightTextView.setText(WeightHelper.convertByString(desireWeight, weightUnitIndex));
        desireWeightUnitTextView.setText(units[weightUnitIndex]);



        startWeightUnitTextView.setText(units[weightUnitIndex]);
        lastWeightUnitTextView.setText(units[weightUnitIndex]);

        realm = Realm.getDefaultInstance();

/*
        final RealmResults<WeightRecord> records = realm.where(WeightRecord.class).findAll();
        if ( ! records.isEmpty()) {
            firstWeightRecord = records.first();
            lastWeightRecord = records.last();
        } else {
            firstWeightRecord = null;
            lastWeightRecord = null;
        }

        circularProgressBar.setSecondaryText(units[weightUnitIndex]);

        if (lastWeightRecord == null) {
            circularProgressBar.setPrimaryText(NOT_INITIALIZED_VALUE);
            //COLOR DEFAULT
        } else {
            float firstDiff = (Math.abs(desireWeight - firstWeightRecord.getValue()));
            float currDiff = (Math.abs(desireWeight - lastWeightRecord.getValue()));

            circularProgressBar.setMaxProgress((int) (firstDiff * 10));
            circularProgressBar.setProgress((int) (currDiff * 10));

            circularProgressBar.setPrimaryText(String.valueOf(desireWeight - lastWeightRecord.getValue()));
        }
/*
        if (lastWeightRecord != null && firstWeightRecord != null) {
            //currentWeightTextView.setText(WeightHelper.convertByString(lastWeightRecord.getValue(), weightUnitIndex));

            if(lastWeightRecord.getValue() > desireWeight && lastWeightRecord.getValue() > firstWeightRecord.getValue()){
                //circularProgressBar.setProgress(100);
                //circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
            }
            else if(lastWeightRecord.getValue() < desireWeight && lastWeightRecord.getValue() < firstWeightRecord.getValue()){
                //circularProgressBar.setProgress(100);
                //circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
            }
            else {
                float progress = 100 - ((currDiff / firstDiff) * 100);
                if(progress > 0){
                    //circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.accept_green));
                }
                else{
                    //circularProgressBar.setProgressColor(ContextCompat.getColor(this,R.color.reject_red));
                }
                //circularProgressBar.setProgress((int)progress);
            }

            //circularProgressBar.setPrimaryText(WeightHelper.convertByString(lastWeightRecord.getValue(), weightUnitIndex));
            //circularProgressBar.setSecondaryText(units[weightUnitIndex]);
        } else {
            //circularProgressBar.setPrimaryText(NOT_INITIALIZED_VALUE);
            //circularProgressBar.setSecondaryText(units[weightUnitIndex]);
        }*/
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
