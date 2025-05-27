package com.example.trabajofinal.omdb;

import com.google.gson.annotations.SerializedName;

public class PeliculaOMDB {

    @SerializedName("Title")
    private String titulo;

    @SerializedName("Year")
    private String anio;

    @SerializedName("imdbID")
    private String imdbID;

    // Lista de actores separados por comas
    @SerializedName("Actors")
    private String actores;

    public PeliculaOMDB() {
        // Constructor vacío
    }
    public String getTitulo() {
        return titulo;
    }

    public String getAnio() {
        return anio;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getActores() {
        return actores;
    }

    public String getActorPrincipal(){
        if (actores != null && !actores.isEmpty()) {
            String[] actoresArray = actores.split(",");
            if (actoresArray.length > 0) {
                return actoresArray[0].trim(); // Retorna el primer actor como actor principal
            }
        }
        return "Desconocido"; // Retorna "Desconocido" si no hay actores
    }
}
