package com.example.betterweather.handler;

import android.location.Location;

public interface LocationHandler {
    public void handleSuccess(Location location);
    public void handleFailure();
    public void handleNoPermission();
}
