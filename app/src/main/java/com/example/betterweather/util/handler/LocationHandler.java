package com.example.betterweather.util.handler;

import android.location.Location;

public interface LocationHandler {
    public void handleSuccess(Location location);
    public void handleFailure();
    public void handleNoPermission();
}
