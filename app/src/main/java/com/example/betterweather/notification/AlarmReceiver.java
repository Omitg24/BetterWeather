package com.example.betterweather.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.example.betterweather.MainActivity;
import com.example.betterweather.notification.CustomWeatherNotificationBuilder;

public class AlarmReceiver extends BroadcastReceiver {
    private int mid = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        CustomWeatherNotificationBuilder.createNotificationByLocationAndWeather(context,pendingIntent,notificationManager);
    }
}
