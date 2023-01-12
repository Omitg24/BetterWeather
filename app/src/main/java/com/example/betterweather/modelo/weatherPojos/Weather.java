package com.example.betterweather.modelo.weatherPojos;

public class Weather {

    private String main;

    private String description;

    public Weather(String main, String description) {
        this.main = main;
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescritcion(String description) {
        this.description = description;
    }
}
