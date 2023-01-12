package com.example.betterweather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.betterweather.databinding.ActivityMapsBinding;
import com.example.betterweather.util.api.ApiManager;
import com.example.betterweather.util.handler.weather.MapWeatherHandler;
import com.example.betterweather.modelo.info.weather.WeatherCallInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ApiManager apiManager;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker currentMarker;
    private TileOverlay tileOver;

    private Spinner spinnerTipos;
    private HorizontalScrollView panelInfo;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiManager = new ApiManager();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerTipos = (Spinner) findViewById(R.id.spinnerTipos);
        panelInfo = (HorizontalScrollView) findViewById(R.id.info);

        loadSpinner();

        loadMenu();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Metodo que carga el spinner de capas
     */
    private void loadSpinner() {
        ArrayAdapter<CharSequence> adpt = ArrayAdapter.createFromResource(this,
                R.array.layers, android.R.layout.simple_spinner_item);

        spinnerTipos.setAdapter(adpt);
        spinnerTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiManager.checkTileType(position);
                if (mMap != null) {
                    if (tileOver != null) {
                        tileOver.remove();
                    }
                    setUpMap();
                }
            }
        });
    }

    /**
     * Metodo que carga el menu de navegacion
     */
    private void loadMenu() {
        bottomNav = findViewById(R.id.navigation_menu);
        bottomNav.setSelectedItemId(R.id.map);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourites:
                        Intent intentFavourites = new Intent(getApplicationContext(), MainRecycler.class);
                        intentFavourites.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intentFavourites);
                        return true;
                    case R.id.home:
                        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                        intentHome.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intentHome);
                        return true;
                    case R.id.map:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.map);
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

        LatLng eii = new LatLng(43.3548057, -5.8534646);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eii));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (currentMarker != null){
                    currentMarker.remove();
                }
                panelInfo.setVisibility(View.INVISIBLE);
                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.marcadorPersonalizado)));
                addMarker(currentMarker);
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (currentMarker != null){
                    currentMarker.remove();
                }
                panelInfo.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Metodo que aniade marcador y obtiene el tiempo para esas coordenadas
     * @param m
     */
    private void addMarker(Marker m) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String ciudad = null;
        try {
            if (!geocoder.getFromLocation(m.getPosition().latitude, m.getPosition().longitude, 1).isEmpty()) {
                ciudad = geocoder.getFromLocation(m.getPosition().latitude, m.getPosition().longitude, 1).get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ciudad != null) {
            //apiManager.getWeatherForMapInfo(lugar, condicion, estado, temperatura, latitud, longitud, ciudad);
            apiManager.getWeather(new WeatherCallInfo(ciudad), new MapWeatherHandler(getActivity()));
            panelInfo.setVisibility(View.VISIBLE);
        }
    }

    private void setUpMap() {
        tileOver = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(apiManager.createTileProvider()));
    }

    private MapsActivity getActivity() {
        return this;
    }
}