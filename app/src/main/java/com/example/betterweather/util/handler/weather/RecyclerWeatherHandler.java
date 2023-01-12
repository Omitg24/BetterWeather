package com.example.betterweather.util.handler.weather;

import com.example.betterweather.modelo.weatherpojos.TemperaturaData;
import com.example.betterweather.modelo.recycler.LineaReciclerFav;
import com.example.betterweather.util.weather.WeatherUtil;
import com.example.betterweather.modelo.info.weather.WeatherCallInfo;
import com.example.betterweather.util.handler.WeatherHandler;

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
        lineaReciclerFav.getTemperatura().setText(temperaturaData.getList().get(0).getMain().getTemp() + WeatherUtil.getUnitLetter(weatherCallInfo.getUnits()));
        WeatherUtil.updateImage(lineaReciclerFav.getImagen(), temperaturaData.getList().get(0).getWeather().get(0).getDescription());
    }

    @Override
    public void handleFailure() {

    }

    @Override
    public void handleNotExists() {

    }
}
