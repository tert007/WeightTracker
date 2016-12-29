package com.greenkey.weighttracker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.RealmObject;

/**
 * Created by Alexander on 28.12.2016.
 */
public class WeightRecord extends RealmObject {

    private static final DecimalFormat decimalFormat;
    private static final SimpleDateFormat simpleDateFormate;

    static {
        decimalFormat = new DecimalFormat("#00.0");

        simpleDateFormate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        simpleDateFormate.setTimeZone(TimeZone.getDefault());
    }

    private float value;
    private long date;

    public float getValue() {
        return value;
    }

    public String getValueByString() {
        return decimalFormat.format(value);
    }

    /**
     * Get integer part of the value.
     */
    public int getFistPartOfValue() {
        return (int) value;
    }

    /**
     * Get fractional part of the value.
     */
    public int getSecondPartOfValue() {
        return Math.round((value - (int) value) * 10);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public long getDate() {
        return date;
    }

    public String getDateByString() {
        return simpleDateFormate.format(new Date(this.date));
    }

    public void setDate(long date) {
        this.date = date;
    }
}
