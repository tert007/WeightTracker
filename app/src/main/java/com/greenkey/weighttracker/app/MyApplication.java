package com.greenkey.weighttracker.app;

import android.app.Application;

import com.greenkey.weighttracker.SettingsManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Alexander on 29.12.2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        SettingsManager.init(this);

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }
}