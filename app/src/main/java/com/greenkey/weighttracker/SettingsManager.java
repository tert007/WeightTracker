package com.greenkey.weighttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.greenkey.weighttracker.app.Sex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 29.12.2016.
 */

public class SettingsManager {
    private static SharedPreferences sharedPreferences;

    private static final String WEIGHT_UNIT_INDEX_KEY = "weight_unit";
    private static final int WEIGHT_UNIT_INDEX_DEFAULT_VALUE = 0;
    private static int weightUnitIndex;

    private static final String DESIRE_WEIGHT_KEY = "goal_weight";
    private static final float DESIRE_WEIGHT_DEFAULT_VALUE = 0;
    private static float desireWeight;

    private static final String SEX_INDEX_KEY = "user_sex";
    private static final String SEX_DEFAULT_VALUE = String.valueOf(Sex.EMPTY);
    private static Sex userSex;

    private static final String BIRTH_DATE_KEY = "user_birth_date";
    private static final int BIRTH_DATE_DEFAULT_VALUE = 0;
    private static long userBirthDate;

    private static final String START_WEIGHT_KEY = "start_weight";
    private static final float START_WEIGHT_DEFAULT_VALUE = 0;
    private static float startWeight;

    private static final String TALL_KEY = "user_tall";
    private static final int TALL_DEFAULT_VALUE = 0;
    private static int userTall;

    private static final String IS_REGISTER_KEY = "user_is_register";
    private static final boolean IS_REGISTER_DEFAULT_VALUE = false;
    private static boolean isRegister = false;

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

    /*static {
        observerList = new ArrayList<>();
    }*/

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userSex = Sex.valueOf(sharedPreferences.getString(SEX_INDEX_KEY, SEX_DEFAULT_VALUE));
        userTall = sharedPreferences.getInt(TALL_KEY,TALL_DEFAULT_VALUE);
        userBirthDate = sharedPreferences.getLong(BIRTH_DATE_KEY,BIRTH_DATE_DEFAULT_VALUE);

        startWeight = sharedPreferences.getFloat(START_WEIGHT_KEY, START_WEIGHT_DEFAULT_VALUE);
        desireWeight = sharedPreferences.getFloat(DESIRE_WEIGHT_KEY, DESIRE_WEIGHT_DEFAULT_VALUE);
        weightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
        isRegister = sharedPreferences.getBoolean(IS_REGISTER_KEY,IS_REGISTER_DEFAULT_VALUE);
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

    public static void setSex(Sex sex) {
        userSex = sex;
        sharedPreferences.edit().putString(SEX_INDEX_KEY,sex.toString()).apply();
    }

    public static Sex getSex(){
        return userSex;
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

    public static boolean isRegister(){
        return  isRegister;
    }

    public static void registerUser(){
        isRegister = true;
        sharedPreferences.edit().putBoolean(IS_REGISTER_KEY,isRegister).apply();
    }

    public static float getStartWeight() {
        return startWeight;
    }
}
