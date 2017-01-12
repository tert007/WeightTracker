package com.greenkey.weighttracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.greenkey.weighttracker.app.Sex;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexander on 29.12.2016.
 */

public class SettingsManager {
    private static SharedPreferences sharedPreferences;

    private static final String WEIGHT_UNIT_INDEX_KEY = "weight_unit";
    private static final int WEIGHT_UNIT_INDEX_DEFAULT_VALUE = 0;
    private static int currentWeightUnitIndex;

    private static final String GOAL_WEIGHT_KEY = "goal_weight";
    private static final float GOAL_WEIGHT_DEFAULT_VALUE = 0;
    private static float currentGoalWeight;

    private static final String SEX_INDEX_KEY = "user_sex";
    private static final String SEX_DEFAULT_VALUE = String.valueOf(Sex.EMPTY);
    private static Sex userSex;

    private static final String BIRTH_DATE_KEY = "user_birth_date";
    private static final int BIRTH_DATE_DEFAULT_VALUE = 0;
    private static long userBirthDate;

    private static final String TALL_KEY = "user_tall";
    private static final int TALL_DEFAULT_VALUE = 0;
    private static int userTall;


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

    static {
        observerList = new ArrayList<>();
    }

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userSex = Sex.valueOf(sharedPreferences.getString(SEX_INDEX_KEY, SEX_DEFAULT_VALUE));
        currentGoalWeight = sharedPreferences.getFloat(GOAL_WEIGHT_KEY, GOAL_WEIGHT_DEFAULT_VALUE);
        userTall = sharedPreferences.getInt(TALL_KEY,TALL_DEFAULT_VALUE);
        userBirthDate = sharedPreferences.getLong(BIRTH_DATE_KEY,BIRTH_DATE_DEFAULT_VALUE);
        currentWeightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
    }

    public static void setParams(int weightIndex, float desireWeight) {
        currentWeightUnitIndex = weightIndex;
        currentGoalWeight = desireWeight;
        sharedPreferences.edit().putInt(WEIGHT_UNIT_INDEX_KEY, weightIndex).apply();
        sharedPreferences.edit().putFloat(GOAL_WEIGHT_KEY, desireWeight).apply();
        informSubscribers();
    }

    public static void setWeightUnitIndex(int index) {
        currentWeightUnitIndex = index;
        sharedPreferences.edit().putInt(WEIGHT_UNIT_INDEX_KEY, index).apply();
        informSubscribers();
    }

    public static int getWeightUnitIndex() {
        return currentWeightUnitIndex;
    }

    public static void setSex(Sex sex) {
        userSex = sex;
        sharedPreferences.edit().putString(SEX_INDEX_KEY,sex.toString()).apply();
    }

    public static Sex getSex(){
        return userSex;
    }

    public static void setGoalWeight(float weight) {
        currentGoalWeight = weight;
        sharedPreferences.edit().putFloat(GOAL_WEIGHT_KEY, currentGoalWeight).apply();
        informSubscribers();
    }

    public static float getGoalWeight() {
        return currentGoalWeight;
    }

    public static void setBirthDate(long birthDate){
        userBirthDate = birthDate;
        sharedPreferences.edit().putLong(BIRTH_DATE_KEY,userBirthDate).apply();
    }

    public static long getBirthDate(){
        return userBirthDate;
    }

    public static void setTall(int tall){
        userTall = tall;
        sharedPreferences.edit().putInt(TALL_KEY,userTall).apply();
    }

    public static int getTall(){
        return userTall;
    }

}
