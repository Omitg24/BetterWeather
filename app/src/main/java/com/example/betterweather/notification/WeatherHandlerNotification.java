package com.example.betterweather.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.example.betterweather.ApiManager;
import com.example.betterweather.R;
import com.example.betterweather.weather.WeatherHandler;
import com.example.betterweather.modelo.TemperaturaData;

public class WeatherHandlerNotification implements WeatherHandler {

    private NotificationManager notificationManager;

    private String description;

    private String temp;

    private boolean notificable;

    private Context context;

    private PendingIntent pendingIntent;

    public WeatherHandlerNotification(NotificationManager notificationManager, Context context, PendingIntent pendingIntent){
        this.notificationManager = notificationManager;
        this.context = context;
        this.pendingIntent = pendingIntent;
    }

    @Override
    public void handleSuccess(TemperaturaData temperaturaData) {
        temp =temperaturaData.getList().get(0).getMain().getTemp() + "ºC" ;
        description = temperaturaData.getList().get(0).getWeather().get(0).getDescription();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager.notify(001,new NotificationCompat.Builder(context, "M_CH_ID")
                .setSmallIcon(R.mipmap.ic_few_clouds_foreground)
                .setContentTitle("Tiempo en "+temperaturaData.getList().get(0).getName())
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentText(ApiManager.getSpanishText(description) + temp)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build());
    }

    @Override
    public void handleFailure() {
        notificable=false;
    }

    @Override
    public void handleNotExists() {
        notificable=false;
    }

    public String getDescription() {
        return description;
    }

    public String getTemp() {
        return temp;
    }

    public boolean isNotificable() {
        return notificable;
    }
}
