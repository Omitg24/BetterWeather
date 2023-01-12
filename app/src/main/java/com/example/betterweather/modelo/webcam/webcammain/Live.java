package com.example.betterweather.modelo.webcam.webcammain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Live {

    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("embed")
    @Expose
    private String embed;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

}
