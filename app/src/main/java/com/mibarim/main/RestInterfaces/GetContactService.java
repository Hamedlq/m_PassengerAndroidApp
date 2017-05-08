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
public interface GetContactService {
    @POST(Constants.Http.URL_GET_CONTACTS)
    @FormUrlEncoded
    ApiResponse GetContacts(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                            @Field("UserId") String s);
}
