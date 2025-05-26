package com.example.trabajofinal.omdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz de Retrofit para el servicio OMDB API.
 */
public interface ServiceOMDB {

    @GET("/")
    Call<SearchResponseOMDB> buscarPeliculas(
            @Query("apikey") String apiKey,
            @Query("s") String titulo
    );

}
