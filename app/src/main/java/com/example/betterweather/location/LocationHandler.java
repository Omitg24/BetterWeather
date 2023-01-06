package com.example.betterweather.location;

import android.location.Location;

public interface LocationHandler {
    public void handleSuccess(Location location);
    public void handleFailure();
    public void handleNoPermission();
}
