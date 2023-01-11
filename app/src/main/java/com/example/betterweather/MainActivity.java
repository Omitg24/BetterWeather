package com.example.betterweather;

import static com.example.betterweather.MainRecycler.LUGAR_SELECCIONADO;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.betterweather.db.LugaresDataSource;
import com.example.betterweather.handler.locationHandler.LocationHandlerAndSetInitialText;
import com.example.betterweather.handler.weatherHandler.MainWeatherHandler;
import com.example.betterweather.modelo.Lugar;
import com.example.betterweather.notification.AlarmReceiver;
import com.example.betterweather.weather.WeatherCallInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final long DELAY = 2000;

    private ApiManager apiManager;

    private FusedLocationProviderClient fusedLocationClient;

    private static EditText placeSearch;

    private TextView textViewSystemTime;

    private static Spinner spinnerUnits;

    private ImageButton favButton;

    private BottomNavigationView bottomNav;

    private Timer timer = new Timer();

    private LugaresDataSource lds;

    public static final int SOLICITUD_TIEMPO = 0;

    public static final int SOLICITUD_VOZ = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        establecerAlarma();
        setContentView(R.layout.activity_main);

        apiManager = new ApiManager();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        placeSearch = (EditText) findViewById(R.id.editTextPlaceSearch);
        textViewSystemTime = (TextView) findViewById(R.id.textViewFechaSistema);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);
        favButton = (ImageButton) findViewById(R.id.addFav);
        bottomNav = findViewById(R.id.navigation_menu);
        bottomNav.setSelectedItemId(R.id.home);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourites:
                        Intent intentFavourites = new Intent(getApplicationContext(), MainRecycler.class);
                        intentFavourites.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityForResult(intentFavourites,SOLICITUD_TIEMPO);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.map:
                        Intent intentMap = new Intent(getApplicationContext(), MapsActivity.class);
                        intentMap.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intentMap);
                        return true;
                }
                return false;
            }
        });

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        textViewSystemTime.setText(sdf.format(currentTime));

        Intent intent = getIntent();

        lds = new LugaresDataSource(getApplicationContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiManager.getWeather(new WeatherCallInfo(placeSearch.getText().toString(),getUnit(parent.getItemAtPosition(position).toString())), new MainWeatherHandler(getActivity()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        findLocationAndSetText();

        placeSearch.addTextChangedListener(getListenerBusqueda());

        if(lds.findPlace(new Lugar(placeSearch.getText().toString()))){
            favButton.setBackgroundResource(R.drawable.ic_favorite_24);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourite();
            }
        });

        ImageButton buttonListen = (ImageButton) findViewById(R.id.bescuchar);
        buttonListen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isGooglePlayServicesAvailable(getActivity())){
                    escuchar();
                }else {
                    Snackbar.make(findViewById(R.id.editTextPlaceSearch),
                            "No se puede acceder a los servicios de Google Play",
                            Snackbar.LENGTH_SHORT).show();
                }
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

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},1);

        }
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
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
        }else if(requestCode == SOLICITUD_VOZ) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                placeSearch.setText(matches.get(0).toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();

        if(intent.getExtras()!=null){
            Lugar lugar = (Lugar) intent.getParcelableExtra(LUGAR_SELECCIONADO);
            if(lugar !=null) {
                placeSearch.setText((lugar).getIdentificadorLugar());
                updateWeather();
            }
        }

        bottomNav.setSelectedItemId(R.id.home);
    }

    public static void procesaCambio(Lugar lugar) {
        if(lugar !=null) {
            placeSearch.setText((lugar).getIdentificadorLugar());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.tituloNotificationChannel1);
            String description = getString(R.string.descripcionNotificationChannel1);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("M_CH_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void establecerAlarma() {
        createNotificationChannel();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (pendingIntent != null && am != null) {
            am.cancel(pendingIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            //am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR, pendingIntent );
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent );
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
                        String ciudad = null;
                        if(location!=null){
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();


                            try {
                                ciudad = geocoder.getFromLocation(latitude, longitude, 1).get(0).getLocality();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(this, "No hemos podido obtener una ubicación", Toast.LENGTH_SHORT).show();
                            ciudad = "Madrid";
                        }
                        placeSearch.setText(ciudad);
                        apiManager.getWeather(new WeatherCallInfo(placeSearch.getText().toString(),getUnit(spinnerUnits.getSelectedItem().toString())),new MainWeatherHandler(getActivity()));
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
            favButton.setBackgroundResource(R.drawable.ic_favorite_border_24);
            Snackbar.make(findViewById(R.id.editTextPlaceSearch), "Se ha borrado de favoritos",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            if(apiManager.getExists()){
                lds.open();
                lds.createPlace(lugar);
                lds.close();
                favButton.setBackgroundResource(R.drawable.ic_favorite_24);
                Snackbar.make(findViewById(R.id.editTextPlaceSearch), "Se ha añadido a favoritos",
                        Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.editTextPlaceSearch), "No se ha podido añadir a favoritos ya que el lugar no existe",
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void findLocationAndSetText() {
        apiManager.findLocation(this, fusedLocationClient, new LocationHandlerAndSetInitialText(getActivity(),placeSearch,fusedLocationClient));
    }

    private void updateWeather() {
        apiManager.getWeather(new WeatherCallInfo(placeSearch.getText().toString().trim(),getUnit(spinnerUnits.getSelectedItem().toString())),new MainWeatherHandler(getActivity()));
    }

    public static String getUnit(String unit) {
        if (unit.equalsIgnoreCase("celsius")) {
            return "metric";
        } else if (unit.equalsIgnoreCase("fahrenheit")) {
            return "imperial";
        }
        return "standard";
    }

    public static String getUnitLetter(String unit) {
        if (unit.equalsIgnoreCase("celsius") || unit.equalsIgnoreCase("metric")) {
            return "ºC";
        } else if (unit.equalsIgnoreCase("fahrenheit") || unit.equalsIgnoreCase("imperial")) {
            return "ºF";
        }
        return "ºK";
    }

    public static Spinner getSpinnerUnits() {
        return spinnerUnits;
    }

    private MainActivity getActivity() {
        return this;
    }

    private TextWatcher getListenerBusqueda() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
                    favButton.setBackgroundResource(R.drawable.ic_favorite_24);
                } else {
                    favButton.setBackgroundResource(R.drawable.ic_favorite_border_24);
                }
            }
        };
    }

    private void escuchar() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "¿Qué lugar está buscando?");
        startActivityForResult(intent, 1);
    }
}