package com.example.betterweather;

import static com.example.betterweather.ApiManager.URL;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterweather.modelo.Lugar;
import com.example.betterweather.modelo.TemperaturaData;
import com.example.betterweather.modelo.ui.LineaReciclerFav;
import com.google.android.gms.common.api.Api;

import org.w3c.dom.Text;

import java.util.List;

public class ListaLugaresAdapter extends RecyclerView.Adapter<ListaLugaresAdapter.LugarViewHolder> {


    // Interfaz para manejar el evento click sobre un elemento
    public interface OnItemClickListener {
        void onItemClick(Lugar item);
    }

    private List<Lugar> listaLugares;
    private final OnItemClickListener listener;

    private String units = "metric";

    public ListaLugaresAdapter(List<Lugar> listaPeli, OnItemClickListener listener) {
        this.listaLugares = listaPeli;
        this.listener = listener;
    }

    /* Indicamos el layout a "inflar" para usar en la vista
     */
    @NonNull
    @Override
    public LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista con el layout para un elemento
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_lugar, parent, false);
        return new LugarViewHolder(itemView);
    }


    /** Asocia el contenido a los componentes de la vista,
     * concretamente con nuestro PeliculaViewHolder que recibimos como parámetro
     */
    @Override
    public void onBindViewHolder(@NonNull LugarViewHolder holder, int position) {
        // Extrae de la lista el elemento indicado por posición
        Lugar lugar= listaLugares.get(position);
        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(lugar,units, listener);
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

        // asignar valores a los componentes
        public void bindUser(final Lugar lugara,String units, final OnItemClickListener listener) {
            ApiManager manager = new ApiManager();
            manager.getWeather(lineaReciclerFav,lugara.getIdentificadorLugar(),units);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(lugara);
                }
            });
        }
    }
}