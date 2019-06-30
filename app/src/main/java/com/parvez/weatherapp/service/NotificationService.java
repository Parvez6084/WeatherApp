package com.parvez.weatherapp.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.parvez.weatherapp.R;
import com.parvez.weatherapp.model.CurrentDataClass;
import com.parvez.weatherapp.network.ApiClient;
import com.parvez.weatherapp.network.ApiInterface;
import com.parvez.weatherapp.utility.HelperClass;
import com.parvez.weatherapp.view.DashboardActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import im.delight.android.location.SimpleLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends BroadcastReceiver {

    private String title;
    private String body;
    private double temp =0;

    private String smallIcon,w_icon;
    private String largeIcon;

    String appid = "c11ce0d04f6389be490cd6acd4631dd0";
    private static final String TAG = "NotificationService";


    @Override
    public void onReceive(Context context, Intent intent) {


        networkCall(context, intent);
//            showNotification(context, title, body, intent);
    }

    private void networkCall(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                SimpleLocation location = new SimpleLocation(context);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<CurrentDataClass> call = apiInterface.getCurrentApi(latitude, longitude, appid);
                call.enqueue(new Callback<CurrentDataClass>() {
                    @Override
                    public void onResponse(Call<CurrentDataClass> call, Response<CurrentDataClass> response) {

                        CurrentDataClass currentDataClass = response.body();

                        Toast.makeText(context, "Api Called", Toast.LENGTH_SHORT).show();

                        if (currentDataClass != null) {

                            String content = "Current weather at " + getCelsiusFromKelvin(currentDataClass.getMain().getTemp())
                                    + "°C";
                            showNotification(context, "WeatherApp", content,
                                    currentDataClass.getWeather().get(0).getIcon(), intent);
                        }
                        //w_icon = currentDataClass.getWeather().get(0).getIcon();
                        //    temp = String.format("%.0f",HelperClass.celsiusFormet(currentDataClass.getMain().getTemp()));

                    }

                    @Override
                    public void onFailure(Call<CurrentDataClass> call, Throwable t) {

                        Log.d(TAG, "onFailure: ");
                    }
                });
            }
        }


        title = "WeatherApp";
    }

    public void showNotification(Context context, String title, String body, String url, Intent intent) {

        new SendNotification(context, intent)
                .execute(url, title, body);
    }

    private String getCelsiusFromKelvin(double kelvin) {

        return String.valueOf(kelvin - 273.15);
    }
}

class SendNotification extends AsyncTask<String, Void, Bitmap> {

    private Context context;
    private String title;
    private String body;
    private Intent intent;

    SendNotification(Context context, Intent intent) {

        super();
        this.context = context;
        this.intent = intent;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        title = params[1];
        body = params[2];

        try {

            String data = "https://openweathermap.org/img/wn/" + params[0] + ".png";
            URL url = new URL(data);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            return bitmap;

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        super.onPostExecute(result);

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
                .setContentText(body)
                .setLargeIcon(result);

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
