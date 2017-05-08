package com.mibarim.main.services;

import com.mibarim.main.RestInterfaces.GetGroupService;
import com.mibarim.main.RestInterfaces.GetRouteResponseService;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.PersonalInfoModel;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/10/2016.
 */
public class GroupService {

    private RestAdapter restAdapter;

    public GroupService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private GetGroupService getService() {
        return getRestAdapter().create(GetGroupService.class);
    }

    public ApiResponse GetMessages(String authToken,String groupId) {
        ApiResponse res = getService().GetMessages("Bearer " + authToken, groupId);
        return res;
    }

    public ApiResponse sendMessage(String authToken, String s, String groupId) {
        ApiResponse res = getService().SendMessage("Bearer " + authToken, groupId, s);
        return res;
    }

    public PersonalInfoModel GetMsgImage(String authToken, long commentId) {
        PersonalInfoModel res = getService().GetMsgUserImage("Bearer " + authToken, commentId);
        return res;
    }
}
