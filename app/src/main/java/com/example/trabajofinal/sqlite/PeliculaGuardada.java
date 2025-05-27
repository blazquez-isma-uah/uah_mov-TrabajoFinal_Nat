package com.example.trabajofinal.sqlite;

import androidx.room.PrimaryKey;

public class PeliculaGuardada {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public String anio;
    public String actorPrincipal;
    public String ciudad;
    public String fecha;

    public PeliculaGuardada(String titulo, String anio, String actorPrincipal, String ciudad, String fecha) {
        this.titulo = titulo;
        this.anio = anio;
        this.actorPrincipal = actorPrincipal;
        this.ciudad = ciudad;
        this.fecha = fecha;
    }
}
