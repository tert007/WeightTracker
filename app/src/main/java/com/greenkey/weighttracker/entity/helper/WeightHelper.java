package com.greenkey.weighttracker.entity.helper;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.RealmObject;

/**
 * Created by Alexander on 28.12.2016.
 */
public class WeightHelper {

    public static final int METRIC_SYSTEM_INDEX = 0;
    public static final int ENGLISH_SYSTEM_INDEX = 1;

    private static final float[] coefficients = {1f, 0.453592f };
    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.0");

    //Из кг в любой вес
    public static float convert(float valueKg, int targetWeightUnitIndex) {
        return valueKg / coefficients[targetWeightUnitIndex];
    }

    public static String convertByString(float valueKg, int targetWeightUnitIndex) {
        return decimalFormat.format(round(convert(valueKg, targetWeightUnitIndex), 1));
    }

    //Из любого веса в кг
    public static float reconvert(float value, int currentWeightUnitIndex) {
        return value * coefficients[currentWeightUnitIndex];
    }

    public static String reconvertByString(float value, int currentWeightUnitIndex) {
        return decimalFormat.format(round(reconvert(value, currentWeightUnitIndex), 1));
    }

    public static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    /**
     * Get integer part of the value.
     */
    public static int getFistPartOfValue(float value) {
        return (int) value;
    }

    /**
     * Get fractional part of the value.
     */
    public static int getSecondPartOfValue(float value) {
        return Math.round((value - (int) value) * 10);
    }
}
