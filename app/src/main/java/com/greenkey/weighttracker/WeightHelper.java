package com.greenkey.weighttracker;

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

    private static final float[] coefficients = {1f, 0.453f};
    private static final DecimalFormat decimalFormat = new DecimalFormat("#00.0");

    //Из кг в любой вес
    public static float convert(float value, int targetWeightUnitIndex) {
        return round(value / coefficients[targetWeightUnitIndex], 1);
    }

    public static String convertByString(float value, int targetWeightUnitIndex) {
        return decimalFormat.format(convert(value, targetWeightUnitIndex));
    }

    //Из любого веса в кг
    public static float reconvert(float value, int currentWeightUnitIndex) {
        return round(value * coefficients[currentWeightUnitIndex], 1);
    }

    public static String reconvertByString(float value, int currentWeightUnitIndex) {
        return decimalFormat.format(reconvert(value, currentWeightUnitIndex));
    }

    private static float round(float number, int scale) {
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
