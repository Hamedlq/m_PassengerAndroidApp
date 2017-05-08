package com.mibarim.main.models.enums;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum ServiceTypes {

    Private("Private", 1),
    RideShare("RideShare", 2),
    EventRide("EventRide", 3),
    RideRequest("RideRequest", 4),
    WorkRequest("WorkRequest", 5);

    private String stringValue;
    private int intValue;

    private ServiceTypes(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


