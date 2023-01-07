package com.example.betterweather;

import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.Locale;

public class WeatherInfoWindowAdapter extends AppCompatActivity implements GoogleMap.InfoWindowAdapter {

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
    public View getInfoContents(final Marker m) {return null;}

    @Override
    public View getInfoWindow(Marker m) {
        View v = inflater.inflate(R.layout.info_window, null);
        lugar = v.findViewById(R.id.valorLugarInfo);
        condicion = v.findViewById(R.id.condicionInfo);
        estado = v.findViewById(R.id.valorEstadoInfo);
        temperatura = v.findViewById(R.id.valorTemperaturaInfo);
        latitud = v.findViewById(R.id.valorLatitudInfo);
        longitud = v.findViewById(R.id.valorLongitudInfo);
        updateInfo(m);
        return v;
    }

    private void updateInfo(Marker m) {
        Geocoder geocoder = new Geocoder(inflater.getContext(), Locale.getDefault());

        String ciudad = null;
        try {
            ciudad = geocoder.getFromLocation(m.getPosition().latitude, m.getPosition().longitude, 1).get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiManager.getWeatherForMapInfo(condicion, estado, temperatura, ciudad);

        lugar.setText(ciudad);
        latitud.setText(String.valueOf(m.getPosition().latitude));
        longitud.setText(String.valueOf(m.getPosition().longitude));
    }
}