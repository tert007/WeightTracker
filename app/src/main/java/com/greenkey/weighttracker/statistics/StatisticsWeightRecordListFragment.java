package com.greenkey.weighttracker.statistics;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
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
public class StatisticsWeightRecordListFragment extends Fragment implements SettingsManager.SettingsObserver {

    //WeightListAdapter adapter;
    ListView listView;

    private float desireWeight;
    private int weightUnitIndex;

    private Realm realm;
    private RealmResults<WeightRecord> realmResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_list_view_fragment, container, false);

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(WeightRecord.class).findAll();

        listView = (ListView) view.findViewById(R.id.statistics_list_view);

        final View emptyView = view.findViewById(R.id.statistics_empty_list_view);
        listView.setEmptyView(emptyView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SettingsManager.subscribe(this);
    }

    @Override
    public void update(float desireWeight, int weightUnitIndex) {
        Log.d("SETTINGS", "UPDATE_SETTINGS_STATISTICS_LIST_VIEW");

        this.desireWeight = desireWeight;
        this.weightUnitIndex = weightUnitIndex;

        Activity activity = getActivity();
        if (activity != null) {
            listView.setAdapter(new WeightListAdapter(getActivity(), realmResults));
        }
    }

    public class WeightListAdapter extends RealmBaseAdapter<WeightRecord> implements ListAdapter {

        private static final String NOT_INITIALIZED_VALUE = "--";

        private final int greenColor;
        private final int redColor;

        public WeightListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<WeightRecord> data) {
            super(context, data);

            this.greenColor = ContextCompat.getColor(context, R.color.accept_green);
            this.redColor = ContextCompat.getColor(context, R.color.reject_red);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(R.layout.statistics_list_view_item, parent, false);
            }

            final WeightRecord currentWeightRecord = getItem(position);

            if (currentWeightRecord == null) {
                return convertView;
            }

            final TextView valueTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_value_text_view);
            valueTextView.setText(WeightHelper.convertByString(currentWeightRecord.getValue(), weightUnitIndex));

            final TextView dateTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_date_text_view);
            dateTextView.setText(currentWeightRecord.getDateByString());

            final TextView weightDifferenceTexView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_weight_difference);

            if (position == 0) {
                weightDifferenceTexView.setText(NOT_INITIALIZED_VALUE);
            } else {
                final WeightRecord previousWeightRecord = getItem(position - 1);

                final float previousWeight = previousWeightRecord.getValue();
                final float currentWeight = currentWeightRecord.getValue();

                final float weightDifference = currentWeight - previousWeight;

                if (weightDifference == 0) {
                    weightDifferenceTexView.setText(WeightHelper.convertByString(weightDifference, weightUnitIndex));
                } else {

                    if (desireWeight > currentWeight) {
                        //Нужно скидывать
                        if (previousWeight > currentWeight){
                            weightDifferenceTexView.setTextColor(redColor);
                        }

                        if (previousWeight < currentWeight) {
                            weightDifferenceTexView.setTextColor(greenColor);
                        }
                    }

                    if (desireWeight < currentWeight) {
                        //Нужно набрать
                        if (previousWeight > currentWeight) {
                            weightDifferenceTexView.setTextColor(greenColor);
                        }

                        if (previousWeight < currentWeight){
                            weightDifferenceTexView.setTextColor(redColor);
                        }
                    }
                }

                weightDifferenceTexView.setText(WeightHelper.convertByString(weightDifference, weightUnitIndex));
            }

            final View remove = convertView.findViewById(R.id.statistics_list_view_item_remove_image_view);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.remove);

                    builder.setMessage(R.string.remove_message);

                    builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            realm.beginTransaction();
                            realmResults.deleteFromRealm(position);
                            realm.commitTransaction();

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

            final View edit = convertView.findViewById(R.id.statistics_list_view_item_edit_image_view);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.set_weight);

                    final LayoutInflater inflater = LayoutInflater.from(getActivity());
                    final View setCurrentWeightView = inflater.inflate(R.layout.set_weight_dialog, null);

                    final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_weight_dialog_first_number_picker);
                    final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.set_weight_dialog_second_number_pickrer);

                    firstNumberPicker.setMinValue(1);
                    firstNumberPicker.setMaxValue(999);

                    secondNumberPicker.setMinValue(0);
                    secondNumberPicker.setMaxValue(9);

                    float convertedValue = WeightHelper.convert(currentWeightRecord.getValue(), weightUnitIndex);

                    int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
                    int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

                    firstNumberPicker.setValue(firstPartOfValue);
                    secondNumberPicker.setValue(secondPartOfValue);

                    builder.setView(setCurrentWeightView);

                    builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                            String value = firstNumberPicker.getValue() + "." + secondNumberPicker.getValue();

                            realm.beginTransaction();
                            float reconvertedValue = WeightHelper.reconvert(Float.valueOf(value), weightUnitIndex);
                            currentWeightRecord.setValue(reconvertedValue);
                            realm.commitTransaction();
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

            return convertView;
        }
    }

}
