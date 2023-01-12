package com.example.betterweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterweather.modelo.weatherpojos.Lugar;
import com.example.betterweather.util.db.LugaresDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainRecycler extends AppCompatActivity {

    public static final String LUGAR_SELECCIONADO = "lugar_seleccionado";

    private List<Lugar> listaLugaresFavoritos;      // lista favoritas de la BD

    private RecyclerView listaLugarView;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycler);

        listaLugarView = (RecyclerView) findViewById(R.id.reciclerView);
        listaLugarView.setHasFixedSize(true);

        loadMenu();

        cargarView();
    }

    /**
     * Metodo que carga el menu de navegacion
     */
    private void loadMenu() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.favourites);
        cargarView();
    }

    /**
     * Usaremos este método para cargar el RecyclerView, la lista de lugares y el Adapter.
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
     * Recupera todas los lugares de la tabla lugares de la BD
     * y las carga en la lista: listaLugaresFavoritos
     */
    private void recuperarLugaresFavoritosDb() {
        LugaresDataSource lugaresDataSource = new LugaresDataSource(getApplicationContext());
        lugaresDataSource.open();
        listaLugaresFavoritos = lugaresDataSource.getAllPlaces();
        //Cerrar
        lugaresDataSource.close();
    }

    /**
     * Metodo que abre la actividad principal con el lugar pasado por parametro
     * @param lugar el lugar con el que abriremos la actividad principal
     */
    public void clickonItem(Lugar lugar) {
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

    /**
     * Metodo que elimina un lugar de la bbdd
     * @param lugar
     */
    public void deleteItem(Lugar lugar) {
        LugaresDataSource lds = new LugaresDataSource(getApplicationContext());
        lds.open();
        lds.removePlace(lugar);
        lds.close();
        Toast.makeText(this, "Se ha borrado " + lugar.getIdentificadorLugar(), Toast.LENGTH_SHORT).show();
    }
}


