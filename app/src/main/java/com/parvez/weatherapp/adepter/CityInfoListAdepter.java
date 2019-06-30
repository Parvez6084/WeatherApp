package com.parvez.weatherapp.adepter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.parvez.weatherapp.R;
import com.parvez.weatherapp.model.CityInfoListClass;
import com.parvez.weatherapp.utility.Constants;
import com.parvez.weatherapp.utility.HelperClass;
import com.parvez.weatherapp.view.MapsActivity;

import java.util.List;

public class CityInfoListAdepter extends RecyclerView.Adapter<CityInfoListAdepter.MyViewHolder> {

    private Context context;
    private List<CityInfoListClass> cityLists;

    public CityInfoListAdepter(Context context, List<CityInfoListClass> cityLists) {
        this.context = context;
        this.cityLists = cityLists;
    }

    @NonNull
    @Override
    public CityInfoListAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityInfoListAdepter.MyViewHolder holder, int position) {

        holder.cityName.setText(cityLists.get(0).getList().get(position).getName());
        holder.weather.setText(cityLists.get(0).getList().get(position).getWeather().get(0).getMain());
        holder.temperature.setText(String.format("%.0f",HelperClass.celsiusFormet(cityLists.get(0).getList().get(position).getMain().getTemp()))+"°c");
        holder.itemView.setOnClickListener(view -> {

            Intent i = new Intent(context, MapsActivity.class);
            i.putExtra(Constants.LIST , new Gson().toJson(cityLists.get(0).getList().get(position)));
            context.startActivity(i);

        });
    }

    @Override
    public int getItemCount() {

        return cityLists.get(0).getList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName, weather, temperature;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.city_name_textView);
            weather = itemView.findViewById(R.id.weather_position_textView);
            temperature = itemView.findViewById(R.id.temperature_textView);

        }
    }
}
