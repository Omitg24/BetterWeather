package com.example.betterweather;

import static com.example.betterweather.MainRecycler.LUGAR_SELECCIONADO;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.betterweather.db.LugaresDataSource;
import com.example.betterweather.modelo.Lugar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final long DELAY = 2000;

    private ApiManager apiManager;

    private TextView textViewSystemTime;

    private ImageButton imageButtonVistaMapa;

    private EditText placeSearch;

    private Spinner spinnerUnits;

    private ImageButton favButton;

    private FusedLocationProviderClient fusedLocationClient;

    private Timer timer = new Timer();

    private LugaresDataSource lds;

    public static final int SOLICITUD_TIEMPO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        apiManager = new ApiManager();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        placeSearch = (EditText) findViewById(R.id.editTextPlaceSearch);
        textViewSystemTime = (TextView) findViewById(R.id.textViewFechaSistema);
        imageButtonVistaMapa = (ImageButton) findViewById(R.id.botonMapa);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);
        favButton = (ImageButton) findViewById(R.id.addFav);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        textViewSystemTime.setText(sdf.format(currentTime));

        lds = new LugaresDataSource(getApplicationContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiManager.getWeather(getActivity(), placeSearch.getText().toString(),
                        getUnit(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findLocationAndSetText();

        placeSearch.addTextChangedListener(getListenerBusqueda());

        if(lds.findPlace(new Lugar(placeSearch.getText().toString()))){
            favButton.setBackgroundResource(R.drawable.favourite);
        }

        findViewById(R.id.botonMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {showMap();}
        });

        findViewById(R.id.botonFav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFavourites();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourite();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOLICITUD_TIEMPO) {
            if (resultCode == RESULT_OK) {
                Lugar lugar = (Lugar) data.getParcelableExtra(LUGAR_SELECCIONADO);
                if(data.getExtras()==null || lugar ==null){
                    findLocationAndSetText();
                }else{
                    placeSearch.setText((lugar).getIdentificadorLugar());
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permiso de ubicación aceptado", Toast.LENGTH_SHORT).show();

                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();

                        String ciudad = null;
                        try {
                            ciudad = geocoder.getFromLocation(latitude, longitude, 1).get(0).getLocality();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        placeSearch.setText(ciudad);
                        apiManager.getWeather(this, ciudad, getUnit(spinnerUnits.getSelectedItem().toString()));
                    }
                } else {
                    Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void addFavourite(){
        Lugar lugar = new Lugar(placeSearch.getText().toString());
        if(lds.findPlace(lugar)){
            lds.open();
            lds.removePlace(lugar);
            lds.close();
            favButton.setBackgroundResource(R.drawable.add_favourite);
            Snackbar.make(findViewById(R.id.editTextPlaceSearch), "Se ha borrado de favoritos",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            if(apiManager.getExists()){
                lds.open();
                lds.createPlace(lugar);
                lds.close();
                favButton.setBackgroundResource(R.drawable.favourite);
                Snackbar.make(findViewById(R.id.editTextPlaceSearch), "Se ha añadido a favoritos",
                        Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.editTextPlaceSearch), "No se ha podido añadir a favoritos ya que el lugar no existe",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void showMap() {
        Intent intentSettingsActivity=new Intent(this, MapsActivity.class);
        startActivity(intentSettingsActivity);
    }

    private void showFavourites(){
        Intent intentSettingsActivity=new Intent(this, MainRecycler.class);
        startActivityForResult(intentSettingsActivity,SOLICITUD_TIEMPO);
    }


    private void findLocationAndSetText() {
        apiManager.findLocationAndSetText(this, placeSearch, fusedLocationClient);
    }

    private void updateWeather() {
        apiManager.getWeather(this, placeSearch.getText().toString(), getUnit(spinnerUnits.getSelectedItem().toString()));
    }

    public String getUnit(String unit) {
        if (unit.equalsIgnoreCase("celsius")) {
            return "metric";
        } else if (unit.equalsIgnoreCase("fahrenheit")) {
            return "imperial";
        }
        return "standard";
    }

    public String getUnitLetter(String unit) {
        if (unit.equalsIgnoreCase("celsius")) {
            return "ºC";
        } else if (unit.equalsIgnoreCase("fahrenheit")) {
            return "ºF";
        }
        return "ºK";
    }

    public Spinner getSpinnerUnits() {
        return spinnerUnits;
    }

    private MainActivity getActivity() {
        return this;
    }

    private TextWatcher getListenerBusqueda() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                //Anadiendo delay para que no saturar la api
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                updateWeather();
                                View view = getCurrentFocus();
                                if(view!=null){
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                        }, DELAY);
                if(lds.findPlace(new Lugar(placeSearch.getText().toString()))){
                    favButton.setBackgroundResource(R.drawable.favourite);
                } else {
                    favButton.setBackgroundResource(R.drawable.add_favourite);
                }
            }
        };
    }
}