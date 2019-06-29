package com.parvez.weatherapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.parvez.weatherapp.model.CityInfoListClass;
import com.parvez.weatherapp.model.CityListRepository;
import com.parvez.weatherapp.utility.Constants;


public class DashBoardViewModel extends AndroidViewModel {

    private CityListRepository cityListRepository ;

    public DashBoardViewModel(@NonNull Application application) {
        super(application);

        cityListRepository = new CityListRepository();
    }

    public  LiveData<CityInfoListClass> getAllCityList (){
        return cityListRepository.getCityList();
    }

    public boolean isSuccessful(CityInfoListClass cityInfoListClass){
        return cityInfoListClass.getMessage().equals(Constants.MESSAGE);
    }



}
