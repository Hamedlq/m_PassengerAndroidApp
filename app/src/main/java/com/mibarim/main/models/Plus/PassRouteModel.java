package com.mibarim.main.models.Plus;

import com.mibarim.main.models.Address.PathPoint;

import java.io.Serializable;

/**
 * Created by Hamed on 4/6/2016.
 */
public class PassRouteModel implements Serializable {
    public int TripId;
    public String Name;
    public String Family;
    public String UserImageId;
    public String  TimingString;
    public String  PricingString;
    public String  CarString;
    public String SrcAddress;
    public String SrcDistance;
    public String SrcLatitude;
    public String SrcLongitude;
    public String DstAddress;
    public String DstDistance;
    public String DstLatitude;
    public String DstLongitude;
    public int EmptySeat;
    public int CarSeats;
    public boolean IsVerified;
    public boolean IsBooked;

}
