package com.greenkey.weighttracker.statistics;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.WeightHelper;
import com.greenkey.weighttracker.WeightRecord;

import java.util.Random;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Alexander on 28.12.2016.
 */
public class StatisticsWeightRecordListFragment extends Fragment/* implements SettingsManager.SettingsListener */{

    private ListView listView;

    private String[] units;

    private float desireWeight;
    private int weightUnitIndex;

    private Realm realm;
    private RealmResults<WeightRecord> realmResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_list_view_fragment, container, false);

        units = getResources().getStringArray(R.array.weight_units_short_name);

        desireWeight = SettingsManager.getDesireWeight();
        weightUnitIndex = SettingsManager.getWeightUnitIndex();

        realm = Realm.getDefaultInstance();
        realmResults = realm.where(WeightRecord.class).findAll().sort("date", Sort.DESCENDING);;

        final View emptyView = view.findViewById(R.id.statistics_empty_list_view);

        listView = (ListView) view.findViewById(R.id.statistics_list_view);
        listView.setEmptyView(emptyView);

        listView.setAdapter(new WeightListAdapter(getActivity(), realmResults));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setItems(R.array.list_item_dialog_types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showEditDialog(position);
                                break;
                            case 1:
                                showRemoveDialog(position);
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });


        return view;
    }

    private void showRemoveDialog(final int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.remove);

        builder.setMessage(R.string.remove_record);

        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                realm.beginTransaction();
                realmResults.deleteFromRealm(itemPosition);
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

    private void showEditDialog(final int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View setCurrentWeightView = inflater.inflate(R.layout.dialog_edit_weight, null);

        final NumberPicker firstNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.first_number_picker);
        final NumberPicker secondNumberPicker = (NumberPicker)setCurrentWeightView.findViewById(R.id.second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        final WeightRecord weightRecord = realmResults.get(itemPosition);

        float convertedValue = WeightHelper.convert(weightRecord.getValue(), weightUnitIndex);

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
                weightRecord.setValue(reconvertedValue);
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

    public class WeightListAdapter extends RealmBaseAdapter<WeightRecord> implements ListAdapter {

        private static final String NOT_INITIALIZED_VALUE = "--";

        private final int greenColor;
        private final int redColor;
        private final int grey;

        public WeightListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<WeightRecord> data) {
            super(context, data);

            this.greenColor = ContextCompat.getColor(context, R.color.accept_green);
            this.redColor = ContextCompat.getColor(context, R.color.reject_red);
            this.grey = ContextCompat.getColor(context, R.color.grey);
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

            final ImageView coloredShape = (ImageView) convertView.findViewById(R.id.statistics_list_view_item_colored_shape_image_view);

            ShapeDrawable shpy = new ShapeDrawable(new RectShape());
            shpy.setIntrinsicHeight(100);
            shpy.setIntrinsicWidth(10);

            Random random = new Random();

            if (random.nextBoolean()) {
                shpy.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.accept_green));
            } else {
                shpy.getPaint().setColor(ContextCompat.getColor(getContext(), R.color.reject_red));
            }

            coloredShape.setImageDrawable(shpy);

            final TextView valueTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_weight_text_view);
            valueTextView.setText(WeightHelper.convertByString(currentWeightRecord.getValue(), weightUnitIndex));

            final TextView valueUnitTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_weight_unit_text_view);
            valueUnitTextView.setText(units[weightUnitIndex]);

            final TextView dateTextView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_date_text_view);
            dateTextView.setText(currentWeightRecord.getDateByString());

            final TextView weightDifferenceTexView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_weight_difference_text_view);

            //final TextView weightDifferenceUnitTexView = (TextView) convertView.findViewById(R.id.statistics_list_view_item_weight_difference_unit_text_view);
            //weightDifferenceUnitTexView.setText(units[weightUnitIndex]);

            if (position == getCount() - 1) {
                weightDifferenceTexView.setText(NOT_INITIALIZED_VALUE);
                weightDifferenceTexView.setTextColor(grey);
            } else {
                final WeightRecord previousWeightRecord = getItem(position + 1);

                final float previousWeight = previousWeightRecord.getValue();
                final float currentWeight = currentWeightRecord.getValue();

                final float weightDifference = currentWeight - previousWeight;

                if (weightDifference == 0) {
                    weightDifferenceTexView.setText(WeightHelper.convertByString(weightDifference, weightUnitIndex));
                    weightDifferenceTexView.setTextColor(grey);
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

            return convertView;
        }
    }

}
