package com.example.betterweather.modelo.webcam.webcamMain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lifetime {

    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("embed")
    @Expose
    private String embed;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

}
