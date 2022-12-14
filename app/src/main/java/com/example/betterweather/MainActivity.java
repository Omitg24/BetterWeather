package com.example.betterweather;

import static com.example.betterweather.MainRecycler.LUGAR_SELECCIONADO;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.betterweather.util.api.ApiManager;
import com.example.betterweather.util.db.LugaresDataSource;
import com.example.betterweather.util.handler.location.LocationHandlerAndSetInitialText;
import com.example.betterweather.util.handler.weather.MainWeatherHandler;
import com.example.betterweather.modelo.info.weather.WeatherCallInfo;
import com.example.betterweather.modelo.weatherpojos.Lugar;
import com.example.betterweather.util.notification.AlarmReceiver;
import com.example.betterweather.util.weather.WeatherUtil;
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

    private Button botonCamara;

    private static WebView web;

    private static TextView titulo;
    private static TextView ciudad;
    private static TextView region;
    private static TextView pais;
    private static TextView continente;

    private BottomNavigationView bottomNav;

    private AlertDialog dialog;

    private Timer timer = new Timer();

    private LugaresDataSource lds;

    public static final int SOLICITUD_TIEMPO = 0;

    public static final int SOLICITUD_VOZ = 1;

    private String[] PERMISSIONS;

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
        botonCamara = (Button) findViewById(R.id.botonCamara);

        loadMenu();

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        textViewSystemTime.setText(sdf.format(currentTime));

        lds = new LugaresDataSource(getApplicationContext());

        loadSpinner();

        placeSearch.addTextChangedListener(getListenerBusqueda());

        if (lds.findPlace(new Lugar(placeSearch.getText().toString()))) {
            favButton.setBackgroundResource(R.drawable.ic_favorite_24);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourite();
            }
        });

        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO};
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }else{
            findLocationAndSetText();
        }
        loadListenButton();

        createWebcamDialog();
    }

    /**
     * Metodo que comprueba si se tienen los permisos de localizacion
     */
    private boolean hasPermissions(Context context, String... PERMISSIONS) {
        if (context != null && PERMISSIONS != null) {
            for (String p : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metodo que inicializa el boton de escuchar
     */
    private void loadListenButton() {
        ImageButton buttonListen = (ImageButton) findViewById(R.id.botonEscuchar);
        buttonListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGooglePlayServicesAvailable(getActivity())) {
                    escuchar();
                } else {
                    Snackbar.make(findViewById(R.id.editTextPlaceSearch),
                            getString(R.string.permisoGooglePlayNoDisponible),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Metodo que carga el menu de navegacion
     */
    private void loadMenu() {
        bottomNav = findViewById(R.id.navigation_menu);

        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourites:
                        Intent intentFavourites = new Intent(getApplicationContext(), MainRecycler.class);
                        intentFavourites.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityForResult(intentFavourites, SOLICITUD_TIEMPO);
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
    }

    /**
     * Metodo que carga el spinner de unidades
     */
    private void loadSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiManager.getWeather(new WeatherCallInfo(placeSearch.getText().toString(), WeatherUtil.getUnit(parent.getItemAtPosition(position).toString())), new MainWeatherHandler(getActivity()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOLICITUD_TIEMPO) {
            if (resultCode == RESULT_OK) {
                Lugar lugar = (Lugar) data.getParcelableExtra(LUGAR_SELECCIONADO);
                if (data.getExtras() == null || lugar == null) {
                    findLocationAndSetText();
                } else {
                    placeSearch.setText((lugar).getIdentificadorLugar());
                }
            }
        } else if (requestCode == SOLICITUD_VOZ) {
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

        if (intent.getExtras() != null) {
            Lugar lugar = (Lugar) intent.getParcelableExtra(LUGAR_SELECCIONADO);
            if (lugar != null) {
                placeSearch.setText((lugar).getIdentificadorLugar());
                updateWeather();
            }
        }

        Lugar lugar = new Lugar(placeSearch.getText().toString());
        if (lds.findPlace(lugar)) {
            favButton.setBackgroundResource(R.drawable.ic_favorite_24);
        } else {
            favButton.setBackgroundResource(R.drawable.ic_favorite_border_24);
        }

        bottomNav.setSelectedItemId(R.id.home);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permisoUbicacionAceptado), Toast.LENGTH_SHORT).show();

                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String ciudad = null;
                    if (location != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();


                        try {
                            ciudad = geocoder.getFromLocation(latitude, longitude, 1).get(0).getLocality();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.noUbicacion), Toast.LENGTH_SHORT).show();
                        ciudad = "Madrid";
                    }
                    placeSearch.setText(ciudad);
                    apiManager.getWeather(new WeatherCallInfo(placeSearch.getText().toString(), WeatherUtil.getUnit(spinnerUnits.getSelectedItem().toString())), new MainWeatherHandler(getActivity()));
                }
            } else {
                Toast.makeText(this, getString(R.string.permisoUbicacionRechazado), Toast.LENGTH_SHORT).show();
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.permisoGrabacionAceptado), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.permisoGrabacionRechazado), Toast.LENGTH_SHORT).show();
            }
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

    public static void procesaCambio(Lugar lugar) {
        if(lugar !=null) {
            placeSearch.setText((lugar).getIdentificadorLugar());
        }
    }

    /**
     * Metodo que crea el canal de notificaciones para la notificacion diaria del tiempo actual
     */
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

    /**
     * Metodo que establece una alarma diaria para las 12:00
     */
    public void establecerAlarma() {
        createNotificationChannel();
        Calendar calendar = Calendar.getInstance();
        //Indicando la hora a la que saltara la alarma
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(this, AlarmReceiver.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Si ya hay una alarma establecida
        if (pendingIntent != null && am != null) {
            am.cancel(pendingIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            //Inexacto ya que asi no consume tanta bateria al dispositivo
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent );
        }
    }

    /**
     * Metodo que aniade a favoritos(bbdd) el lugar seleccionado
     */
    private void addFavourite(){
        Lugar lugar = new Lugar(placeSearch.getText().toString());
        if(lds.findPlace(lugar)){
            lds.open();
            lds.removePlace(lugar);
            lds.close();
            favButton.setBackgroundResource(R.drawable.ic_favorite_border_24);
            Toast.makeText(this, getString(R.string.borradoFavoritos), Toast.LENGTH_SHORT).show();
        } else {
            if(apiManager.getExists()){
                lds.open();
                lds.createPlace(lugar);
                lds.close();
                favButton.setBackgroundResource(R.drawable.ic_favorite_24);
                Toast.makeText(this, getString(R.string.anadidoFavoritos), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.noPodidoFavoritos), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findLocationAndSetText() {
        apiManager.findLocation(this, fusedLocationClient, new LocationHandlerAndSetInitialText(getActivity(),placeSearch));
    }

    private void updateWeather() {
        apiManager.getWeather(new WeatherCallInfo(placeSearch.getText().toString().trim(),WeatherUtil.getUnit(spinnerUnits.getSelectedItem().toString())),new MainWeatherHandler(getActivity()));
    }

    public static Spinner getSpinnerUnits() {
        return spinnerUnits;
    }

    private MainActivity getActivity() {
        return this;
    }

    /**
     * Metodo que devuelve el listener de la busqueda que sirve para consultar los datos una vez
     * pasados un tiempo sin escribir
     * @return
     */
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

    /**
     * Metodo que abre el microfono y escucha lo introducido por voz
     */
    private void escuchar() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    getString(R.string.microfonoBuscar));
            startActivityForResult(intent, 1);
        }else{
            Snackbar.make(findViewById(R.id.editTextPlaceSearch), getString(R.string.noPermisosAceptados),
                    Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * Metodo que crea el dialogo con la informacion de la webcam
     */
    public void createWebcamDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View popupCameraView = getLayoutInflater().inflate(R.layout.popup_camera, null);
        web = popupCameraView.findViewById(R.id.webView);
        titulo = popupCameraView.findViewById(R.id.titulo);
        ciudad = popupCameraView.findViewById(R.id.ciudad);
        region = popupCameraView.findViewById(R.id.region);
        pais = popupCameraView.findViewById(R.id.pais);
        continente = popupCameraView.findViewById(R.id.continente);
        Button botonCerrar = popupCameraView.findViewById(R.id.botonCerrar);

        web.getSettings().setJavaScriptEnabled(true);
        dialogBuilder.setView(popupCameraView);
        dialog = dialogBuilder.create();

        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Metodo que actualiza los datos de la webcam
     * @param url
     * @param title
     * @param city
     * @param reg
     * @param country
     * @param continent
     */
    public static void updateDatos(String url, String title, String city, String reg, String country, String continent) {
        web.loadUrl(url);
        titulo.setText(title);
        ciudad.setText(city);
        region.setText(reg);
        pais.setText(country);
        continente.setText(continent);
    }
}