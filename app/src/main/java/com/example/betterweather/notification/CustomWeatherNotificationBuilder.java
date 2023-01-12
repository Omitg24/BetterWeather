package com.example.betterweather.notification;

import android.app.PendingIntent;
import android.content.Context;

import androidx.core.app.NotificationManagerCompat;

import com.example.betterweather.ApiManager;
import com.example.betterweather.handler.location.LocationHandlerNotification;
import com.example.betterweather.handler.weather.WeatherHandlerNotification;
import com.example.betterweather.location.LocationInfo;
import com.google.android.gms.location.LocationServices;

public class CustomWeatherNotificationBuilder {

    public static void createNotificationByLocationAndWeather(Context context, PendingIntent pendingIntent, NotificationManagerCompat notificationManager) {

        LocationInfo locationInfo = new LocationInfo();

        WeatherHandlerNotification notificationWeatherHandler =
                new WeatherHandlerNotification(notificationManager, context, pendingIntent);

        ApiManager.findLocation(context, LocationServices.getFusedLocationProviderClient(context),
                new LocationHandlerNotification(context,locationInfo,notificationWeatherHandler));
    }
}
