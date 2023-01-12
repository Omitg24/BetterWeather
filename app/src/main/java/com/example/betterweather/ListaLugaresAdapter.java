package com.example.betterweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterweather.util.api.ApiManager;
import com.example.betterweather.util.handler.weather.RecyclerWeatherHandler;
import com.example.betterweather.modelo.weatherpojos.Lugar;
import com.example.betterweather.modelo.recycler.LineaReciclerFav;
import com.example.betterweather.modelo.info.weather.WeatherCallInfo;
import com.example.betterweather.util.weather.WeatherUtil;

import java.util.List;

public class ListaLugaresAdapter extends RecyclerView.Adapter<ListaLugaresAdapter.LugarViewHolder> {


    // Interfaz para manejar el evento click sobre un elemento
    public interface OnItemClickListener {
        void onItemClick(Lugar item);
        void onDeleteItem(Lugar item);
    }

    private List<Lugar> listaLugares;
    private final OnItemClickListener listener;

    public ListaLugaresAdapter(List<Lugar> listaLugares, OnItemClickListener listener) {
        this.listaLugares = listaLugares;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista con el layout para un elemento
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_lugar, parent, false);
        return new LugarViewHolder(itemView);
    }


    /** Asocia el contenido a los componentes de la vista,
     * concretamente con nuestro LugarViewHolder que recibimos como parámetro
     */
    @Override
    public void onBindViewHolder(@NonNull LugarViewHolder holder, int position) {
        // Extrae de la lista el elemento indicado por posición
        Lugar lugar= listaLugares.get(position);
        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(lugar, listener);
    }

    @Override
    public int getItemCount() {
        return listaLugares.size();
    }


    /*Clase interna que define los compoonentes de la vista*/

    public static class LugarViewHolder extends RecyclerView.ViewHolder{

        private LineaReciclerFav lineaReciclerFav;

        public LugarViewHolder(View itemView) {
            super(itemView);

            lineaReciclerFav = new LineaReciclerFav(
                    (ImageView)itemView.findViewById(R.id.imagenReciclerLinea),
                    (TextView)itemView.findViewById(R.id.valorLugar),
                    (TextView)itemView.findViewById(R.id.valorTemperatura));
        }

        /**
         * Metodo que asigna valor a los componentes de la vista
         * @param lugar
         * @param listener
         */
        public void bindUser(final Lugar lugar, final OnItemClickListener listener) {
            ApiManager manager = new ApiManager();
            WeatherCallInfo weatherCallInfo = new WeatherCallInfo(lugar.getIdentificadorLugar(),
                    WeatherUtil.getUnit(MainActivity.getSpinnerUnits().getSelectedItem().toString()));
            manager.getWeather(weatherCallInfo,new RecyclerWeatherHandler(lineaReciclerFav,weatherCallInfo));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(lugar);
                }
            });

            Button botonBorrar = (Button) itemView.findViewById(R.id.botonBorrar);
            botonBorrar.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { listener.onDeleteItem(lugar); }
            });
        }
    }
}
