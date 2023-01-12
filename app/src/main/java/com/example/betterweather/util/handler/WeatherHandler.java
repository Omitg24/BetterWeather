package com.example.betterweather.util.handler;

import com.example.betterweather.modelo.weatherpojos.TemperaturaData;

public interface WeatherHandler {
    public void handleSuccess(TemperaturaData temperaturaData);
    public void handleFailure();
    public void handleNotExists();
}
