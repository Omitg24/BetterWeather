package com.example.BetterWeather.apiUtils;

import com.example.BetterWeather.modelo.TemperaturaData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    // as we are making get request
    // so we are displaying GET as annotation.
    // and inside we are passing
    // last parameter for our url.
    @GET("find?mode=json&lang=es&APPID=43659c6dc1582c2ec51e7a6ce96d6c7d")

    // as we are calling data from array
    // so we are calling it with json object
    // and naming that method as getCourse();
    Call<TemperaturaData> getCourse(@Query("q") String place, @Query("units") String unidad);
}
