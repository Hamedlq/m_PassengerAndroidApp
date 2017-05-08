package com.mibarim.main.models;

import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.enums.CityLocationTypes;

import java.io.Serializable;

/**
 * Created by Hamed on 7/28/2016.
 */
public class CityLocation implements Serializable {
    public CityLocationTypes CityLocationType;
    public String ShortName;
    public String FullName;
    public LocationPoint CityLocationPoint;
}
