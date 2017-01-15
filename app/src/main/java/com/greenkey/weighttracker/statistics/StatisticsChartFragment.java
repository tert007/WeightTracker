package com.greenkey.weighttracker.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.WeightRecord;
import com.greenkey.weighttracker.entity.helper.WeightHelper;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Alexander on 28.12.2016.
 */
public class StatisticsChartFragment extends Fragment {

    private RealmResults realmResults;
    private int weightIndex;
    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_chart_fragment, container, false);
        final View emptyListView = view.findViewById(R.id.statistics_empty_chart_view);

        final ValueLineChart mCubicValueLineChart = (ValueLineChart) view.findViewById(R.id.chart);

        weightIndex = SettingsManager.getWeightUnitIndex();

        realm = Realm.getDefaultInstance();

        realmResults = realm.where(WeightRecord.class).findAll().sort("date", Sort.ASCENDING);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                mCubicValueLineChart.clearChart();

                realmResults = realm.where(WeightRecord.class).findAll().sort("date", Sort.ASCENDING);

                Iterator iterator = realmResults.iterator();
                ValueLineSeries series = new ValueLineSeries();
                series.setColor(0xFF56B7F1);

                while (iterator.hasNext()) {
                    WeightRecord weightRecord = (WeightRecord) iterator.next();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                    series.addPoint(new ValueLinePoint(simpleDateFormat.format(weightRecord.getDate()), WeightHelper.convert(weightRecord.getValue(), weightIndex)));
                }

                mCubicValueLineChart.addSeries(series);
                mCubicValueLineChart.startAnimation();
            }
        });

        if (realmResults.size() <= 1) {
            emptyListView.setVisibility(View.VISIBLE);
            return view;
        }

        Iterator iterator = realmResults.iterator();
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        while (iterator.hasNext()) {
            WeightRecord weightRecord = (WeightRecord) iterator.next();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            series.addPoint(new ValueLinePoint(simpleDateFormat.format(weightRecord.getDate()), WeightHelper.convert(weightRecord.getValue(), weightIndex)));
        }

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();

        return view;
    }
}
