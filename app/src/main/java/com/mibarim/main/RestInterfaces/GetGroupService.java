package com.mibarim.main.RestInterfaces;

import com.mibarim.main.core.Constants;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.enums.PricingOptions;
import com.mibarim.main.models.enums.TimingOptions;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by Hamed on 3/10/2016.
 */
public interface GetGroupService {
    @POST(Constants.Http.URL_GET_MESSAGE)
    @FormUrlEncoded
    ApiResponse GetMessages(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                               @Field("GroupId") String groupId);

    @POST(Constants.Http.URL_SEND_MESSAGE)
    @FormUrlEncoded
    ApiResponse SendMessage(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                            @Field("GroupId") String groupId,
                            @Field("Comment") String s);

    @POST(Constants.Http.MESSAGE_IMAGE_URL)
    @FormUrlEncoded
    PersonalInfoModel GetMsgUserImage(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                        @Field("CommentId") long Id);
}
