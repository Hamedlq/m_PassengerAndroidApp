package com.mibarim.main.RestInterfaces;


import com.mibarim.main.core.Constants;
import com.mibarim.main.models.Address.AddressResult;
import com.mibarim.main.models.Address.AutoCompleteResult;
import com.mibarim.main.models.Address.DetailPlaceResult;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Hamed on 3/14/2016.
 */
public interface GetAddressService {
    @GET(Constants.Geocoding.SERVICE_URL)
    AddressResult getAddress(@Query(Constants.Geocoding.LAT_LONG_KEY) String latlong,
                           @Query(Constants.Geocoding.LANGUAGE_KEY) String language,
                           @Query(Constants.Geocoding.LOCATION_TYPE_VALUE) String locationType,
                           @Query(Constants.Geocoding.GOOGLE_SERVICE_KEY) String googleKey);

    @GET(Constants.Geocoding.AUTOCOMPLETE_SERVICE_URL)
    AutoCompleteResult getAutocomplete(@Query(Constants.Geocoding.AUTOCOMPLETE_INPUT) String searchText,
                                       @Query(Constants.Geocoding.LANGUAGE_KEY) String language,
                                       @Query(Constants.Geocoding.GOOGLE_AUTOCOMPLETE_SERVICE_KEY) String googleKey);

    @GET(Constants.Geocoding.DETAIL_SERVICE_URL)
    DetailPlaceResult getPlaceDetail(@Query(Constants.Geocoding.PLACE_ID_KEY) String placeId,
                                     @Query(Constants.Geocoding.LANGUAGE_KEY) String language,
                                     @Query(Constants.Geocoding.GOOGLE_AUTOCOMPLETE_SERVICE_KEY) String googleKey);


}
