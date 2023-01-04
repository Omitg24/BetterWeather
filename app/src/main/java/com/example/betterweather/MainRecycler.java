package com.example.betterweather;

import android.app.ActivityOptions;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterweather.db.LugaresDataSource;
import com.example.betterweather.modelo.Lugar;

import java.util.List;

public class MainRecycler extends AppCompatActivity {

    // identificador de intent
    public static final String LUGAR_SELECCIONADO = "lugar_seleccionado";
    public static final String LUGAR_CREADO = "lugar_creado";

    private static final int GESTION_ACTIVITY = 1;

    List<Lugar> listaLugaresFavoritos;      // lista favoritas de la BD
    Lugar lugar;

    RecyclerView listaLugarView;

    //SharedPreference de la MainRecycler
    SharedPreferences sharedPreferencesMainRecycler;

    //Objetos para las notificaciones
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

    /**
     * Boolean que indica si es la primera ejecución. Sirve para evitar que el onResume cargue el RecyclerView antes que la base de datos.
     * Una vez cargada la base de datos, primeraEjecucion toma el valor false.
     */
    //  private boolean primeraEjecucion = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        listaLugarView = (RecyclerView) findViewById(R.id.reciclerView);
        listaLugarView.setHasFixedSize(true);
        cargarView();
    }

    /**
     * Usaremos este método para cargar el RecyclerView, la lista de películas y el Adapter.
     * Este método se invoca desde onResume (especialmente
     */
    protected void cargarView() {
        recuperarLugaresFavoritosDb();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaLugarView.setLayoutManager(layoutManager);
        ListaLugaresAdapter lpAdapter = new ListaLugaresAdapter(listaLugaresFavoritos,
                new ListaLugaresAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Lugar lugar) {
                        clickonItem(lugar);
                    }
                });

        listaLugarView.setAdapter(lpAdapter);
    }

    /**
     * Recupera todas ls peliculas de la tabla peliculas (favoritas) de la BD
     * y las carga en la lista: listaPeliFavoritas
     */
    private void recuperarLugaresFavoritosDb() {
        LugaresDataSource lugaresDataSource = new LugaresDataSource(getApplicationContext());
        lugaresDataSource.open();
        listaLugaresFavoritos = lugaresDataSource.getAllPlaces();
        //Cerrar
        lugaresDataSource.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos a qué petición se está respondiendo
        if (requestCode == GESTION_ACTIVITY) {
            // Nos aseguramos que el resultado fue OK
            if (resultCode == RESULT_OK) {
                lugar = data.getParcelableExtra(LUGAR_CREADO);


                // Refrescar el ReciclerView
                //Añadimos a la lista de peliculas la peli nueva
                listaLugaresFavoritos.add(lugar);

                //creamos un nuevo adapter que le pasamos al recyclerView
                ListaLugaresAdapter listaPeliculasAdapter = new ListaLugaresAdapter(listaLugaresFavoritos,
                        new ListaLugaresAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Lugar lugar) {
                                clickonItem(lugar);
                            }
                        });


                listaLugarView.setAdapter(listaPeliculasAdapter);
            }
        }


    }

    // Click del item del adapter
    public void clickonItem(Lugar lugar) {
        //Paso el modo de apertura
        Intent intent = new Intent();
        intent.putExtra(LUGAR_SELECCIONADO, lugar);
        setResult(RESULT_OK,intent);
        finish();
    }


    // Creamos la lista de peliculas
    // EN la versiçon de la BD, desaparace*/
   /* private void rellenarLista() {
        listaPeli = new ArrayList<Pelicula>();
        Categoria cataccion = new Categoria("Acción", "PelisAccion");
        Pelicula peli = new Pelicula("Tenet", "Una acción épica que gira en torno al espionaje internacional, los viajes en el tiempo y la evolución, en la que un agente secreto debe prevenir la Tercera Guerra Mundial.",
                cataccion, "150", "26/8/2020","","","");
        listaPeli.add(peli);

    }*/
}


