package com.parvez.weatherapp.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.parvez.weatherapp.R;

public class NotificationService extends Service {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public IBinder onBind(Intent intent) {

        if (intent.getExtras() != null) {

            String title = intent.getStringExtra("title");


            Notification.Builder notification = new Notification.Builder(this, "demo")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_launcher_background);
        }


        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }
}
