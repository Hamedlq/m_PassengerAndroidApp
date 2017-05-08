package com.mibarim.main.models;


import com.mibarim.main.models.enums.EventTypes;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventResponse implements Serializable {

    public long EventId;
    public EventTypes EventType;
    public String Conductor;
    public String Name;
    public String Address;
    public String Latitude;
    public String Longitude;
    public String StartTimeString;
    public String EndTimeString;
    public String Description;
    public String ExternalLink;


}
