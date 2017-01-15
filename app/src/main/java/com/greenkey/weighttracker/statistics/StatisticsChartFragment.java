package com.greenkey.weighttracker.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.entity.WeightRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Alexander on 28.12.2016.
 */
public class StatisticsChartFragment extends Fragment {

    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_chart_fragment, container, false);

        Realm realm = Realm.getDefaultInstance();
        RealmResults realmResults = realm.where(WeightRecord.class).findAll();

        if (realmResults.isEmpty()) {
            return view;
        }

        chart = (LineChart) view.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        Iterator iterator = realmResults.iterator();

        while (iterator.hasNext()) {
            WeightRecord weightRecord = (WeightRecord) iterator.next();
            entries.add(new Entry(weightRecord.getDate(), weightRecord.getValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");


        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(24f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.getDefault());

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = (long)value;
                return mFormat.format(new Date(millis));
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        return view;
    }
}
