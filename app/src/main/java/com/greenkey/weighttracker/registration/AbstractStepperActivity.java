/*
Copyright 2016 StepStone Services

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.greenkey.weighttracker.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.main.MainActivity;
import com.greenkey.weighttracker.registration.step.RegistrationDesireWeightFragment;
import com.greenkey.weighttracker.registration.step.RegistrationtSexFragment;
import com.greenkey.weighttracker.registration.step.RegistrationStartWeightFragment;
import com.greenkey.weighttracker.registration.step.RegistrationBirthDateFragment;
import com.greenkey.weighttracker.registration.step.RegistrationTallFragment;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractStepAdapter;

public abstract class AbstractStepperActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    View parentLayout;
    StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        parentLayout = findViewById(android.R.id.content);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;
        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager()), startingStepPosition);
        mStepperLayout.setListener(this);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.setCurrentStepPosition(currentStepPosition - 1);
        } else {
            finish();
        }
    }

    @Override
    public void onCompleted(View completeButton) {
        SettingsManager.setUserRegistered(true);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(VerificationError verificationError) {
        Snackbar.make(parentLayout,verificationError.getErrorMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        finish();
    }

    private static class MyStepperAdapter extends AbstractStepAdapter {

        MyStepperAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment createStep(int position) {
            switch (position) {
                case 0:
                    return new RegistrationtSexFragment();
                case 1:
                    return new RegistrationBirthDateFragment();
                case 2:
                    return new RegistrationTallFragment();
                case 3:
                    return new RegistrationStartWeightFragment();
                case 4:
                    return new RegistrationDesireWeightFragment();
                default:
                    throw new IllegalArgumentException("Unsupported position: " + position);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
