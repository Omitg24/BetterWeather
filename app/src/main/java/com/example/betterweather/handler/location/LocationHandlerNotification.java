package com.example.betterweather.handler.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.example.betterweather.ApiManager;
import com.example.betterweather.handler.LocationHandler;
import com.example.betterweather.modelo.info.location.LocationInfo;
import com.example.betterweather.modelo.info.weather.WeatherCallInfo;
import com.example.betterweather.handler.WeatherHandler;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHandlerNotification implements LocationHandler {

    private Context context;

    private LocationInfo locationInfo;

    private WeatherHandler notificationWeatherHandler;

    public LocationHandlerNotification(Context context,LocationInfo locationInfo,WeatherHandler notificationWeatherHandler){
        this.context=context;
        this.locationInfo = locationInfo;
        this.notificationWeatherHandler = notificationWeatherHandler;
    }

    @Override
    public void handleSuccess(Location location) {
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        List<Address> dir=null;
        //Seteamos la localizacion a la obtenida mediante la consulta, si hay error indicamos Madrid
        try {
            dir = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationInfo.setLocation(dir.get(0).getLocality());
        } catch (IOException e) {
            locationInfo.setLocation("Madrid");
        }
       executeNotification(locationInfo);
    }

    @Override
    public void handleFailure() {
        locationInfo.setLocation("Madrid");
        executeNotification(locationInfo);
    }

    @Override
    public void handleNoPermission() {
        locationInfo.setLocation("Madrid");
        executeNotification(locationInfo);
    }

    private void executeNotification(LocationInfo locationInfo){
        new ApiManager().getWeather(new WeatherCallInfo(locationInfo.getLocation()),notificationWeatherHandler);
    }
}
