package com.example.betterweather.modelo.ui;

import android.widget.ImageView;
import android.widget.TextView;

public class LineaReciclerFav {

    private ImageView imagen;
    private TextView lugar;
    private TextView temperatura;

    public LineaReciclerFav(ImageView imagen, TextView lugar, TextView temperatura) {
        this.imagen = imagen;
        this.lugar = lugar;
        this.temperatura = temperatura;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }

    public TextView getLugar() {
        return lugar;
    }

    public void setLugar(TextView lugar) {
        this.lugar = lugar;
    }

    public TextView getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(TextView temperatura) {
        this.temperatura = temperatura;
    }
}
