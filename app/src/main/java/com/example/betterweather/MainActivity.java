package com.example.betterweather;

import static com.example.betterweather.MainRecycler.LUGAR_SELECCIONADO;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betterweather.db.LugaresDataSource;
import com.example.betterweather.modelo.Lugar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ApiManager apiManager;

    private TextView textViewSystemTime;

    private ImageButton imageButtonVistaMapa;

    private EditText placeSearch;

    private Spinner spinnerUnits;

    private FusedLocationProviderClient fusedLocationClient;

    private Timer timer = new Timer();

    private final long DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        apiManager = new ApiManager();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        placeSearch = (EditText) findViewById(R.id.editTextPlaceSearch);
        textViewSystemTime = (TextView) findViewById(R.id.textViewFechaSistema);
        imageButtonVistaMapa = (ImageButton) findViewById(R.id.imageButtonVistaMapa);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        textViewSystemTime.setText(sdf.format(currentTime));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);
        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                apiManager.getWeather(getActivity(), placeSearch.getText().toString(), getUnit(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Lugar lugar = (Lugar) intent.getParcelableExtra(LUGAR_SELECCIONADO);
        if(intent.getExtras()==null || lugar ==null){
            findLocationAndSetText();
        }else{
            placeSearch.setText((lugar).getIdentificadorLugar());
        }

        placeSearch.addTextChangedListener(getListenerBusqueda());


        findViewById(R.id.imageButtonVistaMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uriUrl = Uri.parse("https://openweathermap.org/weathermap?basemap=map&cities=false&layer=precipitation&lat="+ apiManager.getLat() +"&lon="+apiManager.getLon() +"&zoom=5");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        findViewById(R.id.botonFav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        findViewById(R.id.addFav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void add(){
        LugaresDataSource l = new LugaresDataSource(getApplicationContext());
        l.open();
        l.createLugar(new Lugar(placeSearch.getText().toString()));
        l.close();
    }

    private void click(){
        Intent intentSettingsActivity=new Intent(this, MainRecycler.class);
        startActivity(intentSettingsActivity);
    }


    private void findLocationAndSetText() {
       apiManager.findLocationAndSetText(this,placeSearch,fusedLocationClient);
    }

    private void updateWeather() {
        apiManager.getWeather(this, placeSearch.getText().toString(), getUnit(spinnerUnits.getSelectedItem().toString()));
    }

    public String getUnit(String unit) {
        if (unit.equalsIgnoreCase("celsius")) {
            return "metric";
        } else if (unit.equalsIgnoreCase("Fahrenheit")) {
            return "imperial";
        }
        return "standard";
    }

    public String getUnitLetter(String unit) {
        if (unit.equalsIgnoreCase("celsius")) {
            return "ºC";
        } else if (unit.equalsIgnoreCase("Fahrenheit")) {
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

    private TextWatcher getListenerBusqueda(){
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
                            }
                        },DELAY);
            }
        };
    }

    /*
    private void obtainLocationAndRefreshSearch(String ciudad) {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else {
            location = new Location("");
            location.setAltitude(51.509865);
            location.setLongitude(-0.118092);
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        apiManager.getWeather(this,ciudad,"metric");
    }
    */
}