package com.mibarim.main.services;

import com.google.gson.Gson;
import com.mibarim.main.RestInterfaces.SaveRouteRequestService;
import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.models.ConfirmationModel;
import com.mibarim.main.models.NotificationModel;
import com.mibarim.main.models.Plus.PaymentDetailModel;
import com.mibarim.main.models.RouteRequest;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/10/2016.
 */
public class RouteRequestService {

    private RestAdapter restAdapter;

    public RouteRequestService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private SaveRouteRequestService getService() {
        return getRestAdapter().create(SaveRouteRequestService.class);
    }

    public ApiResponse SubmitNewRoute(String authToken, RouteRequest routeRequest) {
        ApiResponse res = getService().SubmitNewRoute("Bearer " + authToken,
                routeRequest.SrcGAddress,
                routeRequest.SrcDetailAddress,
                routeRequest.SrcLatitude,
                routeRequest.SrcLongitude,
                routeRequest.DstGAddress,
                routeRequest.DstDetailAddress,
                routeRequest.DstLatitude,
                routeRequest.DstLongitude,
                routeRequest.AccompanyCount,
                routeRequest.TimingOption,
                routeRequest.PricingOption,
                routeRequest.TheDateString(),
                routeRequest.TheTimeString(),
                routeRequest.SatDatetimeString(),
                routeRequest.SunDatetimeString(),
                routeRequest.MonDatetimeString(),
                routeRequest.TueDatetimeString(),
                routeRequest.WedDatetimeString(),
                routeRequest.ThuDatetimeString(),
                routeRequest.FriDatetimeString(),
                routeRequest.CostMinMax,
                routeRequest.IsDrive,
                routeRequest.RecommendPathId
        );
        return res;
    }

    public ApiResponse SubmitNewEventRoute(String authToken, RouteRequest routeRequest) {
        ApiResponse res = getService().SubmitNewEventRoute("Bearer " + authToken,
                routeRequest.EventId,
                routeRequest.SrcGAddress,
                routeRequest.SrcDetailAddress,
                routeRequest.SrcLatitude,
                routeRequest.SrcLongitude,
                routeRequest.DstGAddress,
                routeRequest.DstDetailAddress,
                routeRequest.DstLatitude,
                routeRequest.DstLongitude,
                routeRequest.CostMinMax,
                routeRequest.IsDrive,
                routeRequest.RecommendPathId
        );
        return res;
    }

    public ApiResponse ConfirmRoute(String authToken, ConfirmationModel confirmModel) {
        ApiResponse res = getService().ConfirmRoute("Bearer " + authToken,
                confirmModel.Ids,
                confirmModel.ConfirmedText
        );
        return res;
    }

    public ApiResponse notConfirmRoute(String authToken, ConfirmationModel confirmModel) {
        ApiResponse res = getService().NotConfirmRoute("Bearer " + authToken,
                confirmModel.Ids
        );
        return res;
    }


    public ApiResponse joinGroup(String authToken, String routeId, String groupId) {
        ApiResponse res = getService().JoinGroup("Bearer " + authToken,
                routeId,
                groupId
        );
        return res;
    }

    public ApiResponse deleteGroup(String authToken, String routeId, String groupId) {
        ApiResponse res = getService().DeleteGroup("Bearer " + authToken,
                routeId,
                groupId
        );
        return res;
    }


    public ApiResponse leaveGroup(String authToken, String routeId, String groupId) {
        ApiResponse res = getService().LeaveGroup("Bearer " + authToken,
                routeId,
                groupId
        );
        return res;
    }

    public ApiResponse acceptSuggestion(String authToken, String routeId, String selRouteId) {
        ApiResponse res = getService().AcceptSuggestion("Bearer " + authToken,
                routeId,
                selRouteId
        );
        return res;
    }

    public ApiResponse deleteRouteSuggestion(String authToken, String routeId, String selRouteId) {
        ApiResponse res = getService().deleteRouteSuggestion("Bearer " + authToken,
                routeId,
                selRouteId
        );
        return res;
    }


    public ApiResponse deleteRoute(String authToken, String routeId) {
        ApiResponse res = getService().deleteRoute("Bearer " + authToken,
                routeId
        );
        return res;
    }
    public ApiResponse shareRoute(String authToken, String routeId) {
        ApiResponse res = getService().shareRoute("Bearer " + authToken,
                routeId
        );
        return res;
    }

    public NotificationModel notify(String mobileNo) {
        NotificationModel res = getService().notify(mobileNo);
        return res;
    }

    public ApiResponse getCityLocations(String lat, String lng) {
        ApiResponse res = getService().getCityLocations(lat, lng);
        return res;
    }

    public ApiResponse getGoogleRoute(String srcLatitude, String srcLongitude, String dstLatitude, String dstLongitude, List<Location> wayPoints) {
        Gson gson = new Gson();
        String waypointsjson = gson.toJson(wayPoints);
        ApiResponse res = getService().getGRoute(srcLatitude, srcLongitude, dstLatitude, dstLongitude, waypointsjson);
        return res;
    }

    public ApiResponse getLocalRoutes(String latitude, String longitude) {
        ApiResponse res = getService().getLocalRoutes(latitude, longitude);
        return res;
    }

    public ApiResponse RequestPrice(String srcLat, String srcLng, String dstLat, String dstLng) {
        ApiResponse res = getService().RequestPrice(
                srcLat,
                srcLng,
                dstLat,
                dstLng
        );
        return res;
    }

    public ApiResponse requestRideShare(String authToken, String routeId, String selRouteId) {
        ApiResponse res = getService().requestRideShare("Bearer " + authToken,
                routeId,
                selRouteId
        );
        return res;
    }

    public PaymentDetailModel bookRequest(String authToken, int tripId) {
        PaymentDetailModel res = getService().bookRequest("Bearer " + authToken,tripId);
        return res;
    }

    public ApiResponse acceptRideShare(String authToken, String contactId) {
        ApiResponse res = getService().acceptRideShare("Bearer " + authToken,
                contactId);
        return res;
    }

    public ApiResponse getRouteInfo(String authToken, String routeUId) {
        ApiResponse res = getService().getRouteInfo("Bearer " + authToken,
                routeUId);
        return res;
    }

    public ApiResponse SubmitRideRequest(String authToken, RouteRequest routeRequest, String routeUId) {
        ApiResponse res = getService().SubmitRideRequest("Bearer " + authToken,
                routeRequest.SrcGAddress,
                routeRequest.SrcDetailAddress,
                routeRequest.SrcLatitude,
                routeRequest.SrcLongitude,
                routeRequest.DstGAddress,
                routeRequest.DstDetailAddress,
                routeRequest.DstLatitude,
                routeRequest.DstLongitude,
                routeRequest.AccompanyCount,
                routeRequest.TimingOption,
                routeRequest.PricingOption,
                routeRequest.TheDateString(),
                routeRequest.TheTimeString(),
                routeRequest.SatDatetimeString(),
                routeRequest.SunDatetimeString(),
                routeRequest.MonDatetimeString(),
                routeRequest.TueDatetimeString(),
                routeRequest.WedDatetimeString(),
                routeRequest.ThuDatetimeString(),
                routeRequest.FriDatetimeString(),
                routeRequest.CostMinMax,
                routeRequest.IsDrive,
                routeRequest.ServiceType,
                routeUId
        );
        return res;
    }
}