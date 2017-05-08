package com.mibarim.main.models.enums;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum MainTabs {

    Profile("Profile", 0),
    Message("Message", 1),
    Map("Map", 2),
    //Event("Event", 3),
    Route("Route", 3);

    private String stringValue;
    private int intValue;

    private MainTabs(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int toInt(){return intValue;}
}


