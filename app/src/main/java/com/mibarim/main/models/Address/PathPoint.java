package com.mibarim.main.models.Address;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hamed on 3/17/2016.
 */
public class PathPoint implements Serializable {
    public MetaData metadata;
    public List<LocationPoint> path;
}
