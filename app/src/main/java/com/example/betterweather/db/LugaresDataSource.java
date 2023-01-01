package com.example.betterweather.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.betterweather.modelo.Lugar;

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


    public long createLugar(Lugar lugarToInsert) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_LUGAR, lugarToInsert.getIdentificadorLugar());

        // Insertamos la valoracion
        long insertId =
                database.insert(MyDBHelper.TABLA_LUGARES, null, values);

        return insertId;
    }

    public List<Lugar> getAllPlaces() {
        // Lista que almacenara el resultado
        List<Lugar> lugarList = new ArrayList<Lugar>();
        //hacemos una query porque queremos devolver un cursor

            Cursor cursor = database.query(MyDBHelper.TABLA_LUGARES, allColumns,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                final Lugar actor = new Lugar();
                actor.setIdentificadorLugar(cursor.getString(0));
                lugarList.add(actor);
                cursor.moveToNext();
            }

            cursor.close();

        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return lugarList;
    }

    /**
     * Devuelve una lista con todos los actores que participan en la película con el id pasado por parámetro.
     * @param id_pelicula
     * @return reparto

    public List<Actor> actoresParticipantes(int id_pelicula) {
        List<Actor> reparto = new ArrayList<Actor>();

        // La expresión SQL correspondiente a la busqueda en un String.
        Cursor cursor = database.rawQuery("SELECT " +
                MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_NOMBRE_ACTOR + ", " +
                MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_PERSONAJE + ", " +
                MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_IMAGEN_ACTOR + ", " +
                MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_URL_imdb +
                " FROM " + MyDBHelper.TABLA_PELICULAS_REPARTO +
                " JOIN " + MyDBHelper.TABLA_REPARTO + " ON " +
                MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_ID_REPARTO +
                " = " + MyDBHelper.TABLA_REPARTO + "." + MyDBHelper.COLUMNA_ID_REPARTO +
                " WHERE " + MyDBHelper.TABLA_PELICULAS_REPARTO + "." + MyDBHelper.COLUMNA_ID_PELICULAS + " = " + id_pelicula, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            reparto.add(new Actor(cursor.getString(0),
                    cursor.getString(1),
                    //Añadimos el encabezado de las páginas web
                    "https://image.tmdb.org/t/p/original/" + cursor.getString(2),
                    "https://www.imdb.com/name/" + cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();

        return reparto;
    }
     */

}
