package com.parvez.weatherapp.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parvez.weatherapp.R;
import com.parvez.weatherapp.adepter.CityInfoListAdepter;
import com.parvez.weatherapp.model.CityInfoListClass;
import com.parvez.weatherapp.viewModel.DashBoardViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.city_weather_recyclerView)
    RecyclerView cityWeatherRecyclerView;
    DashBoardViewModel dashBoardViewModel;

    private CityInfoListAdepter adepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        dashBoardViewModel = ViewModelProviders.of(this).get(DashBoardViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        cityWeatherRecyclerView.setLayoutManager(layoutManager);

        dashBoardViewModel.getAllCityList().observe(this, cityInfoListClass -> {

            if (dashBoardViewModel.isSuccessful(cityInfoListClass)) {

                List<CityInfoListClass> cityList = new ArrayList<>();
                if (cityInfoListClass != null) {
                    cityList.add(cityInfoListClass);
                }
                adepter = new CityInfoListAdepter(DashboardActivity.this, cityList);
                cityWeatherRecyclerView.setAdapter(adepter);
                cityWeatherRecyclerView.setLayoutManager(layoutManager);
            }
        });
    }
}
