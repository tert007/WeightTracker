package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.greenkey.weighttracker.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;


public class RegistrationBirthDateFragment extends Fragment  implements Step {
    DatePicker birthDatePicker;

    public RegistrationBirthDateFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_birth_date_fragment, container, false);
        birthDatePicker = (DatePicker)view.findViewById(R.id.birth_date_picker);
        birthDatePicker.updateDate(1990,0,1);
        birthDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
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
        return R.string.birth_date;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
