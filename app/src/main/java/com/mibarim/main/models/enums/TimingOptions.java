package com.mibarim.main.models.enums;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum TimingOptions {

    Now("Now", 1),
    Today("Today", 2),
    InDateAndTime("InDateAndTime", 3),
    Weekly("Weekly", 4),
    InWeek("InWeek", 5);

    private String stringValue;
    private int intValue;

    private TimingOptions(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


