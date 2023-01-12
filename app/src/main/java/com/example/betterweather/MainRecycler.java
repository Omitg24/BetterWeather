package com.example.betterweather;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterweather.db.LugaresDataSource;
import com.example.betterweather.modelo.Lugar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainRecycler extends AppCompatActivity {

    // identificador de intent
    public static final String LUGAR_SELECCIONADO = "lugar_seleccionado";
    public static final String LUGAR_CREADO = "lugar_creado";

    private static final int GESTION_ACTIVITY = 1;

    private List<Lugar> listaLugaresFavoritos;      // lista favoritas de la BD
    private Lugar lugar;

    private RecyclerView listaLugarView;

    private BottomNavigationView bottomNav;

    //SharedPreference de la MainRecycler
    private SharedPreferences sharedPreferencesMainRecycler;

    //Objetos para las notificaciones
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

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
        bottomNav = findViewById(R.id.navigation_menu);
        bottomNav.setSelectedItemId(R.id.favourites);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favourites:
                        return true;
                    case R.id.home:
                        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                        intentHome.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intentHome);
                        return true;
                    case R.id.map:
                        Intent intentMap = new Intent(getApplicationContext(), MapsActivity.class);
                        intentMap.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intentMap);
                        return true;
                }
                return false;
            }
        });

        cargarView();
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.favourites);
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

                    @Override
                    public void onDeleteItem(Lugar lugar) {
                        deleteItem(lugar);
                        cargarView();
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

    // Click del item del adapter
    public void clickonItem(Lugar lugar) {
        //Paso el modo de apertura
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(LUGAR_SELECCIONADO, lugar);
        setResult(RESULT_OK,intent);
        if(getCallingActivity()!=null){
            if(getCallingActivity().getClassName().contains("MainActivity")){
                finish();
            }else{
                startActivity(intent);
                MainActivity.procesaCambio(lugar);
            }
        }else{
            startActivity(intent);
            MainActivity.procesaCambio(lugar);
        }
    }

    public void deleteItem(Lugar lugar) {
        LugaresDataSource lds = new LugaresDataSource(getApplicationContext());
        lds.open();
        lds.removePlace(lugar);
        lds.close();
        Snackbar.make(findViewById(R.id.reciclerView), "Se ha borrado " + lugar.getIdentificadorLugar(),
                Snackbar.LENGTH_SHORT).show();
    }
}


