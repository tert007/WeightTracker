package com.greenkey.weighttracker.statistics;

import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.WeightRecord;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class StatisticsActivity extends AppCompatActivity {

    private Realm realm;
    private WeightRecord lastWeightRecord;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final static int[] tabIcons = {
            R.drawable.test_1,
            R.drawable.test_2
    };

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

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFrag(new StatisticsWeightRecordListFragment(), null);
        viewPagerAdapter.addFrag(new StatisticsChartFragment(), null);

        viewPager = (ViewPager) findViewById(R.id.statistics_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statisctics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                final Button updateCurrentWeightButton = (Button) findViewById(R.id.main_update_current_weight_button);
                updateCurrentWeightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsActivity.this);
                        builder.setTitle(R.string.set_current_weight);

                        final LayoutInflater inflater = LayoutInflater.from(StatisticsActivity.this);
                        final View setCurrentWeightView = inflater.inflate(R.layout.set_current_weight_dialog, null);

                        final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_current_weight_dialog_first_number_picker);
                        final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_current_weight_dialog_second_number_pickrer);

                        firstNumberPicker.setMinValue(1);
                        firstNumberPicker.setMaxValue(999);

                        secondNumberPicker.setMinValue(0);
                        secondNumberPicker.setMaxValue(9);

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

                                lastWeightRecord = weightRecord;
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
