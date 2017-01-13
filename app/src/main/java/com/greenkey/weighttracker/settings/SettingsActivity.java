package com.greenkey.weighttracker.settings;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;

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
        final AppCompatSpinner weightUnitspinner = (AppCompatSpinner) weightUnitView.findViewById(R.id.settings_weight_unit_spinner);

        weightUnitspinner.setSelection(SettingsManager.getWeightUnitIndex());

        weightUnitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightUnitspinner.performClick();
            }
        });

        weightUnitspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SettingsManager.setWeightUnitIndex(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final View sexView = findViewById(R.id.settings_sex_view);
        final ImageView sexImageView = (ImageView) sexView.findViewById(R.id.settings_sex_image_view);

        final Drawable maleIcon = ContextCompat.getDrawable(this, R.drawable.settings_sex_male_icon);
        final Drawable femaleIcon = ContextCompat.getDrawable(this, R.drawable.settings_sex_female_icon);

        final boolean isMale = true;

        if (isMale) {
            sexImageView.setImageDrawable(maleIcon);
        } else {
            sexImageView.setImageDrawable(femaleIcon);
        }

        sexView.setOnClickListener(new View.OnClickListener() {

            private boolean isMal = isMale;

            @Override
            public void onClick(View v) {
                isMal = !isMal;

                if (isMal) {
                    sexImageView.setImageDrawable(maleIcon);
                } else {
                    sexImageView.setImageDrawable(femaleIcon);
                }

                //SettingsMenager.setIsMale (isMal)
            }
        });
    }

}
