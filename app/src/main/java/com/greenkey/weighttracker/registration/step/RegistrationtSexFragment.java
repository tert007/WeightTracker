package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.greenkey.weighttracker.app.Sex;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationtSexFragment extends Fragment  implements Step {
    ImageView sexMaleImageView;
    ImageView sexFemaleImageView;
    Sex sex;
    String errorMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_sex_fragment, container, false);
        sexMaleImageView = (ImageView) view.findViewById(R.id.registration_sex_choose_male);
        sexFemaleImageView = (ImageView) view.findViewById(R.id.registration_sex_choose_female);
        sex = SettingsManager.getSex();

        if(sex == Sex.MALE){
            sexMaleImageView.setImageResource(R.drawable.registration_sex_male_pressed_icon);
        }
        else if(sex == Sex.FEMALE){
            sexFemaleImageView.setImageResource(R.drawable.registration_sex_female_pressed_icon);
        }

        sexMaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexFemaleImageView.setImageResource(R.drawable.registration_sex_female_icon);
                sexMaleImageView.setImageResource(R.drawable.registration_sex_male_pressed_icon);
                sex = Sex.MALE;
                SettingsManager.setSex(sex);
            }
        });

        sexFemaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexMaleImageView.setImageResource(R.drawable.registration_sex_male_icon);
                sexFemaleImageView.setImageResource(R.drawable.registration_sex_female_pressed_icon);
                sex = Sex.FEMALE;
                SettingsManager.setSex(sex);
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
        if(sex == Sex.EMPTY){
            errorMessage = getString(R.string.sex_error);
            return new VerificationError(errorMessage);
        }
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
