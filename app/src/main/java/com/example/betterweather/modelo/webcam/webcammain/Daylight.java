package com.example.betterweather.modelo.webcam.webcammain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Daylight {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("toenail")
    @Expose
    private String toenail;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getToenail() {
        return toenail;
    }

    public void setToenail(String toenail) {
        this.toenail = toenail;
    }

}
