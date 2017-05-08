package com.mibarim.main.models.Route;

import com.mibarim.main.core.Constants;

import java.io.Serializable;

/**
 * Created by Hamed on 4/6/2016.
 */
public class RouteGroupModel extends BriefRouteModel implements Serializable {
    public int RgHolderRrId;
    public int GroupId;
    public boolean RgIsConfimed;

    public String getAvatarUrl() {
        return Constants.Http.URL_BASE+ Constants.Http.ROUTE_IMAGE_URL+"/"+RgHolderRrId;
    }


}
