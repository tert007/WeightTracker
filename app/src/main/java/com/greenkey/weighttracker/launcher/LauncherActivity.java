package com.greenkey.weighttracker.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.main.MainActivity;
import com.greenkey.weighttracker.registration.RegistrationActivity;

/**
 * Created by Alexander on 13.01.2017.
 */

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (SettingsManager.isUserRegistered()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, RegistrationActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
