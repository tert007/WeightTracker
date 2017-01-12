package com.greenkey.weighttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 29.12.2016.
 */

public class SettingsManager {

    /*
    public interface SettingsListener {
        void onChangeListener(float startWeight, float desireWeight, int weightUnitIndex);
    }

    private static final List<SettingsListener> listenersList = new ArrayList<>();

    public static void addOnChangeListener(@NonNull SettingsListener observer) {
        observer.onChangeListener(startWeight, desireWeight, weightUnitIndex);
        listenersList.add(observer);
    }

    private static void informListeners() {
        for (SettingsListener listener : listenersList) {
            if (listener == null){
                continue;
            }

            listener.onChangeListener(startWeight, desireWeight, weightUnitIndex);
        }
    }*/

    private static SharedPreferences sharedPreferences;

    private static final String WEIGHT_UNIT_INDEX_KEY = "weight_unit";
    private static final int WEIGHT_UNIT_INDEX_DEFAULT_VALUE = 0;
    private static int weightUnitIndex;

    private static final String DESIRE_WEIGHT_KEY = "goal_weight";
    private static final float DESIRE_WEIGHT_DEFAULT_VALUE = 0;
    private static float desireWeight;

    private static final String START_WEIGHT_KEY = "start_weight";
    private static final float START_WEIGHT_DEFAULT_VALUE = 0;
    private static float startWeight;

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        startWeight = sharedPreferences.getFloat(START_WEIGHT_KEY, START_WEIGHT_DEFAULT_VALUE);
        desireWeight = sharedPreferences.getFloat(DESIRE_WEIGHT_KEY, DESIRE_WEIGHT_DEFAULT_VALUE);
        weightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
    }

    public static void setParams(float startWeightValue, float desireWeightValue) {
        startWeight = startWeightValue;
        desireWeight = desireWeightValue;

        sharedPreferences.edit().putFloat(START_WEIGHT_KEY, startWeight).apply();
        sharedPreferences.edit().putFloat(DESIRE_WEIGHT_KEY, desireWeight).apply();

        //informListeners();
    }

    //WeightUnit
    public static void setWeightUnitIndex(int index) {
        weightUnitIndex = index;
        sharedPreferences.edit().putInt(WEIGHT_UNIT_INDEX_KEY, index).apply();

        //informListeners();
    }

    public static int getWeightUnitIndex() {
        return weightUnitIndex;
    }

    public static void setDesireWeight(float weight) {
        desireWeight = weight;
        sharedPreferences.edit().putFloat(DESIRE_WEIGHT_KEY, desireWeight).apply();
        //informListeners();
    }

    public static float getDesireWeight() {
        return desireWeight;
    }

    public static void setStartWeight(float weight) {
        startWeight = weight;
        sharedPreferences.edit().putFloat(START_WEIGHT_KEY, weight).apply();
        //informListeners();
    }

    public static float getStartWeight() {
        return startWeight;
    }
}
