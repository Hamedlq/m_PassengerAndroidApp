package com.mibarim.main.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum LocalRouteTypes {

    @SerializedName("1")
    Driver("Driver", 1),
    @SerializedName("2")
    Passenger("Passenger", 2);

    private String stringValue;
    private int intValue;

    private LocalRouteTypes(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


