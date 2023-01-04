package com.example.betterweather;

import static com.example.betterweather.MainActivity.SOLICITUD_TIEMPO;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.betterweather.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ApiManager apiManager;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiManager = new ApiManager();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNav = findViewById(R.id.navigation_menu);
        bottomNav.setSelectedItemId(R.id.map);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourites:
                        Intent intentFavourites = new Intent(getApplicationContext(), MainRecycler.class);
                        startActivityForResult(intentFavourites, SOLICITUD_TIEMPO);
                        return true;
                    case R.id.home:
                        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentHome);
                        return true;
                    case R.id.map:
                        return true;
                }
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(43.3548057, -5.8534646);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marcador en la Escuela de Ingeniería Informática"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                showInfo(latLng);
            }
        });
    }

    private void showInfo(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double longitude = latLng.longitude;
        double latitude = latLng.latitude;

        String ciudad = null;
        try {
            ciudad = geocoder.getFromLocation(latitude, longitude, 1).get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        apiManager.getWeatherForMapInfo(this, ciudad);
    }
}