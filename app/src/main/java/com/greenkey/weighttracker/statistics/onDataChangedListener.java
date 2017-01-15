package com.greenkey.weighttracker.statistics;

import com.greenkey.weighttracker.entity.WeightRecord;

import io.realm.RealmResults;

/**
 * Created by Alexander on 15.01.2017.
 */

public interface OnDataChangedListener {
    void onDataChangedListener(RealmResults<WeightRecord> realmResults);
}
