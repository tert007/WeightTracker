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

    private static String GOAL_WEIGHT_KEY = "goal_weight";
    private static float GOAL_WEIGHT_DEFAULT_VALUE = 0;
    private static float goalWeight;

    private static WeightUnit[] weightUnites;

    static {
        weightUnites = WeightUnit.values();
    }

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //WeightUnit
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

    //GoalWeight
    public static void setGoalWeight(float weight) {
        goalWeight = weight;
        sharedPreferences.edit().putFloat(GOAL_WEIGHT_KEY, goalWeight).apply();
    }

    public static float getGoalWeight() {
        goalWeight = sharedPreferences.getFloat(GOAL_WEIGHT_KEY, GOAL_WEIGHT_DEFAULT_VALUE);
        return goalWeight;
    }

}
