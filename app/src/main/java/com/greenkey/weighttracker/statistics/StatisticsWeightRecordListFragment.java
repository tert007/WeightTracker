package com.greenkey.weighttracker.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.WeightRecord;

import java.util.Collections;
import java.util.List;

/**
 * Created by Alexander on 28.12.2016.
 */
public class StatisticsWeightRecordListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_list_view_fragment, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.statistics_list_view);

        View emptyView = view.findViewById(R.id.statistics_empty_list_view);
        listView.setEmptyView(emptyView);

        List<WeightRecord> asda = Collections.emptyList();
        ReaderLibraryListViewAdapter a = new ReaderLibraryListViewAdapter(getActivity(), R.layout.statistics_list_view_item, asda);

        listView.setAdapter(a);

        return view;
    }

    public class ReaderLibraryListViewAdapter extends ArrayAdapter<WeightRecord> {

        private Context context;
        private int resourceId;
        private List<WeightRecord> items;

        public ReaderLibraryListViewAdapter(Context context, int resourceId, List<WeightRecord> items) {
            super(context, resourceId, items);

            this.context = context;
            this.resourceId = resourceId;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(resourceId, parent, false);
            }

            return convertView;
        }
    }
}
