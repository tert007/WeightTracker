package com.greenkey.weighttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by Alexander on 29.12.2016.
 */

public class SettingsManager {

    public interface SettingsObserver {
        void update(float desireWeight, int weightUnitIndex);
    }

    private static final List<SettingsObserver> observerList;

    public static void subscribe(@NonNull SettingsObserver observer) {
        observer.update(currentGoalWeight, currentWeightUnitIndex);
        observerList.add(observer);
    }

    private static void informSubscribers() {
        for (SettingsObserver observer : observerList) {
            if (observer == null)
                continue;

            observer.update(currentGoalWeight, currentWeightUnitIndex);
        }
    }

    private static SharedPreferences sharedPreferences;

    private static final String WEIGHT_UNIT_INDEX_KEY = "weight_unit";
    private static final int WEIGHT_UNIT_INDEX_DEFAULT_VALUE = 0;
    private static int currentWeightUnitIndex;

    private static final String GOAL_WEIGHT_KEY = "goal_weight";
    private static final float GOAL_WEIGHT_DEFAULT_VALUE = 0;
    private static float currentGoalWeight;

    static {
        observerList = new ArrayList<>();
    }

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //WeightUnit
    public static void setWeightUnitIndex(int index) {
        currentWeightUnitIndex = index;
        sharedPreferences.edit().putInt(WEIGHT_UNIT_INDEX_KEY, index).apply();
        informSubscribers();
    }

    public static int getWeightUnitIndex() {
        currentWeightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
        return currentWeightUnitIndex;
    }

    /*public static WeightUnit getWeightUnit() {
        int weightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
        currentWeightUnit = WEIGHT_UNITS[weightUnitIndex];

        return currentWeightUnit;
    }*/

    //GoalWeight
    public static void setGoalWeight(float weight) {
        currentGoalWeight = weight;
        sharedPreferences.edit().putFloat(GOAL_WEIGHT_KEY, currentGoalWeight).apply();
        informSubscribers();
    }

    public static float getGoalWeight() {
        currentGoalWeight = sharedPreferences.getFloat(GOAL_WEIGHT_KEY, GOAL_WEIGHT_DEFAULT_VALUE);
        return currentGoalWeight;
    }

}
