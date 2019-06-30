package com.parvez.weatherapp.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parvez.weatherapp.R;
import com.parvez.weatherapp.adepter.CityInfoListAdepter;
import com.parvez.weatherapp.model.CityInfoListClass;
import com.parvez.weatherapp.service.NotificationService;
import com.parvez.weatherapp.utility.Constants;
import com.parvez.weatherapp.viewModel.DashBoardViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.location.SimpleLocation;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class DashboardActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.city_weather_recyclerView)
    RecyclerView cityWeatherRecyclerView;
    DashBoardViewModel dashBoardViewModel;
    boolean doubleBackToExitPressedOnce = false;
    private CityInfoListAdepter adepter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.text_layout_actionbar);

        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };

        if (EasyPermissions.hasPermissions(this, perms)) {

            AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Intent intent = new Intent(DashboardActivity.this, NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            if (manager != null) {

                manager.setExact(AlarmManager.RTC, calendar.getTimeInMillis() + 2000, pendingIntent);
            }

        } else {

            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, 200, perms)
                            .setRationale("Location permission is required for this operation")
                            .setPositiveButtonText("Allow")
                            .setNegativeButtonText("Later")
                            .build());
        }

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


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent intent = new Intent(DashboardActivity.this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        if (manager != null) {

            manager.setExact(AlarmManager.RTC, calendar.getTimeInMillis() + Constants.NOTIFICATIONTIME, pendingIntent);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



}
