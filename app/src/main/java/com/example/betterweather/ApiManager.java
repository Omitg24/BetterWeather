package com.example.betterweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.betterweather.apiUtils.WeatherAPI;
import com.example.betterweather.location.LocationHandler;
import com.example.betterweather.modelo.weatherPojos.TemperaturaData;
import com.example.betterweather.weather.WeatherCallInfo;
import com.example.betterweather.weather.WeatherHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {
    private TemperaturaData result;

    private boolean exists;

    private String urlBase = "https://api.openweathermap.org/data/2.5/";

    private String urlBaseWebcam = "https://api.windy.com/api/webcams/v2/list/";

    private String urlMapa = "https://tile.openweathermap.org/map/";

    public final static String URL = "http://openweathermap.org/img/wn/";

    private String tileType = "clouds";

    private Retrofit retrofit;

    public void getWeather(WeatherCallInfo weatherCallInfo, WeatherHandler weatherHandler){
        retrofit = new Retrofit.Builder().baseUrl(urlBase).addConverterFactory(GsonConverterFactory.create()).build();
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

    public TileProvider createTileProvider() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public java.net.URL getTileUrl(int x, int y, int zoom) {
                String fUrl = urlMapa + (tileType == null ? "clouds_new" : tileType) + "/"
                        + zoom + "/" + x + "/" + y + ".png?appid=43659c6dc1582c2ec51e7a6ce96d6c7d";
                Log.i("Result", fUrl);
                URL url = null;
                try {
                    url = new URL(fUrl);
                }
                catch(MalformedURLException mfe) {
                    mfe.printStackTrace();
                }
                return url;
            }
        };
        return tileProvider;
    }

    public void checkTileType(int position) {
        switch (position) {
            case 0:
                tileType = "clouds_new";
                break;
            case 1:
                tileType = "temp_new";
                break;
            case 2:
                tileType = "precipitation_new";
                break;
            case 3:
                tileType = "wind_new";
                break;
            case 4:
                tileType = "pressure_new";
                break;
        }
    }

    public boolean getExists(){
        return exists;
    }
}
