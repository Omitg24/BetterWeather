package com.example.betterweather.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MyDHelper
 */
public class MyDBHelper extends SQLiteOpenHelper {

    /**
     * Nombre y version de la base de datos
     */
    private static final String DATABASE_NAME = "lugares.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Nombre de la tabla películas y sus columnas
     * id;titulo;argumento;categoria;duracion;fecha;caratula;fondo;trailer
     */
    public static final String TABLA_LUGARES = "tabla_lugares";

    public static final String COLUMNA_ID_LUGAR = "id_lugar";

    /**
     * Script para crear la base datos en SQL
     */
    private static final String CREATE_TABLA_LUGARES = "create table if not exists " + TABLA_LUGARES
            + "( " +
            COLUMNA_ID_LUGAR + " " + "text primary key " +
             ");";

    /**
     * Script para borrar la base de datos (SQL)
     */
    private static final String DATABASE_DROP_LUGARES = "DROP TABLE IF EXISTS " + TABLA_LUGARES;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //invocamos execSQL pq no devuelve ningún tipo de dataset
        db.execSQL(CREATE_TABLA_LUGARES);
        Log.i("ONCREATE", "EJECUTO CREACION");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_LUGARES);
        this.onCreate(db);
    }

    public void borrarBd() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLA_LUGARES,null,null);
        close();
    }
}
