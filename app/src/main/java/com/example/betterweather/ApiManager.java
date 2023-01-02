package com.example.betterweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.gridlayout.widget.GridLayout;

import com.example.betterweather.apiUtils.WeatherAPI;
import com.example.betterweather.modelo.TemperaturaData;
import com.example.betterweather.modelo.ui.LineaReciclerFav;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {

    private String paramsUrl;

    private TemperaturaData result;

    private String urlBase = "https://api.openweathermap.org/data/2.5/";

    private String urlMapa = "https://tile.openweathermap.org/map/";

    public final static String URL = "http://openweathermap.org/img/wn/";

    private double lat;
    private double lon;

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(urlBase).addConverterFactory(GsonConverterFactory.create()).build();

    public void getWeather(MainActivity activity, String city, String units) {
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<TemperaturaData> call = weatherAPI.getCourse(city, units);
        call.enqueue(new Callback<TemperaturaData>() {
            @Override
            public void onResponse(Call<TemperaturaData> call, Response<TemperaturaData> response) {
                if (response.isSuccessful()) {
                    result = response.body();
                    if (result.getList().size() != 0) {
                        updateInfoViewDays(activity);
                        TextView textViewTemperaturaMain = activity.findViewById(R.id.textViewTemperaturaMain);
                        String unit = activity.getUnitLetter(activity.getSpinnerUnits().getSelectedItem().toString());
                        textViewTemperaturaMain.setText(result.getList().get(0).getMain().getTemp() + " " + unit);

                        View background = activity.findViewById(R.id.main_layout);
                        ImageView imageViewPrincipal = activity.findViewById(R.id.imageViewPrincipal);
                        TextView textViewPrincipal = activity.findViewById(R.id.textViewDescripcion);
                        updateWeather(background, imageViewPrincipal, textViewPrincipal, result.getList().get(0).getWeather().get(0).getDescription());
                        //imageViewPrincipal.setImageResource(getIdOfImageView(result.getList().get(0).getWeather().get(0).getDescription()));

                        lon = result.getList().get(0).getCoord().getLon();
                        lat = result.getList().get(0).getCoord().getLat();
                    } else {
                        Snackbar.make(activity.findViewById(R.id.editTextPlaceSearch), "No se ha podido encontrar el lugar, intenta ser más especifico", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TemperaturaData> call, Throwable t) {
                Snackbar.make(activity.findViewById(R.id.editTextPlaceSearch), "No se ha podido realizar la solicitud :(", Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    public void findLocationAndSetText(MainActivity activity,EditText placeSearch,FusedLocationProviderClient fusedLocationClient){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(activity.findViewById(R.id.editTextPlaceSearch), "La aplicación no tiene permisos para acceder a tu ubicación", Snackbar.LENGTH_SHORT).show();
            placeSearch.setText("Londres");
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Geocoder geoCoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
                            List<Address> dir;
                            try {
                                dir = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                lat = dir.get(0).getLatitude();
                                lon = dir.get(0).getLongitude();
                            } catch (IOException e) {
                                placeSearch.setText("Londres");
                                return;
                            }
                            placeSearch.setText(dir.get(0).getPostalCode());
                        } else {
                            placeSearch.setText("Londres");
                            return;
                        }
                    }
                });

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public TemperaturaData callApi(String city,String units){
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<TemperaturaData> call = weatherAPI.getCourse(city, units);
        call.enqueue(new Callback<TemperaturaData>() {
            @Override
            public void onResponse(Call<TemperaturaData> call, Response<TemperaturaData> response) {
                if (response.isSuccessful()) {
                    result = response.body();
                    Log.i("aasd","aasdasdasdasdsadsa");
                }
                Log.i("aasd","erigweruywetryuetgruywer");
            }
            @Override
            public void onFailure(Call<TemperaturaData> call, Throwable t) {
                Log.i("aasd","ooeriuweoriuweori");
            }
        });
        return result;
    }

    private void updateInfoViewDays(MainActivity activity) {
        GridLayout grid = (GridLayout) activity.findViewById(R.id.gridLayoutDays);
        grid.removeAllViews();
        for (int i = 0; i < result.getList().size(); i++) {
            LinearLayout auxLayout = new LinearLayout(grid.getContext());

            TextView textoDia = new TextView(grid.getContext());
            textoDia.setText(getFechaString(i));
            textoDia.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView textoMinima = new TextView(grid.getContext());
            textoMinima.setText("Min: " + result.getList().get(i).getMain().getTemp_min());
            textoMinima.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            TextView textoMaxima = new TextView(grid.getContext());
            textoMaxima.setText("Max: " + result.getList().get(i).getMain().getTemp_max());
            textoMaxima.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            ImageView imagenTiempo = new ImageView(grid.getContext());
            imagenTiempo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            updateImage(imagenTiempo, result.getList().get(i).getWeather().get(0).getDescription());

            auxLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            auxLayout.setOrientation(LinearLayout.VERTICAL);
            auxLayout.addView(textoDia);
            auxLayout.addView(imagenTiempo);
            imagenTiempo.getLayoutParams().width = 250;
            imagenTiempo.requestLayout();
            textoDia.setTextColor(Color.BLACK);
            textoMinima.setTextColor(Color.BLACK);
            textoMaxima.setTextColor(Color.BLACK);
            auxLayout.addView(textoMaxima);
            auxLayout.addView(textoMinima);
            grid.addView(auxLayout);
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    private void updateWeather(View background, ImageView imageViewPrincipal, TextView textViewPrincipal, String weather) {
        updateBackground(background, weather);
        updateImage(imageViewPrincipal, weather);
        updateText(textViewPrincipal, weather);
    }

    private void updateBackground(View background, String weather) {
        if (weather.equals("clear sky")) {
            background.setBackgroundResource(R.drawable.clear_sky);
        } else if (weather.equals("few clouds")) {
            background.setBackgroundResource(R.drawable.few_clouds);
        } else if (weather.equals("broken clouds")) {
            background.setBackgroundResource(R.drawable.broken_clouds);
        } else if (weather.contains("cloud")) {
            background.setBackgroundResource(R.drawable.scattered_clouds);
        } else if (weather.equals("shower rain")) {
            background.setBackgroundResource(R.drawable.shower_rain);
        } else if (weather.contains("rain")) {
            background.setBackgroundResource(R.drawable.rain);
        } else if (weather.equals("thunderstorm")) {
            background.setBackgroundResource(R.drawable.thunderstorm);
        } else if (weather.equals("snow")) {
            background.setBackgroundResource(R.drawable.snow);
        } else if (weather.equals("mist")) {
            background.setBackgroundResource(R.drawable.mist);
        } else {
            background.setBackgroundResource(R.drawable.few_clouds);
        }
    }

    private void updateImage(ImageView imageView, String weather) {
        if (weather.equals("clear sky")) {
            imageView.setImageResource(R.mipmap.ic_clear_sky_foreground);
        } else if (weather.equals("few clouds")) {
            imageView.setImageResource(R.mipmap.ic_few_clouds_foreground);
        } else if (weather.equals("broken clouds")) {
            imageView.setImageResource(R.mipmap.ic_broken_clouds_foreground);
        } else if (weather.contains("cloud")) {
            imageView.setImageResource(R.mipmap.ic_scattered_clouds_foreground);
        } else if (weather.equals("shower rain")) {
            imageView.setImageResource(R.mipmap.ic_shower_rain_foreground);
        } else if (weather.contains("rain")) {
            imageView.setImageResource(R.mipmap.ic_rain_foreground);
        } else if (weather.equals("thunderstorm")) {
            imageView.setImageResource(R.mipmap.ic_thunderstorm_foreground);
        } else if (weather.equals("snow")) {
            imageView.setImageResource(R.mipmap.ic_snow_foreground);
        } else if (weather.equals("mist")) {
            imageView.setImageResource(R.mipmap.ic_mist_foreground);
        } else {
            imageView.setImageResource(R.mipmap.ic_few_clouds_foreground);
        }
    }

    private void updateText(TextView textView, String weather) {
        if (weather.equals("clear sky")) {
            textView.setText("Soleado");
        } else if (weather.equals("few clouds")) {
            textView.setText("Sol y nubes");
        } else if (weather.equals("broken clouds")) {
            textView.setText("Nuboso");
        } else if (weather.contains("cloud")) {
            textView.setText("Nubes leves");
        } else if (weather.equals("shower rain")) {
            textView.setText("Lluvia leve");
        } else if (weather.contains("rain")) {
            textView.setText("Lluvioso");
        } else if (weather.equals("thunderstorm")) {
            textView.setText("Tormenta");
        } else if (weather.equals("snow")) {
            textView.setText("Nieve");
        } else if (weather.equals("mist")) {
            textView.setText("Nublado");
        } else {
            textView.setText("Sol y nubes");
        }
    }

    private String getFechaString(int i) {
        if (i == 0) {
            return "Hoy";
        }
        Calendar fecha = Calendar.getInstance();
        fecha.add(Calendar.DATE, i);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        return sdf.format(fecha.getTime());
    }

    public void getWeather(LineaReciclerFav lineaReciclerFav, String identificadorLugar, String units) {
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<TemperaturaData> call = weatherAPI.getCourse(identificadorLugar, units);
        call.enqueue(new Callback<TemperaturaData>() {
            @Override
            public void onResponse(Call<TemperaturaData> call, Response<TemperaturaData> response) {
                if (response.isSuccessful()) {
                    result = response.body();
                    lineaReciclerFav.getLugar().setText(identificadorLugar);
                    lineaReciclerFav.getTemperatura().setText(result.getList().get(0).getMain().getTemp());
                    updateImage(lineaReciclerFav.getImagen(), result.getList().get(0).getWeather().get(0).getDescription());
                }
            }
            @Override
            public void onFailure(Call<TemperaturaData> call, Throwable t) {
            }
        });
    }
}
