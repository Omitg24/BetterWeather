package com.example.betterweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Conexion {

    private Context aContexto;

    public Conexion(Context aContexto) {
        this.aContexto = aContexto;
    }

    public boolean CompruebaConexion() {
        boolean conectado = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) aContexto.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        conectado = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return conectado;
    }
}
