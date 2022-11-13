package com.example.betterweather.modelo;

import java.util.List;

public class TemperaturaData {

    private List<ObjetoLista> list;

    public TemperaturaData(List<ObjetoLista> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TemperaturaData{" +
                "list=" + list +
                '}';
    }

    public List<ObjetoLista> getList() {
        return list;
    }

    public void setList(List<ObjetoLista> list) {
        this.list = list;
    }
}
