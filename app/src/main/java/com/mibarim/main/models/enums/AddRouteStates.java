package com.mibarim.main.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum AddRouteStates {

    SelectGoHomeState("SelectGoHomeState", 1),
    SelectGoWorkState("SelectGoWorkState", 2),
    SelectOriginState("SelectOriginState", 3),
    SelectDestinationState("SelectDestinationState", 4),
    SelectDriveRouteState("SelectDriveRouteState", 5),
    SelectEventOriginState("SelectEventOriginState", 6),
    SelectEventDestinationState("SelectEventDestinationState", 7),
    SelectReturnHomeState("SelectReturnHomeState", 8),
    SelectReturnWorkState("SelectReturnWorkState", 9),
    SelectDriverPassenger("SelectDriverPassenger", 10),
    SelectTime("SelectTime", 11),
    SelectWeek("SelectWeek", 12);

    private String stringValue;
    private int intValue;

    private AddRouteStates(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


