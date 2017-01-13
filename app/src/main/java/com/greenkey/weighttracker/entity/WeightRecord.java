package com.greenkey.weighttracker.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.RealmObject;

/**
 * Created by Alexander on 28.12.2016.
 */
public class WeightRecord extends RealmObject {

    private static final SimpleDateFormat simpleDateFormate;

    static {
        simpleDateFormate = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        simpleDateFormate.setTimeZone(TimeZone.getDefault());
    }

    private float value;
    private long date;

    public float getValue() {
        return value;
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
