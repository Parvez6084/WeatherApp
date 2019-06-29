package com.parvez.weatherapp.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

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
import com.parvez.weatherapp.utility.Function;
import com.parvez.weatherapp.utility.HelperClass;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
    TextView temperatureImageView;
    private GoogleMap mMap;
    CityInfoListClass.Information information;
    double lat, lon;
    String cityName;
    Typeface weatherFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().hasExtra("list")) {
            String object = getIntent().getStringExtra("list");
             information = new Gson().fromJson(object, CityInfoListClass.Information.class);
        }

        cityNameTextView.setText(information.getName());
        weatherPositionTextView.setText("cloud "+information.getWeather().get(0).getMain());
        humidityTextView.setText("Humidity : "+information.getMain().getHumidity() + "");
        windSpeedTextView.setText("Wind Speed : "+information.getWind().getSpeed() + "");
        maxTempTextView.setText("Max.Temp : "+String.format("%.0f",HelperClass.celsiusFormet(information.getMain().getTempMax()))+"°c");
        minTempTextView.setText("Min.Temp : "+String.format("%.0f",HelperClass.celsiusFormet(information.getMain().getTempMin()))+"°c");
        temperatureTextView.setText(String.format("%.0f",HelperClass.celsiusFormet(information.getMain().getTemp()))+"°c");
        temperatureImageView.setText(information.getWeather().get(0).getIcon());

        lat = information.getCoord().getLat();
        lon = information.getCoord().getLon();
        cityName =information.getName();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng selectLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(selectLocation).title(cityName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}
