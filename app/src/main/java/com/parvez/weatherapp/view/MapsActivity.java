package com.parvez.weatherapp.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.parvez.weatherapp.R;
import com.parvez.weatherapp.model.CityInfoListClass;
import com.parvez.weatherapp.utility.Constants;
import com.parvez.weatherapp.utility.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.cityName_textView)
    TextView cityNameTextView;
    @BindView(R.id.weather_position_textView)
    TextView weatherPositionTextView;
    @BindView(R.id.humidity_textView)
    TextView humidityTextView;
    @BindView(R.id.windSpeed_textView)
    TextView windSpeedTextView;
    @BindView(R.id.maxTemp_textView)
    TextView maxTempTextView;
    @BindView(R.id.minTemp_textView)
    TextView minTempTextView;
    @BindView(R.id.temperature_textView)
    TextView temperatureTextView;
    @BindView(R.id.temperatureIcon_textView)
    ImageView temperatureImageView;



    private GoogleMap mMap;
    CityInfoListClass.Information information;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (getIntent().hasExtra(Constants.LIST)) {
            String object = getIntent().getStringExtra(Constants.LIST);
            information = new Gson().fromJson(object, CityInfoListClass.Information.class);
        }

        cityNameTextView.setText(information.getName());
        weatherPositionTextView.setText(information.getWeather().get(0).getMain());
        humidityTextView.setText("Humidity : " + information.getMain().getHumidity() + "");
        windSpeedTextView.setText("Wind Speed : " + information.getWind().getSpeed() + "");
        maxTempTextView.setText("Max.Temp : " + String.format("%.0f", HelperClass.celsiusFormet(information.getMain().getTempMax())) + "°c");
        minTempTextView.setText("Min.Temp : " + String.format("%.0f", HelperClass.celsiusFormet(information.getMain().getTempMin())) + "°c");
        temperatureTextView.setText(String.format("%.0f", HelperClass.celsiusFormet(information.getMain().getTemp())) + "°c");
        Glide.with(this).load("https://openweathermap.org/img/wn/"+information.getWeather().get(0).getIcon()+"@2x.png").into(temperatureImageView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng selectLocation = new LatLng(information.getCoord().getLat(), information.getCoord().getLon());
        mMap.addMarker(new MarkerOptions().position(selectLocation).title(information.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}
