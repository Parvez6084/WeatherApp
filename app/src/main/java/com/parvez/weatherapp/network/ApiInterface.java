package com.parvez.weatherapp.network;

import com.parvez.weatherapp.model.CityInfoListClass;
import com.parvez.weatherapp.model.CurrentDataClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76")
    Call<CityInfoListClass> getCityListApi();

    @GET("data/2.5/weather")
    Call<CurrentDataClass> getCurrentApi(@Query("lat") double lat,
                                         @Query("lon") double lon,
                                         @Query("appid") String appid
    );


}
