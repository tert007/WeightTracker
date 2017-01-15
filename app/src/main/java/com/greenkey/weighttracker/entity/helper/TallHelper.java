package com.greenkey.weighttracker.entity.helper;

import java.util.Locale;

/**
 * Created by Vadim on 13.01.2017.
 */

public class TallHelper {

    public static final int METRIC_SYSTEM_INDEX = 0;
    public static final int ENGLISH_SYSTEM_INDEX = 1;

    private static final float CENTIMETERS_PER_INCH = 2.54f;
    private static final int INCHES_PER_FOOT = 12;

    public static String convertToUnitSystem(int tallCm, int unitSystemIndex) {
        switch (unitSystemIndex) {

            case METRIC_SYSTEM_INDEX:
                return String.valueOf(tallCm);

            case ENGLISH_SYSTEM_INDEX:
                int inchCount = (int) (tallCm / CENTIMETERS_PER_INCH);
                int footCount = inchCount / INCHES_PER_FOOT;

                inchCount = inchCount - footCount * INCHES_PER_FOOT;

                return String.format(Locale.getDefault(), "%1$d' %2$d\"", footCount, inchCount);

            default:
                return null;
        }
    }
}
