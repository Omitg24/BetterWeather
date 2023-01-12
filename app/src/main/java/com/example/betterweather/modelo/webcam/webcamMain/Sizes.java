package com.example.betterweather.modelo.webcam.webcamMain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes {

    @SerializedName("icon")
    @Expose
    private Icon icon;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("toenail")
    @Expose
    private Toenail toenail;

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public Toenail getToenail() {
        return toenail;
    }

    public void setToenail(Toenail toenail) {
        this.toenail = toenail;
    }

}
