package com.example.betterweather.weather;

import com.example.betterweather.modelo.weatherPojos.TemperaturaData;

public interface WeatherHandler {
    public void handleSuccess(TemperaturaData temperaturaData);
    public void handleFailure();
    public void handleNotExists();
}
