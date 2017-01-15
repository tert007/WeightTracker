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
import com.greenkey.weighttracker.entity.Unit;
import com.greenkey.weighttracker.entity.helper.TallHelper;
import com.greenkey.weighttracker.entity.helper.WeightHelper;
import com.stepstone.stepper.VerificationError;

public class SettingsActivity extends AppCompatActivity {

    private Unit unit;

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

        final View unitSystemView = findViewById(R.id.settings_unit_system_view);
        final ImageView unitSystemImageView = (ImageView) unitSystemView.findViewById(R.id.settings_unit_system_image_view);

        unit = SettingsManager.getUnit();

        switch (unit) {
            case ENGLISH:
                unitSystemImageView.setImageResource(R.drawable.settings_unit_british_sytem_icon);
                break;
            case METRIC:
                unitSystemImageView.setImageResource(R.drawable.settings_unit_metric_system_icon);
                break;
        }

        unitSystemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (unit == Unit.ENGLISH) {
                    unit = Unit.METRIC;

                    unitSystemImageView.setImageResource(R.drawable.settings_unit_metric_system_icon);
                } else {
                    unit = Unit.ENGLISH;

                    unitSystemImageView.setImageResource(R.drawable.settings_unit_british_sytem_icon);
                }

                switch (unit) {
                    case ENGLISH:
                        SettingsManager.setUnit(unit);
                        SettingsManager.setWeightUnitIndex(WeightHelper.ENGLISH_SYSTEM_INDEX);
                        SettingsManager.setTallUnitIndex(TallHelper.ENGLISH_SYSTEM_INDEX);
                        break;
                    case METRIC:
                        SettingsManager.setUnit(unit);
                        SettingsManager.setWeightUnitIndex(WeightHelper.METRIC_SYSTEM_INDEX);
                        SettingsManager.setTallUnitIndex(TallHelper.METRIC_SYSTEM_INDEX);
                        break;
                }
            }
        });
    }
}
