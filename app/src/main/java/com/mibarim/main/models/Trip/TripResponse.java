package com.mibarim.main.models.Trip;

import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.enums.CityLocationTypes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hamed on 7/28/2016.
 */
public class TripResponse implements Serializable {
    public long TripId;
    public String CarInfo;
    public LocationPoint SrcPoint ;
    public LocationPoint DstPoint ;
    public PathPoint PathRoute;
    public List<TripRouteModel> TripRoutes;
}
