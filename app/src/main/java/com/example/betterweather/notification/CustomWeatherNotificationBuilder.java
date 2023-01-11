package com.example.betterweather.notification;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.betterweather.ApiManager;
import com.example.betterweather.location.LocationInfo;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Locale;

public class CustomWeatherNotificationBuilder {

    public static void createNotificationByLocationAndWeather(Context context, PendingIntent pendingIntent, NotificationManagerCompat notificationManager) {

        LocationInfo locationInfo = new LocationInfo();

        WeatherHandlerNotification notificationWeatherHandler =
                new WeatherHandlerNotification(notificationManager, context, pendingIntent);

        ApiManager.findLocation(context, LocationServices.getFusedLocationProviderClient(context),
                new LocationHandlerNotification(context,locationInfo,notificationWeatherHandler));
    }
}
