package com.parvez.weatherapp.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.parvez.weatherapp.R;
import com.parvez.weatherapp.service.NotificationService;

import java.util.Calendar;
import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this,DashboardActivity.class);
            finish();
            startActivity(i);
        },3000);
    }
}
