package com.example.trabajofinal.omdb;

import com.google.gson.annotations.SerializedName;

public class PeliculaOMDB {

    @SerializedName("Title")
    private String titulo;

    @SerializedName("Year")
    private String anio;

    public PeliculaOMDB() {
        // Constructor vacío
    }

    public PeliculaOMDB(String titulo, String anio) {
        this.titulo = titulo;
        this.anio = anio;
    }
    public String getTitulo() {
        return titulo;
    }

    public String getAnio() {
        return anio;
    }
}
