package com.example.betterweather.apiUtils;

import com.example.betterweather.modelo.webcam.webcamID.WebcamID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebcamIdAPI {

    @GET("nearby={coords},{zoom}?key=RVIQq7qhWTW1f0aGSFMGCKf8yQdrKgC3")

        // as we are calling data from array
        // so we are calling it with json object
        // and naming that method as getCourse();
    Call<WebcamID> getCourse(@Path("coords") String coords, @Path("zoom") int zoom);
}
