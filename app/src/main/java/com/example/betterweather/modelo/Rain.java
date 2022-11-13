package com.example.betterweather.modelo;

public class Rain {

    private String horas;

    private String litrosMetroCuadradoHoras;

    public Rain(String horas, String litrosMetroCuadradoHoras) {
        this.horas = horas;
        this.litrosMetroCuadradoHoras = litrosMetroCuadradoHoras;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getLitrosMetroCuadradoHoras() {
        return litrosMetroCuadradoHoras;
    }

    public void setLitrosMetroCuadradoHoras(String litrosMetroCuadradoHoras) {
        this.litrosMetroCuadradoHoras = litrosMetroCuadradoHoras;
    }
}
