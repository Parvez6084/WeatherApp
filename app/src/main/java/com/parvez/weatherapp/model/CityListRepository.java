package com.parvez.weatherapp.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.parvez.weatherapp.network.ApiClient;
import com.parvez.weatherapp.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityListRepository {

    private static final String TAG = "CityListRepository";
    private MutableLiveData<CityInfoListClass> cityListMutableLiveData;

    public MutableLiveData<CityInfoListClass> getCityList(){

        if (cityListMutableLiveData == null){
            cityListMutableLiveData = new MutableLiveData<>();
        }

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CityInfoListClass>call = apiInterface.getCityListApi();
        call.enqueue(new Callback<CityInfoListClass>() {
            @Override
            public void onResponse(@NonNull Call<CityInfoListClass> call,@NonNull Response<CityInfoListClass> response) {
              CityInfoListClass listClass = response.body();
              cityListMutableLiveData.setValue(listClass);
            }

            @Override
            public void onFailure(@NonNull Call<CityInfoListClass> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });

        return cityListMutableLiveData;
    }
}
