package com.mibarim.main.models.UserInfo;

import com.mibarim.main.models.Address.PathPoint;

import java.io.Serializable;

/**
 * Created by Hamed on 4/6/2016.
 */
public class UserRouteModel implements Serializable {
    public int RouteId;
    public String Name;
    public String Family;
    public boolean IsVerified;
    public String UserImageId;
    public String UserAboutme;
    public String TimingString;
    public String PricingString;
    public String CarString;
    public String SrcDistance;
    public String SrcLatitude;
    public String SrcLongitude;
    public String SrcAddress;
    public String DstAddress;
    public String DstDistance;
    public String DstLatitude;
    public String DstLongitude;
    public int TripCount;
    public float UserRating;
    public int AccompanyCount;
    public boolean IsDrive;
    public boolean Sat;
    public boolean Sun;
    public boolean Mon;
    public boolean Tue;
    public boolean Wed;
    public boolean Thu;
    public boolean Fri;

}
