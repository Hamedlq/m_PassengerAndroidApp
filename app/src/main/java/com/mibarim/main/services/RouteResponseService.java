package com.mibarim.main.services;

import com.mibarim.main.RestInterfaces.GetRouteResponseService;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.UserInfo.UserRouteModel;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/10/2016.
 */
public class RouteResponseService {

    private RestAdapter restAdapter;

    public RouteResponseService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private GetRouteResponseService getService() {
        return getRestAdapter().create(GetRouteResponseService.class);
    }

    public ApiResponse GetRoutes(String authToken) {
        ApiResponse res = getService().GetUserRoutes("Bearer " + authToken,"7");//7 is for retrofit problem
        return res;
    }

    public PersonalInfoModel GetRouteImage(String authToken, long routeId) {
        PersonalInfoModel res = getService().GetRouteUserImage("Bearer " + authToken, routeId);
        return res;
    }
    public ApiResponse GetRouteSuggests(String authToken, long routeId) {
        ApiResponse res = getService().GetRouteSuggest("Bearer " + authToken, routeId);
        return res;
    }

    public ApiResponse GetPassengerRoutes(String authToken, long filterId) {
        ApiResponse res = getService().GetPassengerRoutes("Bearer " + authToken, filterId);
        return res;
    }


    public ApiResponse GetRouteSimilarSuggests(String authToken, long contactId) {
        ApiResponse res = getService().GetRouteSimilarSuggests("Bearer " + authToken, contactId);
        return res;
    }

    public UserRouteModel GetTripProfileInfo(String authToken, long routeId) {
        UserRouteModel res = getService().GetTripProfileInfo("Bearer " + authToken, routeId);
        return res;
    }
}
