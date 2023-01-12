package com.example.betterweather.util.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.betterweather.MainActivity;
import com.example.betterweather.R;

public class WeatherUtil {

    public static void updateWeather(MainActivity activity, ImageView imageViewPrincipal, TextView textViewPrincipal, String weather) {
        updateBackground(activity, weather);
        updateImage(imageViewPrincipal, weather);
        updateText(textViewPrincipal, weather);
    }

    public static void updateBackground(MainActivity activity, String weather) {
        View background = activity.findViewById(R.id.main_layout);
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

    public static void updateImage(ImageView imageView, String weather) {
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

    public static void updateText(TextView textView, String weather) {
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

    public static String getSpanishText(String weather){
        if (weather.equals("clear sky")) {
            return "Soleado";
        } else if (weather.equals("few clouds")) {
            return "Sol y nubes";
        } else if (weather.equals("broken clouds")) {
            return "Nuboso";
        } else if (weather.contains("cloud")) {
            return "Nubes leves";
        } else if (weather.equals("shower rain")) {
            return "Lluvia leve";
        } else if (weather.contains("rain")) {
            return "Lluvioso";
        } else if (weather.equals("thunderstorm")) {
            return "Tormenta";
        } else if (weather.equals("snow")) {
            return "Nieve";
        } else if (weather.equals("mist")) {
            return "Nublado";
        } else {
            return "Sol y nubes";
        }
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
}
