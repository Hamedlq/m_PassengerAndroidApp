package com.mibarim.main.models.Address;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hamed on 3/15/2016.
 */
public class AutoCompleteResult implements Serializable {
    public List<Place> predictions;
    public String status;
}
