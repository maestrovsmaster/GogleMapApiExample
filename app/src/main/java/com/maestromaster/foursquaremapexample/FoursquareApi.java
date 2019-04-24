package com.maestromaster.foursquaremapexample;

import com.maestromaster.foursquaremapexample.items._MainResponce;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareApi {


    @GET("/v2/venues/search")
    Call<_MainResponce> searchVenues(@Query("ll") String ll, @Query("client_id") String client_id,
                                     @Query("client_secret") String client_secret, @Query("v") String v);

}
