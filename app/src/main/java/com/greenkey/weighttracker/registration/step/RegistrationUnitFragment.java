package com.greenkey.weighttracker.registration.step;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.Unit;
import com.greenkey.weighttracker.entity.helper.TallHelper;
import com.greenkey.weighttracker.entity.helper.WeightHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationUnitFragment extends Fragment implements Step {

    private View englishSystemView;
    private View metricSystemView;
    private Unit unit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_unit_fragment, container, false);

        englishSystemView = view.findViewById(R.id.registration_unit_english_system_view);
        metricSystemView = view.findViewById(R.id.registration_unit_metric_system_view);

        unit = SettingsManager.getUnit();

        englishSystemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unit = Unit.ENGLISH;

                metricSystemView.setBackgroundResource(R.drawable.card_background);
                englishSystemView.setBackgroundResource(R.drawable.card_background_registration_selected);
            }
        });

        metricSystemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unit = Unit.METRIC;

                englishSystemView.setBackgroundResource(R.drawable.card_background);
                metricSystemView.setBackgroundResource(R.drawable.card_background_registration_selected);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (unit == Unit.ENGLISH) {
            englishSystemView.performClick();
        } else if (unit == Unit.METRIC) {
            metricSystemView.performClick();
        }
    }

    @Override
    public int getName() {
        return R.string.unit_system;
    }

    @Override
    public VerificationError verifyStep() {
        switch (unit) {
            case EMPTY :
                return new VerificationError(getString(R.string.unit_system_error));
            case ENGLISH:
                SettingsManager.setUnit(unit);
                SettingsManager.setWeightUnitIndex(WeightHelper.ENGLISH_SYSTEM_INDEX);
                SettingsManager.setTallUnitIndex(TallHelper.ENGLISH_SYSTEM_INDEX);


                return null;
            case METRIC:
                SettingsManager.setUnit(unit);
                SettingsManager.setWeightUnitIndex(WeightHelper.METRIC_SYSTEM_INDEX);
                SettingsManager.setTallUnitIndex(TallHelper.METRIC_SYSTEM_INDEX);


                return null;
            default:
                return null;
        }
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
