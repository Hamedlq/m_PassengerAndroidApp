package com.mibarim.main.services;


import com.mibarim.main.RestInterfaces.GetAddressService;
import com.mibarim.main.core.Constants;
import com.mibarim.main.core.CustomRestAdapterRequestInterceptor;
import com.mibarim.main.core.RestAdapterRequestInterceptor;
import com.mibarim.main.models.Address.AddressResult;
import com.mibarim.main.models.Address.AutoCompleteResult;
import com.mibarim.main.models.Address.DetailPlaceResult;
import com.mibarim.main.util.DynamicJsonConverter;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/14/2016.
 */
public class AddressService {
    private RestAdapter restAdapter;

    public AddressService() {
        this.restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.Geocoding.SERVICE_BASE_URL)
                .setRequestInterceptor(new CustomRestAdapterRequestInterceptor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new DynamicJsonConverter())
                .build();
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private GetAddressService getAddressService() {

        return getRestAdapter().create(GetAddressService.class);
    }

    public AddressResult getAddress(String latitude, String longitude) {
        AddressResult res = getAddressService().getAddress(latitude +','+ longitude,
                Constants.Geocoding.LANGUAGE_VALUE,
                Constants.Geocoding.LOCATION_TYPE_VALUE,
                Constants.Geocoding.GOOGLE_SERVICE_VALUE);
        return res;
    }
    public AutoCompleteResult getAutocomplete(String searchText) {
        AutoCompleteResult res = getAddressService().getAutocomplete(searchText,
                Constants.Geocoding.LANGUAGE_VALUE,
                Constants.Geocoding.GOOGLE_AUTOCOMPLETE_SERVICE_VALUE);
        return res;
    }

    public DetailPlaceResult getPlaceDetail(String placeId) {
        DetailPlaceResult res = getAddressService().getPlaceDetail(placeId,
                Constants.Geocoding.LANGUAGE_VALUE,
                Constants.Geocoding.GOOGLE_AUTOCOMPLETE_SERVICE_VALUE);
        return res;
    }

}
