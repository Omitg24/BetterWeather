package com.example.betterweather.handler.weatherHandler;

import com.example.betterweather.MainActivity;
import com.example.betterweather.modelo.TemperaturaData;
import com.example.betterweather.modelo.ui.LineaReciclerFav;
import com.example.betterweather.util.WeatherUtil;
import com.example.betterweather.weather.WeatherCallInfo;
import com.example.betterweather.weather.WeatherHandler;

public class RecyclerWeatherHandler implements WeatherHandler {

    private WeatherCallInfo weatherCallInfo;

    private LineaReciclerFav lineaReciclerFav;

    public RecyclerWeatherHandler(LineaReciclerFav lineaReciclerFav,WeatherCallInfo weatherCallInfo){
        this.lineaReciclerFav=lineaReciclerFav;
        this.weatherCallInfo=weatherCallInfo;
    }

    @Override
    public void handleSuccess(TemperaturaData temperaturaData) {
        lineaReciclerFav.getLugar().setText(weatherCallInfo.getCity());
        lineaReciclerFav.getTemperatura().setText(temperaturaData.getList().get(0).getMain().getTemp() + MainActivity.getUnitLetter(weatherCallInfo.getUnits()));
        WeatherUtil.updateImage(lineaReciclerFav.getImagen(), temperaturaData.getList().get(0).getWeather().get(0).getDescription());
    }

    @Override
    public void handleFailure() {

    }

    @Override
    public void handleNotExists() {

    }
}
