package com.parvez.weatherapp.network;

import com.parvez.weatherapp.model.CityInfoListClass;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("data/2.5/find?lat=23.68&lon=90.35&cnt=50&appid=e384f9ac095b2109c751d95296f8ea76")
    Call<CityInfoListClass> getCityListApi();
}
