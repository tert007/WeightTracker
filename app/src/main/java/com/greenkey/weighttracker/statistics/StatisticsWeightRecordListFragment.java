package com.greenkey.weighttracker.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.WeightHelper;
import com.greenkey.weighttracker.WeightRecord;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Alexander on 28.12.2016.
 */
public class StatisticsWeightRecordListFragment extends Fragment {

    private int weightUnitIndex;

    private Realm realm;
    private RealmResults<WeightRecord> realmResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_list_view_fragment, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.statistics_list_view);
        final View emptyView = view.findViewById(R.id.statistics_empty_list_view);

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(WeightRecord.class).findAll();

        final WeightListAdapter adapter = new WeightListAdapter(getActivity(), realmResults);

        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        weightUnitIndex = SettingsManager.getWeightUnitIndex();

        return view;
    }

    public class WeightListAdapter extends RealmBaseAdapter<WeightRecord> implements ListAdapter {

        public WeightListAdapter(Context context, OrderedRealmCollection<WeightRecord> realmResults) {
            super(context, realmResults);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(R.layout.statistics_list_view_item, parent, false);
            }

            WeightRecord weightRecord = getItem(position);
            if (weightRecord != null) {
                final TextView valueTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_value_text_view);
                valueTextView.setText(WeightHelper.convertByString(weightRecord.getValue(), weightUnitIndex));

                final TextView dateTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_date_text_view);
                dateTextView.setText(weightRecord.getDateByString());
            }

            final View remove = convertView.findViewById(R.id.statistics_list_view_item_remove_image_view);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realm.beginTransaction();
                    realmResults.deleteFromRealm(position);
                    realm.commitTransaction();
                }
            });

            return convertView;
        }
    }

}
