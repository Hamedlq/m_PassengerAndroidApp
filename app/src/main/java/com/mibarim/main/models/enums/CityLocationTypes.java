package com.mibarim.main.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum CityLocationTypes {

    @SerializedName("1")
    CityPoint("CityPoint", 1),
    @SerializedName("2")
    Cinema("Cinema", 2),
    @SerializedName("3")
    Theaters("Theaters", 3);

    private String stringValue;
    private int intValue;

    private CityLocationTypes(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


