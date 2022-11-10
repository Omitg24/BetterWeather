package com.example.BetterWeather;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import com.example.BetterWeather.apiUtils.WeatherAPI;
import com.example.BetterWeather.modelo.TemperaturaData;
import com.example.BetterWeather.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {

    private String paramsUrl;

    private TemperaturaData result;

    private String urlBase = "https://api.openweathermap.org/data/2.5/";

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
                        ImageView imageViewPrincipal = activity.findViewById(R.id.imageViewPrincipal);
                        imageViewPrincipal.setImageResource(getIdOfImageView(result.getList().get(0).getWeather().get(0).getDescription()));
                        TextView textViewPrincipal = activity.findViewById(R.id.textViewDescripcion);
                        textViewPrincipal.setText(getIdOfTextView(result.getList().get(0).getWeather().get(0).getDescription()));
                    } else {
                        Toast.makeText(activity, "No se ha podido realizar la solicitud, intenta ser mas especifico con el lugar :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TemperaturaData> call, Throwable t) {
                Toast.makeText(activity, "No se ha podido realizar la solicitud :(", Toast.LENGTH_SHORT).show();
            }
        });
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
            imagenTiempo.setImageResource(getIdOfImageView(result.getList().get(i).getWeather().get(0).getDescription()));
            imagenTiempo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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

    private int getIdOfImageView(String description) {
        if (description.contains("nubes")) {
            return R.mipmap.ic_launcher_nubes_foreground;
        }
        if (description.contains("sol")) {
            return R.mipmap.ic_sol;
        }
        if (description.contains("lluvia")) {
            return R.mipmap.ic_launcher_lluvia_foreground;
        }
        if (description.contains("nieve")) {
            return R.mipmap.ic_launcher_nieve_foreground;
        }
        if (description.contains("tormenta")) {
            return R.mipmap.ic_launcher_tormenta_foreground;
        }
        return R.mipmap.ic_launcher_solnubes_foreground;
    }

    private String getIdOfTextView(String description) {
        if (description.contains("nubes")) {
            return "Nuboso";
        }
        if (description.contains("sol")) {
            return "Soleado";
        }
        if (description.contains("lluvia")) {
            return "Lluvia";
        }
        if (description.contains("nieve")) {
            return "Nieve";
        }
        if (description.contains("tormenta")) {
            return "Tormentas";
        }
        return "Sol y Nubes";
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

}
