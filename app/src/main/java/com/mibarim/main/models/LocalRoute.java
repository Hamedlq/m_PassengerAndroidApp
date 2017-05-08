package com.mibarim.main.models;

import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.enums.CityLocationTypes;
import com.mibarim.main.models.enums.LocalRouteTypes;

import java.io.Serializable;

/**
 * Created by Hamed on 7/28/2016.
 */
public class LocalRoute implements Serializable {
    public String RouteUId;
    public LocalRouteTypes LocalRouteType;
    public LocationPoint SrcPoint ;
    public LocationPoint DstPoint ;
    public String RouteStartTime ;
    public PathPoint PathRoute;
    public String Name;
    public String Family;

}
