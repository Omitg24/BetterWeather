package com.example.betterweather.modelo;

import java.util.ArrayList;

public class ObjetoLista {

    private String name;
    private Coord coord;
    private MainData main;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private ArrayList<Weather> weather;

    public ObjetoLista(String name, MainData main, Wind wind, Rain rain, Clouds clouds, ArrayList<Weather> weather) {
        this.name = name;
        this.main = main;
        this.wind = wind;
        this.rain = rain;
        this.clouds = clouds;
        this.weather = weather;
    }

    public ObjetoLista(String name, Coord coord, MainData main, Wind wind, Rain rain, Clouds clouds, ArrayList<Weather> weather) {
        this.name = name;
        this.coord = coord;
        this.main = main;
        this.wind = wind;
        this.rain = rain;
        this.clouds = clouds;
        this.weather = weather;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainData getMain() {
        return main;
    }

    public void setMain(MainData main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }
}
