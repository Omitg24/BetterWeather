package com.example.betterweather.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.betterweather.modelo.weatherpojos.Lugar;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejemplo <b>SQLite</b>. Ejemplo de uso de SQLite.
 *
 * DAO para la tabla de actor.
 * Se encarga de abrir y cerrar la conexion, asi como hacer las consultas relacionadas con la tabla Actor
 *

 */
public class LugaresDataSource {
    /**
     * Referencia para manejar la base de datos. Este objeto lo obtenemos a partir de MyDBHelper
     * y nos proporciona metodos para hacer operaciones
     * CRUD (create, read, update and delete)
     */
    private SQLiteDatabase database;
    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;
    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = { MyDBHelper.COLUMNA_ID_LUGAR };
    /**
     * Constructor.
     *
     * @param context
     */
    public LugaresDataSource(Context context) {
        //el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    /**
     * Abre una conexion para escritura con la base de datos.
     * Esto lo hace a traves del helper con la llamada a getWritableDatabase. Si la base de
     * datos no esta creada, el helper se encargara de llamar a onCreate
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Cierra la conexion con la base de datos
     */
    public void close() {
        dbHelper.close();
    }

    public long removePlace(Lugar lugarToRemove) {
        // Borramos el lugar
        long removeId =
                database.delete(MyDBHelper.TABLA_LUGARES, "id_lugar=?",
                        new String[]{lugarToRemove.getIdentificadorLugar().toLowerCase().trim()});

        return removeId;
    }

    /**
     * Metodo que inserta un lugar pasado por parametro a la base de datos
     * @param lugarToInsert
     * @return
     */
    public long createPlace(Lugar lugarToInsert) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_LUGAR, lugarToInsert.getIdentificadorLugar().toLowerCase().trim());

        // Insertamos el lugar
        long insertId =
                database.insert(MyDBHelper.TABLA_LUGARES, null, values);

        return insertId;
    }

    /**
     * Metodo que busca un lugar en la base de datos
     * @param lugar
     * @return
     */
    public boolean findPlace(Lugar lugar){
        open();
        List<Lugar> lugares = getAllPlaces();
        for(int i=0;i<lugares.size();i++){
            if (lugares.get(i).getIdentificadorLugar().toLowerCase().equals(lugar.getIdentificadorLugar().toLowerCase().trim())){
                close();
                return true;
            }
        }
        close();
        return false;
    }

    /**
     * Metodo que devuelve todos los lugares de la bbdd
     * @return
     */
    public List<Lugar> getAllPlaces() {
        // Lista que almacenara el resultado
        List<Lugar> lugarList = new ArrayList<Lugar>();
        //hacemos una query porque queremos devolver un cursor

            Cursor cursor = database.query(MyDBHelper.TABLA_LUGARES, allColumns,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                final Lugar actor = new Lugar();
                String lugar = cursor.getString(0);
                String res = lugar.substring(0, 1).toUpperCase() + lugar.substring(1);
                actor.setIdentificadorLugar(res);
                lugarList.add(actor);
                cursor.moveToNext();
            }

            cursor.close();

        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return lugarList;
    }
}
