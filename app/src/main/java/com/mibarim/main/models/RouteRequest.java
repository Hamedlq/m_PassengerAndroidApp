package com.mibarim.main.models;

import com.mibarim.main.models.enums.PricingOptions;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.models.enums.TimingOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hamed on 3/8/2016.
 */
public class RouteRequest {
    public RouteRequest(){
        PricingOption = PricingOptions.MinMax;
    }
    SimpleDateFormat timef = new SimpleDateFormat("HH:mm", Locale.US);
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public ServiceTypes ServiceType;
    public long EventId;
    public String SrcGAddress;
    public String SrcDetailAddress;
    public String SrcLatitude;
    public String SrcLongitude;
    public String DstGAddress;
    public String DstDetailAddress;
    public String DstLatitude;
    public String DstLongitude;
    public TimingOptions TimingOption;
    public Calendar TheTime;
    public boolean IsReturn;
    public Calendar TheReturnTime;
    public Date TheDate;
    public Calendar SatDatetime;
    public Calendar SunDatetime;
    public Calendar MonDatetime;
    public Calendar TueDatetime;
    public Calendar WedDatetime;
    public Calendar ThuDatetime;
    public Calendar FriDatetime;
    public int AccompanyCount;
    public boolean IsDrive;
    public PricingOptions PricingOption;
    public float CostMinMax;
    public long RecommendPathId;

    public String TheDateString() {
        if (TheDate != null)
            return datef.format(TheDate);
        return null;
    }

    public String TheTimeString() {
        if (TheTime != null)
            return timef.format(TheTime.getTime());
        return null;
    }
    public String TheReturnTimeString() {
        if (TheReturnTime!= null)
            return timef.format(TheReturnTime.getTime());
        return null;
    }

    public String SatDatetimeString() {
        if (SatDatetime != null)
            return timef.format(SatDatetime.getTime());
        return null;
    }

    public String SunDatetimeString() {
        if (SunDatetime != null)
            return timef.format(SunDatetime.getTime());
        return null;
    }

    public String MonDatetimeString() {
        if (MonDatetime != null)
            return timef.format(MonDatetime.getTime());
        return null;
    }

    public String TueDatetimeString() {
        if (TueDatetime != null)
            return timef.format(TueDatetime.getTime());
        return null;
    }

    public String WedDatetimeString() {
        if (WedDatetime != null)
            return timef.format(WedDatetime.getTime());
        return null;
    }

    public String ThuDatetimeString() {
        if (ThuDatetime != null)
            return timef.format(ThuDatetime.getTime());
        return null;
    }

    public String FriDatetimeString() {
        if (FriDatetime != null)
            return timef.format(FriDatetime.getTime());
        return null;
    }

}
