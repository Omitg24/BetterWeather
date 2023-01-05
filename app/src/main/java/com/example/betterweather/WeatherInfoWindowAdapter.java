package com.example.betterweather;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.Locale;

public class WeatherInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater inflater;
    private ApiManager apiManager;

    private TextView lugar;
    private ImageView condicion;
    private TextView estado;
    private TextView temperatura;
    private TextView latitud;
    private TextView longitud;

    public WeatherInfoWindowAdapter(LayoutInflater inflater){
        this.apiManager = new ApiManager();
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.info_window, null);
        lugar = v.findViewById(R.id.valorLugarInfo);
        condicion = v.findViewById(R.id.condicionInfo);
        estado = v.findViewById(R.id.valorEstadoInfo);
        temperatura = v.findViewById(R.id.valorTemperaturaInfo);
        latitud = v.findViewById(R.id.valorLatitudInfo);
        longitud = v.findViewById(R.id.valorLongitudInfo);
        getInfo(m.getPosition().latitude, m.getPosition().longitude);
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

    private void getInfo(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(inflater.getContext(), Locale.getDefault());

        String ciudad = null;
        try {
            ciudad = geocoder.getFromLocation(latitude, longitude, 1).get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lugar.setText(ciudad);
        latitud.setText(String.valueOf(latitude));
        longitud.setText(String.valueOf(longitude));

        apiManager.getWeatherForMapInfo(condicion, estado, temperatura, ciudad);
    }
}