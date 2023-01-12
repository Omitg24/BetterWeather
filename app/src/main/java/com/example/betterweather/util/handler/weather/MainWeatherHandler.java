package com.example.betterweather.util.handler.weather;

import static com.example.betterweather.util.api.ApiManager.findWebcam;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import com.example.betterweather.MainActivity;
import com.example.betterweather.R;
import com.example.betterweather.modelo.weatherpojos.TemperaturaData;
import com.example.betterweather.util.weather.WeatherUtil;
import com.example.betterweather.util.handler.WeatherHandler;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainWeatherHandler implements WeatherHandler {

    private MainActivity activity;

    public MainWeatherHandler(MainActivity mainActivity){
        this.activity=mainActivity;
    }

    @Override
    public void handleSuccess(TemperaturaData result) {
        updateInfoViewDays(result);
        updateInfoViewData(result);
        TextView textViewTemperaturaMain = activity.findViewById(R.id.textViewTemperaturaMain);
        String unit = WeatherUtil.getUnitLetter(activity.getSpinnerUnits().getSelectedItem().toString());
        textViewTemperaturaMain.setText(result.getList().get(0).getMain().getTemp() + " " + unit);

        double lat = result.getList().get(0).getCoord().getLat();
        double lon = result.getList().get(0).getCoord().getLon();
        findWebcam(lat, lon, 5);

        ImageView imageViewPrincipal = activity.findViewById(R.id.iconWeatherCondition);
        TextView textViewPrincipal = activity.findViewById(R.id.textViewDescripcion);
        WeatherUtil.updateWeather(activity, imageViewPrincipal, textViewPrincipal, result.getList().get(0).getWeather().get(0).getDescription());
    }

    @Override
    public void handleFailure() {
        Snackbar.make(activity.findViewById(R.id.editTextPlaceSearch), "La solicitud no se ha podido realizar", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void handleNotExists() {
        Snackbar.make(activity.findViewById(R.id.editTextPlaceSearch), "El lugar que ha introducido no existe", Snackbar.LENGTH_SHORT).show();
    }

    private void updateInfoViewDays(TemperaturaData result) {
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

            TextView textoDescripcion = new TextView(grid.getContext());
            textoDescripcion.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            WeatherUtil.updateText(textoDescripcion, result.getList().get(i).getWeather().get(0).getDescription());

            ImageView imagenTiempo = new ImageView(grid.getContext());
            imagenTiempo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            WeatherUtil.updateImage(imagenTiempo, result.getList().get(i).getWeather().get(0).getDescription());

            auxLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            auxLayout.setOrientation(LinearLayout.VERTICAL);
            auxLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            auxLayout.addView(textoDia);
            auxLayout.addView(imagenTiempo);
            imagenTiempo.getLayoutParams().width = 250;
            imagenTiempo.requestLayout();
            textoDia.setTextColor(Color.BLACK);
            textoDescripcion.setTextColor(Color.BLACK);
            textoMinima.setTextColor(Color.BLACK);
            textoMaxima.setTextColor(Color.BLACK);
            auxLayout.addView(textoDescripcion);
            auxLayout.addView(textoMaxima);
            auxLayout.addView(textoMinima);
            grid.addView(auxLayout);
        }
    }

    private void updateInfoViewData(TemperaturaData result) {
        TextView datosIzda = (TextView) activity.findViewById(R.id.datosGeneralesIzda);
        TextView datosDcha = (TextView) activity.findViewById(R.id.datosGeneralesDcha);
        String velocidad = result.getList().get(0).getWind() != null ? result.getList().get(0).getWind().getSpeed() : "No hay datos";
        String direccion = result.getList().get(0).getWind() != null ? result.getList().get(0).getWind().getDeg() : "No hay datos";
        String humedad = result.getList().get(0).getMain() != null ? result.getList().get(0).getMain().getHumidity() : "No hay datos";
        String presion = result.getList().get(0).getMain() != null ? result.getList().get(0).getMain().getPressure() : "No hay datos";
        String lat = result.getList().get(0).getCoord() != null ? String.valueOf(result.getList().get(0).getCoord().getLat()) : "No hay datos";
        String lon = result.getList().get(0).getCoord() != null ? String.valueOf(result.getList().get(0).getCoord().getLon()) : "No hay datos";

        String textIzda = String.format("Humedad: %s/100\n" +
                        "Latitud: %s\n" +
                        "Longitud: %s " ,
                humedad, lat, lon);
        String textDcha = String.format("Presión: %s PA\n" +
                        "Viento: %s m/s\n" +
                        "Dirección: %sº",
                presion, velocidad, direccion);
        datosIzda.setText(textIzda);
        datosDcha.setText(textDcha);
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
