package com.parvez.weatherapp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.parvez.weatherapp.R;
import com.parvez.weatherapp.model.CurrentDataClass;
import com.parvez.weatherapp.network.ApiClient;
import com.parvez.weatherapp.network.ApiInterface;
import com.parvez.weatherapp.utility.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends BroadcastReceiver {

    private String title;
    private String body;
    private double temp;

    private String smallIcon,w_icon;
    private String largeIcon;

    double lat,lon;
    String appid = "e384f9ac095b 2109c751d95296f8ea76";
    private static final String TAG = "NotificationService";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

       // networkCall();
        showNotification(context,title,body,intent);
    }

   /* private void networkCall() {


   *//*     LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();*//*

        //lat lng

        lat =35;
        lon=139;


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CurrentDataClass> call = apiInterface.getCurrentApi(lat,lon,appid);
        call.enqueue(new Callback<CurrentDataClass>() {
            @Override
            public void onResponse(Call<CurrentDataClass> call, Response<CurrentDataClass> response) {
                CurrentDataClass currentDataClass = response.body();
                //w_icon = currentDataClass.getWeather().get(0).getIcon();
                temp = HelperClass.celsiusFormet(currentDataClass.getMain().getTemp());

            }

            @Override
            public void onFailure(Call<CurrentDataClass> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });


        title = "WeatherApp";
        body = "Current Temperature "+temp+"°c";
        largeIcon = "https://openweathermap.org/img/wn/"+w_icon+".png";

    }*/

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
