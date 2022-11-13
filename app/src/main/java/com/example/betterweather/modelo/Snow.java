package com.example.betterweather.modelo;

public class Snow {

    private String horas;

    private String volumenHoras;

    public Snow(String horas, String volumenHoras) {
        this.horas = horas;
        this.volumenHoras = volumenHoras;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getVolumenHoras() {
        return volumenHoras;
    }

    public void setVolumenHoras(String volumenHoras) {
        this.volumenHoras = volumenHoras;
    }
}
