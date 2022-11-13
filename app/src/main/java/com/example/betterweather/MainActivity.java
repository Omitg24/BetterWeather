package com.example.betterweather;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.betterweather.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ApiManager apiManager;

    private TextView textViewSystemTime;

    private EditText placeSearch;

    private ImageButton btnSearch;

    private Spinner spinnerUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiManager = new ApiManager();
        placeSearch = (EditText) findViewById(R.id.editTextPlaceSearch);
        textViewSystemTime = (TextView) findViewById(R.id.textViewFechaSistema);
        btnSearch = (ImageButton) findViewById(R.id.imageButtonSearch);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        textViewSystemTime.setText(sdf.format(currentTime));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeather();
            }
        });

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
        updateWeather();
    }

    private void updateWeather() {
        if (placeSearch.getText().length() < 2) {
            Toast.makeText(this, "Debes ser mas especifico con el lugar :(", Toast.LENGTH_SHORT).show();
        } else {
            apiManager.getWeather(this, placeSearch.getText().toString(), getUnit(spinnerUnits.getSelectedItem().toString()));
        }
    }

    private String getUnit(String unit) {
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