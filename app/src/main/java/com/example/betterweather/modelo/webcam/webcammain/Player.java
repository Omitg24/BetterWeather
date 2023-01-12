package com.example.betterweather.modelo.webcam.webcammain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("live")
    @Expose
    private Live live;
    @SerializedName("day")
    @Expose
    private Day day;
    @SerializedName("month")
    @Expose
    private Month month;
    @SerializedName("year")
    @Expose
    private Year year;
    @SerializedName("lifetime")
    @Expose
    private Lifetime lifetime;

    public Live getLive() {
        return live;
    }

    public void setLive(Live live) {
        this.live = live;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Lifetime getLifetime() {
        return lifetime;
    }

    public void setLifetime(Lifetime lifetime) {
        this.lifetime = lifetime;
    }

}
