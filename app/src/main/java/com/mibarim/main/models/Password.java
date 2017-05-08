package com.mibarim.main.models;

import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.enums.CityLocationTypes;

import java.io.Serializable;

/**
 * Created by Hamed on 7/28/2016.
 */
public class Password implements Serializable {
    public String Password;
    public boolean Confirmed;
    public boolean IsUserRegistered;
}
