package com.mibarim.main.models.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hamed on 3/8/2016.
 */
public enum UserCardTypes {

    @SerializedName("1")
    InviteFriend("InviteFriend", 1),
    @SerializedName("2")
    DiscountCode("DiscountCode", 2),
    @SerializedName("3")
    AboutMe("AboutMe", 3),
    @SerializedName("4")
    UserInfo("UserInfo", 4),
    @SerializedName("5")
    LicenseInfo("LicenseInfo", 5),
    @SerializedName("6")
    CarInfo("CarInfo", 6),
    @SerializedName("7")
    BankInfo("BankInfo", 7),
    @SerializedName("8")
    WithDraw("WithDraw", 8),
    @SerializedName("9")
    Exit("Exit", 9);


    private String stringValue;
    private int intValue;

    private UserCardTypes(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


