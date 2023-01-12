package com.example.betterweather.apiUtils;

import com.example.betterweather.modelo.webcam.webcamMain.WebcamMain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebcamMainAPI {

    @GET("webcam={idwebcam}?show=webcams:location,image,player&key=RVIQq7qhWTW1f0aGSFMGCKf8yQdrKgC3")
    Call<WebcamMain> getCourse(@Path("idwebcam") Long id);
}
