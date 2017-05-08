package com.mibarim.main.models;

import java.io.Serializable;

/**
 * Created by Hamed on 4/9/2016.
 */
public class ContactModel implements Serializable {
    public long ContactId;
    public String Name;
    public String Family;
    public String Gender;
    public String LastMsgTime;
    public String LastMsg;
    public int IsSupport;
    public int IsDriver;
    public int IsRideAccepted;
    public int IsPassengerAccepted;
    public String UserImageId;
    public String Base64UserPic;
    public String AboutUser;
    public int NewChats;

}
