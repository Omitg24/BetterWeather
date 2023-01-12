package com.example.betterweather.modelo.weatherpojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Lugar implements Parcelable {

    private String identificadorLugar;

    public Lugar(String identificadorLugar) {
        this.identificadorLugar = identificadorLugar;
    }

    public Lugar(){}


    protected Lugar(Parcel in) {
        identificadorLugar = in.readString();
    }

    public static final Creator<Lugar> CREATOR = new Creator<Lugar>() {
        @Override
        public Lugar createFromParcel(Parcel in) {
            return new Lugar(in);
        }

        @Override
        public Lugar[] newArray(int size) {
            return new Lugar[size];
        }
    };

    public String getIdentificadorLugar() {
        return identificadorLugar;
    }

    public void setIdentificadorLugar(String identificadorLugar) {
        this.identificadorLugar = identificadorLugar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identificadorLugar);
    }
}
