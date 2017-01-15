package com.greenkey.weighttracker.statistics;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;
import android.widget.NumberPicker;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.WeightRecord;
import com.greenkey.weighttracker.entity.helper.WeightHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class StatisticsActivity extends AppCompatActivity implements OnDataChangedListener, UpdateListener {

    List<OnDataChangedListener> listeners = new ArrayList<>();

    private final static int[] tabIcons = {
            R.drawable.ic_signs,
            R.drawable.ic_bars_chart
    };

    private int weightUnitIndex;

    private Realm realm;
    private RealmResults<WeightRecord> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        weightUnitIndex = SettingsManager.getWeightUnitIndex();

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(WeightRecord.class).findAll();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.statistics);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.statistics_floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewWeightDialog();
            }
        });

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFrag(new StatisticsWeightRecordListFragment(), null);
        viewPagerAdapter.addFrag(new StatisticsChartFragment(), null);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.statistics_view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.statistics_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
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

    private void showAddNewWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsActivity.this);

        final LayoutInflater inflater = LayoutInflater.from(StatisticsActivity.this);
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_add_new_weight, null);

        final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        WeightRecord currentWeight = null;
        if ( ! realmResults.isEmpty()) {
            currentWeight = realmResults.first();
        }

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
                firstNumberPicker.clearFocus();
                secondNumberPicker.clearFocus();

                realm.beginTransaction();

                WeightRecord weightRecord = realm.createObject(WeightRecord.class);

                String value = firstNumberPicker.getValue() + "." + secondNumberPicker.getValue();

                float reconvertedValue = WeightHelper.reconvert(Float.valueOf(value), weightUnitIndex);

                weightRecord.setValue(reconvertedValue);
                weightRecord.setDate(System.currentTimeMillis());

                realm.copyToRealm(weightRecord);
                realm.commitTransaction();

                notifyListeners(realmResults);

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

    /*
      Обзваниваем всех подписчиков
    */
    private void notifyListeners(RealmResults<WeightRecord> realmResults) {
        for (OnDataChangedListener listener : listeners) {
            if (listener == null) {
                continue;
            }

            listener.onDataChangedListener(realmResults);
        }
    }

    /*
       Фрагмент присылает обновления нам, а мы уже всем подписчикам
    */
    @Override
    public void onDataChangedListener(RealmResults<WeightRecord> realmResults) {
        this.realmResults = realmResults;
        notifyListeners(realmResults);
    }

    /*
        Подписки всех фрагметов
    */
    @Override
    public void addOnDataChangeListener(OnDataChangedListener listener) {
        listener.onDataChangedListener(realmResults);
        listeners.add(listener);
    }
}
