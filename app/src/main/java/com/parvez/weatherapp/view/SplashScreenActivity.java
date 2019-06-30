package com.parvez.weatherapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

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


        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent intent = new Intent(SplashScreenActivity.this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        if (manager != null) {
            manager.setExact(AlarmManager.RTC, calendar.getTimeInMillis() + 2000, pendingIntent);
        }

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this,DashboardActivity.class);
            finish();
            startActivity(i);
        },3000);
    }
}
