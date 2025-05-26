package com.example.trabajofinal.omdb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Clase que representa la respuesta de búsqueda de películas de la API OMDB.
 * Esta clase se utiliza para mapear la respuesta JSON de la API a un objeto Java.
 */
public class SearchResponseOMDB {

    //SerializedName indica el nombre del campo en el JSON que se corresponde con esta variable
    @SerializedName("Search")
    private List<PeliculaOMDB> searchResults;

    public List<PeliculaOMDB> getSearchResults() {
        return searchResults;
    }
}
