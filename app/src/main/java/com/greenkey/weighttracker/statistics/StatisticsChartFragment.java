package com.greenkey.weighttracker.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.entity.WeightRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
//if (true)
        //return view;
        /*Realm realm = Realm.getDefaultInstance();

        RealmResults realmResults = realm.where(WeightRecord.class).findAll();
        if (realmResults.isEmpty()) {
            return view;
        }
*/
        chart = (LineChart) view.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        setData(15, 15);

        chart.invalidate();
/*
        //Iterator iterator = realmResults.iterator();
        //while (iterator.hasNext()) {
            //WeightRecord weightRecord = (WeightRecord) iterator.next();
            entries.add(new Entry(15, 15));
        //}
        entries.add(new Entry(25, 35));
        entries.add(new Entry(45, 15));
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        //dataSet.setColor(...);
        //dataSet.setValueTextColor(...); // styling, ...

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
*/
        return view;
    }

    private void setData(int count, float range) {

        // now in hours
        long now = 34;//TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        ArrayList<Entry> values = new ArrayList<Entry>();

        float from = now;

        // count = hours
        float to = now + count;

        Random random = new Random();

        // increment by 1 hour
        for (float x = from; x < to; x++) {

            float y = random.nextFloat() + 10;
            values.add(new Entry(x, y)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }
}
