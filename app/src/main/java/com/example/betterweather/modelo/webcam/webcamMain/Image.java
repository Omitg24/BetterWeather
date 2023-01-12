package com.example.betterweather.modelo.webcam.webcamMain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("sizes")
    @Expose
    private Sizes sizes;
    @SerializedName("daylight")
    @Expose
    private Daylight daylight;
    @SerializedName("update")
    @Expose
    private Integer update;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Sizes getSizes() {
        return sizes;
    }

    public void setSizes(Sizes sizes) {
        this.sizes = sizes;
    }

    public Daylight getDaylight() {
        return daylight;
    }

    public void setDaylight(Daylight daylight) {
        this.daylight = daylight;
    }

    public Integer getUpdate() {
        return update;
    }

    public void setUpdate(Integer update) {
        this.update = update;
    }

}
