package com.greenkey.weighttracker.statistics;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements SettingsManager.SettingsObserver {

    private float desireWeight;
    private int weightUnitIndex;

    private String[] units;

    private final static int[] tabIcons = {
            R.drawable.test_1,
            R.drawable.test_2
    };

    private TextView goalWeightTextView;
    private TextView weightUnitTextView;
    //private StatisticsWeightRecordListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.statistics);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        units = getResources().getStringArray(R.array.weight_units_short_name);

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFrag(new StatisticsWeightRecordListFragment(), null);
        viewPagerAdapter.addFrag(new StatisticsChartFragment(), null);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.statistics_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        goalWeightTextView = (TextView) findViewById(R.id.statistics_goal_weight_text_view);
        weightUnitTextView = (TextView) findViewById(R.id.statistics_weight_unite_text_view);

        final View goalWeightEditImageView = findViewById(R.id.statistics_goal_weight_edit_image_view);
        goalWeightEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsActivity.this);
                builder.setTitle(R.string.set_desired_weight);

                final LayoutInflater inflater = LayoutInflater.from(StatisticsActivity.this);
                final View setCurrentWeightView = inflater.inflate(R.layout.set_weight_dialog, null);

                final NumberPicker firstNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.set_weight_dialog_first_number_picker);
                final NumberPicker secondNumberPicker = (NumberPicker) setCurrentWeightView.findViewById(R.id.set_weight_dialog_second_number_pickrer);

                firstNumberPicker.setMinValue(1);
                firstNumberPicker.setMaxValue(999);

                secondNumberPicker.setMinValue(0);
                secondNumberPicker.setMaxValue(9);

                final float currentGoalWeight = SettingsManager.getGoalWeight();
                final float convertedValue = WeightHelper.convert(currentGoalWeight, weightUnitIndex);

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

        SettingsManager.subscribe(this);
    }

    @Override
    public void update(float desireWeight, int weightUnitIndex) {
        Log.d("SETTINGS", "UPDATE_SETTINGS_STATISTICS");

        this.desireWeight = desireWeight;
        this.weightUnitIndex = weightUnitIndex;


        goalWeightTextView.setText(WeightHelper.convertByString(desireWeight, weightUnitIndex));
        weightUnitTextView.setText(units[weightUnitIndex]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statisctics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {

            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
