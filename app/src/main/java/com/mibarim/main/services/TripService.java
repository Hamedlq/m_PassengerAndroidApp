package com.mibarim.main.services;

import com.mibarim.main.RestInterfaces.GetContactService;
import com.mibarim.main.RestInterfaces.GetTripService;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.Trip.TripResponse;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/10/2016.
 */
public class TripService {

    private RestAdapter restAdapter;

    public TripService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private GetTripService getService() {
        return getRestAdapter().create(GetTripService.class);
    }

    public ApiResponse sendUserLocation(String mobile, String latitude, String longitude) {
        ApiResponse res=getService().SendUserLocation(mobile, latitude, longitude);
        return res;
    }

    public ApiResponse getTripInfo(String authToken, Long tripId) {
        ApiResponse res=getService().GetTripInfo("Bearer " + authToken, String.valueOf(tripId));
        return res;
    }

    public ApiResponse endTrip(String authToken, long tripId) {
        ApiResponse res=getService().EndTrip("Bearer " + authToken, String.valueOf(tripId));
        return res;
    }
}
