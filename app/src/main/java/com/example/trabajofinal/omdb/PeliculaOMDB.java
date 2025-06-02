package com.example.trabajofinal.omdb;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que representa una película obtenida de la API OMDB.
 * Contiene información básica como título, año, ID de IMDb y actores.
 */
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

    /**
     * Obtiene el actor principal de la película.
     * Asume que el primer actor en la lista es el principal.
     * Si no hay actores, retorna "Desconocido".
     *
     * @return El nombre del actor principal o "Desconocido" si no hay actores.
     */
    public String getActorPrincipal(){
        if (actores != null && !actores.isEmpty()) {
            String[] actoresArray = actores.split(",");
            if (actoresArray.length > 0) {
                return actoresArray[0].trim();
            }
        }
        return "Desconocido";
    }
}
