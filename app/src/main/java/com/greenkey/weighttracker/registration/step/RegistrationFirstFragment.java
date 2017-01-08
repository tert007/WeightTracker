package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.greenkey.weighttracker.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationFirstFragment extends Fragment  implements Step {
    ImageView sexMaleImageView;
    ImageView sexFemaleImageView;

    public RegistrationFirstFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_first_fragment, container, false);
        sexMaleImageView = (ImageView) view.findViewById(R.id.registration_sex_choose_male);
        sexFemaleImageView = (ImageView) view.findViewById(R.id.registration_sex_choose_female);

        sexMaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexFemaleImageView.setImageResource(R.drawable.registration_sex_female_icon);
                sexMaleImageView.setImageResource(R.drawable.registration_sex_male_pressed_icon);
            }
        });

        sexFemaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexMaleImageView.setImageResource(R.drawable.registration_sex_male_icon);
                sexFemaleImageView.setImageResource(R.drawable.registration_sex_female_pressed_icon);
            }
        });
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
        return R.string.sex;
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
