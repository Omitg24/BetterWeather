package com.example.betterweather.handler.weather;

import android.app.PendingIntent;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.betterweather.R;
import com.example.betterweather.modelo.weatherpojos.TemperaturaData;
import com.example.betterweather.util.WeatherUtil;
import com.example.betterweather.handler.WeatherHandler;

public class WeatherHandlerNotification implements WeatherHandler {

    private NotificationManagerCompat notificationManager;

    private String description;

    private String temp;

    private Context context;

    private PendingIntent pendingIntent;

    public WeatherHandlerNotification(NotificationManagerCompat notificationManager, Context context, PendingIntent pendingIntent){
        this.notificationManager = notificationManager;
        this.context = context;
        this.pendingIntent = pendingIntent;
    }

    @Override
    public void handleSuccess(TemperaturaData temperaturaData) {
        temp = temperaturaData.getList().get(0).getMain().getTemp() + " ºC" ;
        description = temperaturaData.getList().get(0).getWeather().get(0).getDescription();
        notificationManager.notify(001,new NotificationCompat.Builder(context, "M_CH_ID")
                .setSmallIcon(R.mipmap.ic_few_clouds_foreground)
                .setContentTitle("Tiempo en " + temperaturaData.getList().get(0).getName())
                .setAutoCancel(true)
                .setContentText(WeatherUtil.getSpanishText(description) + ": " + temp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build());
    }

    @Override
    public void handleFailure() {
    }

    @Override
    public void handleNotExists() {
    }
}
