package com.greenkey.weighttracker.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final View weightUnitView = findViewById(R.id.settings_weight_unit_view);
        final AppCompatSpinner spinner = (AppCompatSpinner) weightUnitView.findViewById(R.id.settings_weight_unit_spinner);

        spinner.setSelection(SettingsManager.getWeightUnitIndex());

        weightUnitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SettingsManager.setWeightUnitIndex(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
