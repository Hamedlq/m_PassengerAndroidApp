package com.mibarim.main.models;

import com.mibarim.main.models.enums.PricingOptions;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.models.enums.TimingOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hamed on 3/8/2016.
 */
public class InviteModel implements Serializable {
    public String InviteCode;
    public String InviteLink;
}
