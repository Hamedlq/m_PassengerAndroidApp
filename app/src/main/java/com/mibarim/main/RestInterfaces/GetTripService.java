package com.mibarim.main.RestInterfaces;

import com.mibarim.main.core.Constants;
import com.mibarim.main.models.ApiResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by Hamed on 3/10/2016.
 */
public interface GetTripService {
    @POST(Constants.Http.URL_USER_TRIP_LOCATION)
    @FormUrlEncoded
    ApiResponse SendUserLocation(@Field("Mobile") String mobile, @Field("Lat") String latitude,
                                 @Field("Lng") String longitude
    );

    @POST(Constants.Http.URL_GET_TRIP_INFO)
    @FormUrlEncoded
    ApiResponse GetTripInfo(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                             @Field("TripId") String tripId
    );

    @POST(Constants.Http.URL_END_TRIP)
    @FormUrlEncoded
    ApiResponse EndTrip(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                            @Field("TripId") String tripId
    );
}
