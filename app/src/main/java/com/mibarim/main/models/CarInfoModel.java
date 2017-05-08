package com.mibarim.main.models;

import java.io.Serializable;

/**
 * Created by Hamed on 4/12/2016.
 */
public class CarInfoModel implements Serializable {
    public String CarType;
    public String CarPlateNo;
    public String CarColor;
    //public HttpPostedFileBase CarCardPic { set; get; }
    public String Base64CarCardPic;
    public String Base64CarCardBckPic;
}
