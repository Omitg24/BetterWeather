package com.example.betterweather;

import static com.example.betterweather.MainActivity.SOLICITUD_TIEMPO;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.betterweather.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.MalformedURLException;
import java.net.URL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String tileType = "clouds";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker currentMarker;
    private TileOverlay tileOver;

    private Spinner spinnerTipos;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerTipos = (Spinner) findViewById(R.id.spinnerTipos);

        ArrayAdapter<CharSequence> adpt = ArrayAdapter.createFromResource(this,
                R.array.layers, android.R.layout.simple_spinner_item);

        spinnerTipos.setAdapter(adpt);
        spinnerTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check click
                switch (position) {
                    case 0:
                        tileType = "clouds_new";
                        break;
                    case 1:
                        tileType = "temp_new";
                        break;
                    case 2:
                        tileType = "precipitation_new";
                        break;
                    case 3:
                        tileType = "wind_new";
                        break;
                    case 4:
                        tileType = "pressure_new";
                        break;
                }
                if (mMap != null) {
                    if (tileOver != null) {
                        tileOver.remove();
                    }
                    setUpMap();
                }
            }
        });

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

        WeatherInfoWindowAdapter infoWindow = new WeatherInfoWindowAdapter(getLayoutInflater());

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
                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador personalizado"));
                infoWindow.getInfoWindow(currentMarker).invalidate();
                if (currentMarker != null){
                    currentMarker.showInfoWindow();
                }
            }
        });
        mMap.setInfoWindowAdapter(infoWindow);
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (currentMarker != null){
                    currentMarker.remove();
                }
            }
        });
    }


    private void setUpMap() {
        tileOver = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(createTileProvider()));
    }

    private TileProvider createTileProvider() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String fUrl = "https://tile.openweathermap.org/map/" + (tileType == null ? "clouds_new" : tileType) + "/"
                        + zoom + "/" + x + "/" + y + ".png?appid=43659c6dc1582c2ec51e7a6ce96d6c7d";
                Log.i("Result", fUrl);
                URL url = null;
                try {
                    url = new URL(fUrl);
                }
                catch(MalformedURLException mfe) {
                    mfe.printStackTrace();
                }
                return url;
            }
        };
        return tileProvider;
    }
}