package com.example.betterweather.modelo.webcam.webcamid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebcamID {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private Result result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
