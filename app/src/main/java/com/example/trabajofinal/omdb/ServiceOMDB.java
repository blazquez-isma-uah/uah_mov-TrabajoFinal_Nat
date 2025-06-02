package com.example.trabajofinal.omdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz para definir los servicios de la API OMDB.
 */
public interface ServiceOMDB {

    /**
     * Busca películas en la API OMDB por título y tipo.
     *
     * @param apiKey La clave de API para autenticar la solicitud.
     * @param tipo El tipo de búsqueda (ejemplo: "movie", "series").
     * @param titulo El título de la película a buscar.
     * @return Un objeto Call que contiene la respuesta de la búsqueda.
     */
    @GET("/")
    Call<SearchResponseOMDB> buscarPeliculas(
            @Query("apikey") String apiKey,
            @Query("type") String tipo,
            @Query("s") String titulo
    );

    /**
     * Obtiene los detalles de una película específica por su IMDb ID.
     *
     * @param apiKey La clave de API para autenticar la solicitud.
     * @param imdbID El ID de IMDb de la película a obtener.
     * @return Un objeto Call que contiene los detalles de la película.
     */
    @GET("/")
    Call<PeliculaOMDB> obtenerDetallePelicula(
            @Query("apikey") String apiKey,
            @Query("i") String imdbID
    );

}
