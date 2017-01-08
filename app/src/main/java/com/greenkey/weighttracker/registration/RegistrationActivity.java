package com.greenkey.weighttracker.registration;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.greenkey.weighttracker.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class RegistrationActivity extends AbstractStepperActivity{

    @Override
    protected int getLayoutResId() {
        return R.layout.registration_activity;
    }
}
