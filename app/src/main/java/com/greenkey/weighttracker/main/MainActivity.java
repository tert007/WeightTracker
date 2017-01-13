package com.greenkey.weighttracker.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.helper.WeightHelper;
import com.greenkey.weighttracker.entity.WeightRecord;
import com.greenkey.weighttracker.app.view.CircularProgressBar;
import com.greenkey.weighttracker.settings.SettingsActivity;
import com.greenkey.weighttracker.statistics.StatisticsActivity;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity /*implements SettingsManager.SettingsListener*/ {

    private static final String NOT_INITIALIZED_VALUE = "--";

    private Realm realm;
    private String[] units;

    private View parentView;

    private FloatingActionButton floatingActionButton;
    private CircularProgressBar circularProgressBar;

    private TextView desireWeightTextView;
    private TextView desireWeightUnitTextView;

    private TextView startWeightTextView;
    private TextView startWeightUnitTextView;

    private TextView currentWeightTextView;
    private TextView currentWeightUnitTextView;

    private float startWeight;
    private float desireWeight;
    private int weightUnitIndex;

    private WeightRecord currentWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        startWeight = SettingsManager.getStartWeight();
        desireWeight = SettingsManager.getDesireWeight();
        weightUnitIndex = SettingsManager.getWeightUnitIndex();

        units = getResources().getStringArray(R.array.weight_units_short_name);

        realm = Realm.getDefaultInstance();

        parentView = findViewById(android.R.id.content);

        circularProgressBar = (CircularProgressBar)findViewById(R.id.circular_progress_bar);
        circularProgressBar.setProgressWidth(15);
        circularProgressBar.showPrimaryText(true);
        circularProgressBar.showSecondaryText(true);
        circularProgressBar.setProgressColor(ContextCompat.getColor(this, R.color.accept_green));

        desireWeightTextView = (TextView) findViewById(R.id.main_desire_weight_text_view);
        desireWeightUnitTextView = (TextView) findViewById(R.id.main_desire_weight_unit_text_view);

        startWeightTextView = (TextView) findViewById(R.id.main_started_weight_text_view);
        startWeightUnitTextView = (TextView) findViewById(R.id.main_started_weight_unit_text_view);

        currentWeightTextView = (TextView) findViewById(R.id.main_current_weight_text_view);
        currentWeightUnitTextView = (TextView) findViewById(R.id.main_current_weight_unit_text_view);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.main_floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewWeightDialog();
            }
        });

        final View currentWeightView = findViewById(R.id.main_current_weight_view);
        currentWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWeight == null) {
                    Snackbar.make(parentView, R.string.edit_current_weight_error, Snackbar.LENGTH_LONG)
                            .setAction(R.string.add, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showAddNewWeightDialog();
                                }
                            }).show();
                } else {
                    showEditCurrentWeightDialog();
                }
            }
        });

        final View desireWeightView = findViewById(R.id.main_desire_weight_view);
        desireWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetDesiredWeightDialog();
            }
        });

        final View startWeightView = findViewById(R.id.main_started_weight_view);
        startWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetStartWeightDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RealmResults<WeightRecord> realmResults = realm.where(WeightRecord.class).findAll();
        if ( ! realmResults.isEmpty()) {
            currentWeight = realmResults.last();
        }

        int tempWeightUnitIndex = SettingsManager.getWeightUnitIndex();
        if (weightUnitIndex != tempWeightUnitIndex) {
            weightUnitIndex = tempWeightUnitIndex;
        }

        updateUI();
    }

    private void showSetDesiredWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_set_desire_weight, null);

        final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.second_number_picker);

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
                desireWeight = WeightHelper.reconvert(value, weightUnitIndex);

                SettingsManager.setDesireWeight(desireWeight);

                updateUI();

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

    private void showCongratulationsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_congratulations, null);

        final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.second_number_picker);

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

                desireWeight = WeightHelper.reconvert(value, weightUnitIndex);
                startWeight = currentWeight.getValue();

                SettingsManager.setDesireWeight(desireWeight);
                SettingsManager.setStartWeight(startWeight);

                updateUI();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateUI();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSetStartWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_set_start_weight, null);

        final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        final float convertedValue = WeightHelper.convert(startWeight, weightUnitIndex);

        final int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
        final int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

        firstNumberPicker.setValue(firstPartOfValue);
        secondNumberPicker.setValue(secondPartOfValue);

        builder.setView(setCurrentWeightView);

        builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                final float value = Float.valueOf(firstNumberPicker.getValue() + "." + secondNumberPicker.getValue());
                startWeight = WeightHelper.reconvert(value, weightUnitIndex);

                SettingsManager.setStartWeight(startWeight);

                updateUI();

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

    private void showAddNewWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_add_new_weight, null);

        final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        if (currentWeight != null) {
            float convertedValue = WeightHelper.convert(currentWeight.getValue(), weightUnitIndex);

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

                currentWeight = weightRecord;

                realm.commitTransaction();


                if (startWeight < desireWeight) { //Набор веса
                    if (currentWeight.getValue() >= desireWeight) {
                        showCongratulationsDialog();
                    } else {
                        updateUI();
                    }
                } else if (startWeight == desireWeight) {
                    updateUI();
                } else if (startWeight > desireWeight) {
                    if (currentWeight.getValue() <= desireWeight) {
                        showCongratulationsDialog();
                    } else {
                        updateUI();
                    }
                }

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

    private void showEditCurrentWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_edit_current_weight, null);

        final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        //if (currentWeight != null) {
            final float convertedValue = WeightHelper.convert(currentWeight.getValue(), weightUnitIndex);

            final int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
            final int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

            firstNumberPicker.setValue(firstPartOfValue);
            secondNumberPicker.setValue(secondPartOfValue);
        //}

        builder.setView(setCurrentWeightView);

        builder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                final float value = Float.valueOf(firstNumberPicker.getValue() + "." + secondNumberPicker.getValue());
                final float reconvertedValue = WeightHelper.reconvert(value, weightUnitIndex);

                realm.beginTransaction();
                currentWeight.setValue(reconvertedValue);
                realm.commitTransaction();

                dialog.dismiss();

                if (startWeight < desireWeight) { //Набор веса
                    if (currentWeight.getValue() > desireWeight) {
                        showCongratulationsDialog();
                    } else {
                        updateUI();
                    }

                } else if (startWeight > desireWeight) {
                    if (currentWeight.getValue() < desireWeight) {
                        showCongratulationsDialog();
                    } else {
                        updateUI();
                    }
                }
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

    private void updateUI() {
        desireWeightTextView.setText(WeightHelper.convertByString(desireWeight, weightUnitIndex));
        desireWeightUnitTextView.setText(units[weightUnitIndex]);

        startWeightTextView.setText(WeightHelper.convertByString(startWeight, weightUnitIndex));
        startWeightUnitTextView.setText(units[weightUnitIndex]);

        if (currentWeight == null) {
            currentWeightTextView.setText(NOT_INITIALIZED_VALUE);
        } else {
            currentWeightTextView.setText(WeightHelper.convertByString(currentWeight.getValue(), weightUnitIndex));
        }

        currentWeightUnitTextView.setText(units[weightUnitIndex]);

        float maxProgress = 0;
        float progress = 0;

        float currentBalance = 0;

        if (currentWeight == null) {
            circularProgressBar.setPrimaryText(NOT_INITIALIZED_VALUE);
            circularProgressBar.setSecondaryText(units[weightUnitIndex]);
        } else {
            if (startWeight < desireWeight) {
                maxProgress = desireWeight - startWeight;
                progress = currentWeight.getValue() - startWeight;

                currentBalance = Math.abs(desireWeight - currentWeight.getValue());
            } else if (startWeight == desireWeight) {
                currentBalance = Math.abs(currentWeight.getValue() - desireWeight);
            } else if (startWeight > desireWeight) {
                maxProgress = startWeight - desireWeight;
                progress = startWeight - currentWeight.getValue();

                currentBalance = Math.abs(currentWeight.getValue() - desireWeight);
            }

            circularProgressBar.setMaxProgress(maxProgress);
            circularProgressBar.setProgress(progress);

            circularProgressBar.setPrimaryText(WeightHelper.convertByString(currentBalance, weightUnitIndex));
            circularProgressBar.setSecondaryText(units[weightUnitIndex]);
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
