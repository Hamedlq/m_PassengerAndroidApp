package com.mibarim.main.RestInterfaces;

import com.mibarim.main.core.Constants;
import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.NotificationModel;
import com.mibarim.main.models.Plus.PaymentDetailModel;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.enums.PricingOptions;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.models.enums.TimingOptions;
//import com.squareup.okhttp.Route;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.HEAD;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by Hamed on 3/10/2016.
 */
public interface SaveRouteRequestService {
    @POST(Constants.Http.URL_INSERT_ROUTE)
    @FormUrlEncoded
    ApiResponse SubmitNewRoute(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                               @Field("SrcGAddress") String SrcGAddress,
                               @Field("SrcDetailAddress") String SrcDetailAddress,
                               @Field("SrcLatitude") String SrcLatitude,
                               @Field("SrcLongitude") String SrcLongitude,
                               @Field("DstGAddress") String DstGAddress,
                               @Field("DstDetailAddress") String DstDetailAddress,
                               @Field("DstLatitude") String DstLatitude,
                               @Field("DstLongitude") String DstLongitude,
                               @Field("AccompanyCount") int AccompanyCount,
                               @Field("TimingOption") TimingOptions TimingOption,
                               @Field("PriceOption") PricingOptions PricingOption,
                               @Field("TheDate") String TheDate,
                               @Field("TheTime") String TheTime,
                               @Field("SatDatetime") String SatDatetime,
                               @Field("SunDatetime") String SunDatetime,
                               @Field("MonDatetime") String MonDatetime,
                               @Field("TueDatetime") String TueDatetime,
                               @Field("WedDatetime") String WedDatetime,
                               @Field("ThuDatetime") String ThuDatetime,
                               @Field("FriDatetime") String FriDatetime,
                               @Field("CostMinMax") float CostMinMax,
                               @Field("IsDrive") boolean IsDrive,
                               @Field("RecommendPathId") long RecommendPathId
    );

    @POST(Constants.Http.URL_INSERT_EVENT_ROUTE)
    @FormUrlEncoded
    ApiResponse SubmitNewEventRoute(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                               @Field("EventId") Long EventId,
                               @Field("SrcGAddress") String SrcGAddress,
                               @Field("SrcDetailAddress") String SrcDetailAddress,
                               @Field("SrcLatitude") String SrcLatitude,
                               @Field("SrcLongitude") String SrcLongitude,
                               @Field("DstGAddress") String DstGAddress,
                               @Field("DstDetailAddress") String DstDetailAddress,
                               @Field("DstLatitude") String DstLatitude,
                               @Field("DstLongitude") String DstLongitude,
                               @Field("CostMinMax") float CostMinMax,
                               @Field("IsDrive") boolean IsDrive,
                               @Field("RecommendPathId") long RecommendPathId
    );

    @POST(Constants.Http.URL_CONFIRM_ROUTE)
    @FormUrlEncoded
    ApiResponse ConfirmRoute(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                             @Field("RouteIdsCommaSeprated") String Ids,
                             @Field("ConfirmedText") String ConfirmedText
    );

    @POST(Constants.Http.URL_NOT_CONFIRM_ROUTE)
    @FormUrlEncoded
    ApiResponse NotConfirmRoute(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                @Field("RouteIdsCommaSeprated") String Ids
    );

    @POST(Constants.Http.URL_JOIN_GROUP)
    @FormUrlEncoded
    ApiResponse JoinGroup(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                          @Field("RouteId") String routeId,
                          @Field("GroupId") String groupId
    );

    @POST(Constants.Http.URL_DELETE_GROUP)
    @FormUrlEncoded
    ApiResponse DeleteGroup(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                            @Field("RouteId") String routeId,
                            @Field("GroupId") String groupId
    );

    @POST(Constants.Http.URL_LEAVE_GROUP)
    @FormUrlEncoded
    ApiResponse LeaveGroup(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                           @Field("RouteId") String routeId,
                           @Field("GroupId") String groupId
    );

    @POST(Constants.Http.URL_ACCEPT_ROUTE)
    @FormUrlEncoded
    ApiResponse AcceptSuggestion(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                 @Field("RouteId") String routeId,
                                 @Field("SelfRouteId") String selRouteId
    );

    @POST(Constants.Http.URL_DELETE_ROUTE_SUGGESTION)
    @FormUrlEncoded
    ApiResponse deleteRouteSuggestion(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                      @Field("RouteId") String routeId,
                                      @Field("SelfRouteId") String selRouteId
    );

