package com.example.betterweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.gridlayout.widget.GridLayout;

import com.example.betterweather.apiUtils.WeatherAPI;
import com.example.betterweather.location.LocationHandler;
import com.example.betterweather.modelo.TemperaturaData;
import com.example.betterweather.modelo.ui.LineaReciclerFav;
import com.example.betterweather.util.WeatherUtil;
import com.example.betterweather.weather.WeatherCallInfo;
import com.example.betterweather.weather.WeatherHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {

    private String paramsUrl;

    private TemperaturaData result;

    private boolean exists;

    private String urlBase = "https://api.openweathermap.org/data/2.5/";

    private String urlMapa = "https://tile.openweathermap.org/map/";

    public final static String URL = "http://openweathermap.org/img/wn/";

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(urlBase).addConverterFactory(GsonConverterFactory.create()).build();

    public void getWeatherForMapInfo(ImageView condicion, TextView estado, TextView temperatura, String city) {
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<TemperaturaData> call = weatherAPI.getCourse(city, "metric");
        call.enqueue(new Callback<TemperaturaData>() {
            @Override
            public void onResponse(Call<TemperaturaData> call, Response<TemperaturaData> response) {
                if (response.isSuccessful()) {
                    result = response.body();
                    if (result.getList().size() != 0) {
                        String tmp = result.getList().get(0).getMain().getTemp() + " ºC";

                        temperatura.setText(tmp);

                        WeatherUtil.updateImage(condicion, result.getList().get(0).getWeather().get(0).getDescription());
                        WeatherUtil.updateText(estado, result.getList().get(0).getWeather().get(0).getDescription());
                    }
                }
            }
            @Override
            public void onFailure(Call<TemperaturaData> call, Throwable t) {}
        });
    }

    public void getWeather(WeatherCallInfo weatherCallInfo, WeatherHandler weatherHandler){
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<TemperaturaData> call = weatherAPI.getCourse(weatherCallInfo.getCity(), weatherCallInfo.getUnits());
        call.enqueue(new Callback<TemperaturaData>() {
            @Override
            public void onResponse(Call<TemperaturaData> call, Response<TemperaturaData> response) {
                if (response.isSuccessful()) {
                    result = response.body();
                    if (result.getList().size() != 0) {
                        weatherHandler.handleSuccess(result);
                        exists=true;
                    } else {
                        weatherHandler.handleNotExists();
                        //No se puede añadir a favoritos
                        exists=false;
                    }
                }
            }
            @Override
            public void onFailure(Call<TemperaturaData> call, Throwable t) {
                weatherHandler.handleFailure();
            }
        });
    }

    public static void findLocation(Context context, FusedLocationProviderClient fusedLocationClient, LocationHandler locationHandler){
        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationHandler.handleNoPermission();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                   locationHandler.handleSuccess(location);
                                } else {
                                    locationHandler.handleFailure();
                                }
                            }
                        }
                );
    }

    public boolean getExists(){
        return exists;
    }
}
