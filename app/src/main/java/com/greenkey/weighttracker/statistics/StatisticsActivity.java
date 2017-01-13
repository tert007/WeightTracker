package com.greenkey.weighttracker.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.greenkey.weighttracker.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private float startWeight;
    private float desireWeight;
    private int weightUnitIndex;

    private String[] units;

    private final static int[] tabIcons = {
            R.drawable.ic_signs,
            R.drawable.ic_bars_chart
    };

    //private TextView weightUnitTextView;
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

        //desireWeightTextView = (TextView) findViewById(R.id.statistics_desire_weight_text_view);
        //desireWeightUnitTextView = (TextView) findViewById(R.id.statistics_desire_weight_unit_text_view);

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFrag(new StatisticsWeightRecordListFragment(), null);
        viewPagerAdapter.addFrag(new StatisticsChartFragment(), null);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.statistics_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.statistics_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        //SettingsManager.addOnChangeListener(this);
    }
/*
    @Override
    public void onChangeListener(float startWeight, float desireWeight, int weightUnitIndex) {
        Log.d("SETTINGS", "UPDATE_SETTINGS_STATISTICS");

        this.startWeight = startWeight;
        this.desireWeight = desireWeight;
        this.weightUnitIndex = weightUnitIndex;

        //desireWeightTextView.setText(WeightHelper.convertByString(desireWeight, weightUnitIndex));
        //desireWeightUnitTextView.setText(units[weightUnitIndex]);
        //weightUnitTextView.setText(units[weightUnitIndex]);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statisctics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.add: {

            }
            return true;
            default:*/
                return super.onOptionsItemSelected(item);
        //}
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
