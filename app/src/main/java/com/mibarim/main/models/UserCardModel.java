package com.mibarim.main.models;


import android.graphics.drawable.Drawable;

import com.mibarim.main.models.Route.GroupModel;
import com.mibarim.main.models.Route.RouteGroupModel;
import com.mibarim.main.models.enums.UserCardTypes;

import java.io.Serializable;
import java.util.List;

public class UserCardModel implements Serializable {

    public String CardTitle;
    public Drawable CardIcon;
    public String CardDiscount;
    public UserCardTypes CardType;
}
