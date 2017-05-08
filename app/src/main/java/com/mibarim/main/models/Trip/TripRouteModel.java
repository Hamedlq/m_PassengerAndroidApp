package com.mibarim.main.models.Trip;

import com.mibarim.main.models.Address.PathPoint;

import java.io.Serializable;

/**
 * Created by Hamed on 7/28/2016.
 */
public class TripRouteModel implements Serializable {
    public String UserName;
    public String UserFamily;
    public String UserMobile;
    public String Lat;
    public String Lng;
    public float PayPrice;
    public boolean IsDriver;
    public boolean IsMe;
    public String UserImageId;
}
