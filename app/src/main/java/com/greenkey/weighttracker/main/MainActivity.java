package com.greenkey.weighttracker.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.settings.SettingsActivity;
import com.greenkey.weighttracker.statistics.StatisticsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        SettingsManager.init(this);

        final Button addWeightButton = (Button) findViewById(R.id.main_add_weight_record_button);
        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.set_current_weight);

                final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.set_current_weight_dialog, null);

                final NumberPicker numberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_current_weight_dialog_number_picker);

                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(999);

                builder.setView(setCurrentWeightView);

                builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();            }
        });
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
}
