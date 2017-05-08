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
public interface GetEventService {
    @POST(Constants.Http.URL_GET_EVENT)
    @FormUrlEncoded
    ApiResponse GetEvents(@Field("UserId") String s);
}
