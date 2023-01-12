package com.example.betterweather.handler.location;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.EditText;

import com.example.betterweather.MainActivity;
import com.example.betterweather.R;
import com.example.betterweather.location.LocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHandlerAndSetInitialText implements LocationHandler {

    private MainActivity activity;

    private EditText placeSearch;
    private FusedLocationProviderClient fusedLocationClient;

    public LocationHandlerAndSetInitialText(MainActivity activity, EditText placeSearch, FusedLocationProviderClient fusedLocationClient){
        this.activity=activity;
        this.placeSearch=placeSearch;
        this.fusedLocationClient=fusedLocationClient;
    }

    @Override
    public void handleSuccess(Location location) {
        Geocoder geoCoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
        List<Address> dir;
        try {
            dir = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            placeSearch.setText("Madrid");
            return;
        }
        placeSearch.setText(dir.get(0).getLocality());
    }

    @Override
    public void handleFailure() {
        placeSearch.setText("Madrid");
    }

    @Override
    public void handleNoPermission() {
        Snackbar.make(activity.findViewById(R.id.editTextPlaceSearch), "La aplicación no tiene los permisos necesarios (Ubicación)", Snackbar.LENGTH_SHORT).show();
        placeSearch.setText("Madrid");
    }
}
