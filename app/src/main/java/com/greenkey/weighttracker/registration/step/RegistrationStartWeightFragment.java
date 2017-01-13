package com.greenkey.weighttracker.registration.step;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.WeightRecord;
import com.greenkey.weighttracker.entity.helper.WeightHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import io.realm.Realm;

public class RegistrationStartWeightFragment extends Fragment  implements Step, NumberPicker.OnValueChangeListener {

    public static final float START_WEIGHT_DEFAULT_VALUE = 70;

    private Realm realm;

    private float startWeight;
    private int weightUnitIndex;
    private NumberPicker firstNumberPicker;
    private NumberPicker secondNumberPicker;
    private boolean isChoseStartWeight = false;
    private String errorMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_start_weight_fragment, container, false);

        realm = Realm.getDefaultInstance();

        firstNumberPicker = (NumberPicker)view.findViewById(R.id.set_weight_dialog_first_number_picker);
        secondNumberPicker = (NumberPicker)view.findViewById(R.id.set_weight_dialog_second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        startWeight = SettingsManager.getStartWeight();
        weightUnitIndex = SettingsManager.getWeightUnitIndex();

        if(startWeight == 0){
            startWeight = START_WEIGHT_DEFAULT_VALUE;
        } else {
            isChoseStartWeight = true;
        }

        final float convertedValue = WeightHelper.convert(startWeight, weightUnitIndex);

        final int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
        final int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

        firstNumberPicker.setValue(firstPartOfValue);
        secondNumberPicker.setValue(secondPartOfValue);

        firstNumberPicker.setOnValueChangedListener(this);
        secondNumberPicker.setOnValueChangedListener(this);

        return view;
    }

    @Override
    public int getName() {
        return R.string.current_weight;
    }

    @Override
    public VerificationError verifyStep() {
        if(!isChoseStartWeight){
            errorMessage = getString(R.string.start_weight_error);
            return new VerificationError(errorMessage);
        }
        else {
            final float value = Float.valueOf(firstNumberPicker.getValue() + "." + secondNumberPicker.getValue());
            startWeight = WeightHelper.reconvert(value, weightUnitIndex);
            SettingsManager.setStartWeight(startWeight);

            realm.beginTransaction();
            WeightRecord weightRecord = realm.createObject(WeightRecord.class);
            weightRecord.setValue(startWeight);
            weightRecord.setDate(System.currentTimeMillis());
            realm.commitTransaction();

            return null;
        }
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        isChoseStartWeight = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
