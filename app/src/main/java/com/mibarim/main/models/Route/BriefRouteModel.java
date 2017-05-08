package com.mibarim.main.models.Route;

import com.mibarim.main.models.Address.PathPoint;

import java.io.Serializable;

/**
 * Created by Hamed on 4/6/2016.
 */
public class BriefRouteModel implements Serializable {
    public int RouteId;
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
    public int AccompanyCount;
    public boolean IsDrive;
    public boolean IsSuggestSeen;
    public PathPoint PathRoute;
    public boolean IsVerified;

}