    @POST(Constants.Http.URL_DELETE_ROUTE)
    @FormUrlEncoded
    ApiResponse deleteRoute(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                      @Field("RouteRequestId") String routeId
    );
    @POST(Constants.Http.URL_SHARE_ROUTE)
    @FormUrlEncoded
    ApiResponse shareRoute(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                      @Field("RouteRequestId") String routeId
    );
    @POST(Constants.Http.URL_NOTIFY_EVENT)
    @FormUrlEncoded
    NotificationModel notify(@Field("Mobile") String mobile);

    @POST(Constants.Http.URL_GET_CITY_LOCATION)
    @FormUrlEncoded
    ApiResponse getCityLocations(@Field("Lat") String lat,@Field("Lng") String lng);

    @POST(Constants.Http.URL_GET_RECOMMEND_ROUTES)
    @FormUrlEncoded
    ApiResponse getGRoute(@Field(Constants.MibarimServer.SRC_LAT) String srcLat,
                          @Field(Constants.MibarimServer.SRC_LNG) String srcLng,
                          @Field(Constants.MibarimServer.DST_LAT) String dstLat,
                          @Field(Constants.MibarimServer.DST_LNG) String dstLng,
                          @Field(Constants.MibarimServer.WAYPOINTS) String waypoint);

    @POST(Constants.Http.URL_GET_LOCAL_ROUTES)
    @FormUrlEncoded
    ApiResponse getLocalRoutes(@Field("Lat") String lat,@Field("Lng") String lng);

    @POST(Constants.Http.URL_PRICE)
    @FormUrlEncoded
    ApiResponse RequestPrice(@Field("SrcLat") String SrcLat,
                             @Field("SrcLng") String SrcLng,
                             @Field("DstLat") String DstLat,
                             @Field("DstLng") String DstLng
    );
    @POST(Constants.Http.URL_REQUEST_RIDE_SHARE)
    @FormUrlEncoded
    ApiResponse requestRideShare(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                 @Field("RouteId") String routeId,
                                 @Field("SelfRouteId") String selRouteId);

    @POST(Constants.Http.URL_BOOK_REQUEST)
    @FormUrlEncoded
    PaymentDetailModel bookRequest(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                   @Field("TripId") int tripId);

    @POST(Constants.Http.URL_ACCEPT_RIDE_SHARE)
    @FormUrlEncoded
    ApiResponse acceptRideShare(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                                 @Field("ContactId") String contactId);

    @POST(Constants.Http.URL_GET_ROUTE_INFO)
    @FormUrlEncoded
    ApiResponse getRouteInfo(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                             @Field("RouteUId") String routeGId);

    @POST(Constants.Http.URL_INSERT_RIDE_REQUEST)
    @FormUrlEncoded
    ApiResponse SubmitRideRequest(@Header(Constants.Http.PARAM_AUTHORIZATION) String authToken,
                               @Field("SrcGAddress") String SrcGAddress,
                               @Field("SrcDetailAddress") String SrcDetailAddress,
                               @Field("SrcLatitude") String SrcLatitude,
                               @Field("SrcLongitude") String SrcLongitude,
                               @Field("DstGAddress") String DstGAddress,
                               @Field("DstDetailAddress") String DstDetailAddress,
                               @Field("DstLatitude") String DstLatitude,
                               @Field("DstLongitude") String DstLongitude,
                               @Field("AccompanyCount") int AccompanyCount,
                               @Field("TimingOption") TimingOptions TimingOption,
                               @Field("PriceOption") PricingOptions PricingOption,
                               @Field("TheDate") String TheDate,
                               @Field("TheTime") String TheTime,
                               @Field("SatDatetime") String SatDatetime,
                               @Field("SunDatetime") String SunDatetime,
                               @Field("MonDatetime") String MonDatetime,
                               @Field("TueDatetime") String TueDatetime,
                               @Field("WedDatetime") String WedDatetime,
                               @Field("ThuDatetime") String ThuDatetime,
                               @Field("FriDatetime") String FriDatetime,
                               @Field("CostMinMax") float CostMinMax,
                               @Field("IsDrive") boolean IsDrive,
                               @Field("ServiceType") ServiceTypes ServiceType,
                               @Field("RouteUId") String RouteUId
    );
}
