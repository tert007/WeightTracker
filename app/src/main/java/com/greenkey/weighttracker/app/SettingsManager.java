package com.greenkey.weighttracker.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.greenkey.weighttracker.entity.Sex;

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

    private static final String IS_USER_REGISTERED_KEY = "is_user_registered";
    private static final boolean IS_USER_REGISTERED_DEFAULT_VALUE = false;
    private static boolean isUserRegistered = false;

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        userSex = Sex.valueOf(sharedPreferences.getString(SEX_INDEX_KEY, SEX_DEFAULT_VALUE));
        userTall = sharedPreferences.getInt(TALL_KEY,TALL_DEFAULT_VALUE);
        userBirthDate = sharedPreferences.getLong(BIRTH_DATE_KEY,BIRTH_DATE_DEFAULT_VALUE);

        startWeight = sharedPreferences.getFloat(START_WEIGHT_KEY, START_WEIGHT_DEFAULT_VALUE);
        desireWeight = sharedPreferences.getFloat(DESIRE_WEIGHT_KEY, DESIRE_WEIGHT_DEFAULT_VALUE);
        weightUnitIndex = sharedPreferences.getInt(WEIGHT_UNIT_INDEX_KEY, WEIGHT_UNIT_INDEX_DEFAULT_VALUE);
        isUserRegistered = sharedPreferences.getBoolean(IS_USER_REGISTERED_KEY, IS_USER_REGISTERED_DEFAULT_VALUE);
    }

    //WeightUnit
    public static void setWeightUnitIndex(int index) {
        weightUnitIndex = index;
        sharedPreferences.edit().putInt(WEIGHT_UNIT_INDEX_KEY, index).apply();
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
        sharedPreferences.edit().putLong(BIRTH_DATE_KEY, userBirthDate).apply();
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
    }

    public static float getDesireWeight() {
        return desireWeight;
    }

    public static void setStartWeight(float weight) {
        startWeight = weight;
        sharedPreferences.edit().putFloat(START_WEIGHT_KEY, weight).apply();
    }

    public static boolean isUserRegistered(){
        return isUserRegistered;
    }

    public static void setUserRegistered(boolean isRegistered){
        isUserRegistered = isRegistered;
        sharedPreferences.edit().putBoolean(IS_USER_REGISTERED_KEY, isUserRegistered).apply();
    }

    public static float getStartWeight() {
        return startWeight;
    }
}
