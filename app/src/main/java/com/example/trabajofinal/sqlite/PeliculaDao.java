package com.example.trabajofinal.sqlite;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PeliculaDao {

    @Insert
    void insertarPelicula(PeliculaGuardada pelicula);

    @Query("SELECT * FROM PeliculaGuardada")
    List<PeliculaGuardada> obtenerTodas();
}
