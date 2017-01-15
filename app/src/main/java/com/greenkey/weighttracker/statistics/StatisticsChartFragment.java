package com.greenkey.weighttracker.statistics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.WeightRecord;
import com.greenkey.weighttracker.entity.helper.WeightHelper;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Alexander on 28.12.2016.
 */
public class StatisticsChartFragment extends Fragment implements OnDataChangedListener {

    private int weightIndex;
    private RealmResults realmResults;

    private View emptyChartView;
    private ValueLineChart chart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weightIndex = SettingsManager.getWeightUnitIndex();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity instanceof UpdateListener) {
            ((UpdateListener) activity).addOnDataChangeListener(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_chart_fragment, container, false);

        chart = (ValueLineChart) view.findViewById(R.id.chart);
        emptyChartView = view.findViewById(R.id.statistics_empty_chart_view);

        return view;
    }

    @Override
    public void onDataChangedListener(RealmResults<WeightRecord> realmResults) {
        if (realmResults == null) {
            return;
        }

        this.realmResults = realmResults.sort("date", Sort.ASCENDING);

        updateUI();
    }

    private void updateUI(){
        if (realmResults.size() <= 1) {
            emptyChartView.setVisibility(View.VISIBLE);
            return;
        } else {
            emptyChartView.setVisibility(View.GONE);
        }

        chart.clearChart();

        Iterator iterator = realmResults.iterator();
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

        while (iterator.hasNext()) {
            WeightRecord weightRecord = (WeightRecord) iterator.next();
            series.addPoint(new ValueLinePoint(simpleDateFormat.format(weightRecord.getDate()), WeightHelper.convert(weightRecord.getValue(), weightIndex)));
        }

        chart.addSeries(series);
        chart.startAnimation();
    }
}
