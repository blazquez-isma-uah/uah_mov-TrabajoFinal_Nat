package com.example.trabajofinal.sqlite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Clase principal de acceso a la base de datos Room
 */
@Database(entities = {PeliculaGuardada.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract PeliculaDao peliculaDao();

    /**
     * Método para obtener una instancia de la base de datos.
     * Utiliza el patrón Singleton para asegurar que solo haya una instancia.
     *
     * @param contexto Contexto de la aplicación
     * @return Instancia de AppDatabase
     */
    public static AppDatabase getInstance(Context contexto) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    contexto.getApplicationContext(),
                    AppDatabase.class,
                    "peliculas_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}
