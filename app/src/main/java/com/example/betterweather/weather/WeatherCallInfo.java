package com.example.betterweather.weather;

public class WeatherCallInfo {

    private String city;

    private String units;

    public WeatherCallInfo(String city, String units) {
        this.city = city;
        this.units = units;
    }

    public WeatherCallInfo(String city) {
        this.city = city;
        this.units="metric";
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUnits() {
        if(units==null){
            return "metric";
        }
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
