package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.helper.WeightHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationDesireWeightFragment extends Fragment implements Step, NumberPicker.OnValueChangeListener {
    public static final float DESIRE_WEIGHT_DEFAULT_VALUE = 70;
    private float desireWeight;
    private int weightUnitIndex;
    NumberPicker firstNumberPicker;
    NumberPicker secondNumberPicker;
    boolean chooseDesireWeight = false;
    String errorMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_desire_weight, container, false);
        firstNumberPicker = (NumberPicker)view.findViewById(R.id.set_weight_dialog_first_number_picker);
        secondNumberPicker = (NumberPicker)view.findViewById(R.id.set_weight_dialog_second_number_picker);

        firstNumberPicker.setMinValue(1);
        firstNumberPicker.setMaxValue(999);

        secondNumberPicker.setMinValue(0);
        secondNumberPicker.setMaxValue(9);

        desireWeight = SettingsManager.getDesireWeight();
        weightUnitIndex = SettingsManager.getWeightUnitIndex();

        if(desireWeight == 0){
            desireWeight = DESIRE_WEIGHT_DEFAULT_VALUE;
        }
        else{
            chooseDesireWeight = true;
        }

        final float convertedValue = WeightHelper.convert(desireWeight, weightUnitIndex);

        final int firstPartOfValue = WeightHelper.getFistPartOfValue(convertedValue);
        final int secondPartOfValue = WeightHelper.getSecondPartOfValue(convertedValue);

        firstNumberPicker.setValue(firstPartOfValue);
        secondNumberPicker.setValue(secondPartOfValue);

        firstNumberPicker.setOnValueChangedListener(this);
        secondNumberPicker.setOnValueChangedListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public int getName() {
        return R.string.desired_weight;
    }

    @Override
    public VerificationError verifyStep() {
        if(!chooseDesireWeight){
            errorMessage = getString(R.string.desire_weight_error);
            return new VerificationError(errorMessage);
        }
        else {
            firstNumberPicker.clearFocus();
            secondNumberPicker.clearFocus();
            final float value = Float.valueOf(firstNumberPicker.getValue() + "." + secondNumberPicker.getValue());
            desireWeight = WeightHelper.reconvert(value, weightUnitIndex);
            SettingsManager.setDesireWeight(desireWeight);
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
        chooseDesireWeight = true;
    }
}
