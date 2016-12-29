package com.greenkey.weighttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Alexander on 29.12.2016.
 */

public class SettingsManager {

    private static SharedPreferences sharedPreferences;

    private static String WEIGHT_UNIT_INDEX_KEY = "weight_unit";
    private static int WEIGHT_UNIT_INDEX_DEFAULT_VALUE = 0;
    private static WeightUnit currentWeightUnit;

    private static WeightUnit[] weightUnites;

    static {
        weightUnites = WeightUnit.values();
    }

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setWeightUnitIndex(int index) {
        currentWeightUnit = weightUnites[index];
        sharedPreferences.edit().putInt(WEIGHT_UNIT_INDEX_KEY, index).apply();
    }

    public static int getWeightUnitIndex() {
        return sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
    }

    public static WeightUnit getWeightUnit() {
        int weightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
        currentWeightUnit = weightUnites[weightUnitIndex];

        return currentWeightUnit;
    }

}
