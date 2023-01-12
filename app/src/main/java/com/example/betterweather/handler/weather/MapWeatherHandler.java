package com.example.betterweather.handler.weather;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.betterweather.MapsActivity;
import com.example.betterweather.R;
import com.example.betterweather.modelo.TemperaturaData;
import com.example.betterweather.util.WeatherUtil;
import com.example.betterweather.weather.WeatherHandler;

public class MapWeatherHandler implements WeatherHandler {

    private MapsActivity activity;

    public MapWeatherHandler(MapsActivity mapsActivity) {
        this.activity = mapsActivity;
    }

    @Override
    public void handleSuccess(TemperaturaData result) {
        TextView lugar = activity.findViewById(R.id.valorLugarInfo);
        ImageView condicion = activity.findViewById(R.id.condicionInfo);
        TextView estado = activity.findViewById(R.id.valorEstadoInfo);
        TextView temperatura = activity.findViewById(R.id.valorTemperaturaInfo);
        TextView latitud = activity.findViewById(R.id.valorLatitudInfo);
        TextView longitud = activity.findViewById(R.id.valorLongitudInfo);

        String tmp = result.getList().get(0).getMain().getTemp() + " ºC";

        temperatura.setText(tmp);

        WeatherUtil.updateImage(condicion, result.getList().get(0).getWeather().get(0).getDescription());
        WeatherUtil.updateText(estado, result.getList().get(0).getWeather().get(0).getDescription());

        lugar.setText(result.getList().get(0).getName());
        latitud.setText(String.valueOf(result.getList().get(0).getCoord().getLat()));
        longitud.setText(String.valueOf(result.getList().get(0).getCoord().getLon()));
    }

    @Override
    public void handleFailure() { }

    @Override
    public void handleNotExists() { }
}
