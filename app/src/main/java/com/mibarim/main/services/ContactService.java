package com.mibarim.main.services;

import com.mibarim.main.RestInterfaces.GetContactService;
import com.mibarim.main.RestInterfaces.GetEventService;
import com.mibarim.main.models.ApiResponse;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/10/2016.
 */
public class ContactService {

    private RestAdapter restAdapter;

    public ContactService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private GetContactService getService() {
        return getRestAdapter().create(GetContactService.class);
    }

    public ApiResponse GetContacts(String authToken) {
        ApiResponse res = getService().GetContacts("Bearer " + authToken, "7");
        return res;
    }
}
