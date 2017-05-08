package com.mibarim.main.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum EventTypes {

    @SerializedName("1")
    Goto("Goto", 1),
    @SerializedName("2")
    ReturnFrom("ReturnFrom", 2),
    @SerializedName("3")
    GoReturn("GoReturn", 3);

    private String stringValue;
    private int intValue;

    private EventTypes(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


