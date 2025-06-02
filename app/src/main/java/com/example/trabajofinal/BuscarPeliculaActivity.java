package com.example.trabajofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajofinal.omdb.PeliculaOMDB;
import com.example.trabajofinal.omdb.PeliculasAdapter;
import com.example.trabajofinal.omdb.SearchResponseOMDB;
import com.example.trabajofinal.omdb.ServiceOMDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscarPeliculaActivity extends AppCompatActivity {

    private EditText editTextBuscar;
    private Button buttonBuscar, botonAtras;
    private RecyclerView recyclerViewResultados;

    private List<PeliculaOMDB> listaPeliculas;
    private PeliculasAdapter peliculaAdapter;

    private ServiceOMDB service;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_pelicula);

        // Recuperar apiKey
        apiKey = getString(R.string.ombd_api_key);

        configureServer();
        initializeWidgets();

        buttonBuscar.setOnClickListener(v -> onClickBuscar());

        botonAtras.setOnClickListener(v -> finish());
    }

    /**
     * Maneja el evento de clic en el botón de búsqueda.
     * Valida el campo de texto de búsqueda y realiza una consulta a la API OMDB
     */
    private void onClickBuscar() {
        String titulo = editTextBuscar.getText().toString().trim();
        if (titulo.isEmpty()) {
            // Mostrar un mensaje de error si el campo está vacío
            Toast.makeText(this, getString(R.string.error_titulo), Toast.LENGTH_SHORT).show();
            return;
        }
        // Consultar la API OMDB con el título ingresado
        buscarPeliculasOMDB(titulo);
    }

    /**
     * Realiza una búsqueda de películas en la API OMDB utilizando el título proporcionado.
     * Actualiza la lista de películas y el adaptador del RecyclerView con los resultados.
     *
     * @param titulo El título de la película a buscar.
     */
    public void buscarPeliculasOMDB(String titulo){
        // Limpiar la lista de películas antes de realizar una nueva búsqueda
        listaPeliculas.clear();

        // Realizar la llamada a la API con Retrofit para obtener el listado de películas
        Call<SearchResponseOMDB> call = service.buscarPeliculas(apiKey, "movie", titulo);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<SearchResponseOMDB> call, Response<SearchResponseOMDB> response) {
                buscarPeliculasResponse(response);
            }
            // Si la llamada falla, mostrar un mensaje de error
            @Override
            public void onFailure(Call<SearchResponseOMDB> call, Throwable t) {
                Toast.makeText(BuscarPeliculaActivity.this,
                        getString(R.string.error_conexion) + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Maneja la respuesta de la búsqueda de películas.
     * Actualiza la lista de películas y el adaptador del RecyclerView si la búsqueda fue exitosa.
     * Si no se encontraron resultados, muestra un mensaje al usuario.
     *
     * @param response La respuesta de la API que contiene los resultados de la búsqueda.
     */
    private void buscarPeliculasResponse(Response<SearchResponseOMDB> response) {
        if (response.isSuccessful() && response.body() != null) {
            SearchResponseOMDB searchResponse = response.body();
            if (searchResponse.getSearchResults() != null && !searchResponse.getSearchResults().isEmpty()) {
                listaPeliculas.addAll(searchResponse.getSearchResults());
                // Configurar el adaptador con la lista de películas
                if (peliculaAdapter == null) {
                    peliculaAdapter = new PeliculasAdapter(listaPeliculas, pelicula -> {
                        // Si se hace clic en una película, llamada a la API para obtener detalles
                        obtenerDetallesPelicula(pelicula.getImdbID());
                    });
                    recyclerViewResultados.setAdapter(peliculaAdapter);
                } else {
                    peliculaAdapter.notifyDataSetChanged();
                }

                Toast.makeText(BuscarPeliculaActivity.this,
                        getString(R.string.resultados_encontrados, listaPeliculas.size()), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(BuscarPeliculaActivity.this, getString(R.string.sin_resultados), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BuscarPeliculaActivity.this, getString(R.string.error_busqueda), Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Llamada a la API de OMDB para obtener los detalles de una pelicula por su imdbID.
     *
     * @param imdbID El ID de IMDb de la película a obtener.
     */
    public void obtenerDetallesPelicula(String imdbID) {
        // Realizar la llamada a la API
        Call<PeliculaOMDB> call = service.obtenerDetallePelicula(apiKey, imdbID);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PeliculaOMDB> call, Response<PeliculaOMDB> response) {
                obtenerDetallesResponse(response);
            }
            // Si la llamada falla, mostrar un mensaje de error
            @Override
            public void onFailure(Call<PeliculaOMDB> call, Throwable t) {
                Toast.makeText(BuscarPeliculaActivity.this, getString(R.string.error_conexion) + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Maneja la respuesta de la API al obtener los detalles de una película.
     * Si la respuesta es exitosa, inicia la actividad del Formulario con los datos de la película.
     * Si hay un error, muestra un mensaje al usuario.
     *
     * @param response La respuesta de la API que contiene los detalles de la película.
     */
    private void obtenerDetallesResponse(Response<PeliculaOMDB> response) {
        if (response.isSuccessful() && response.body() != null) {
            PeliculaOMDB pelicula = response.body();

            // Obtener los datos de la película y pasarlos a la actividad del Formulario
            Intent intent = new Intent(BuscarPeliculaActivity.this, FormularioActivity.class);
            intent.putExtra("titulo", pelicula.getTitulo());
            intent.putExtra("anio", pelicula.getAnio());
            intent.putExtra("actor", pelicula.getActorPrincipal());
            startActivity(intent);

        } else {
            Toast.makeText(BuscarPeliculaActivity.this, getString(R.string.error_detalle), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Configura el cliente Retrofit para realizar las llamadas a la API OMDB.
     */
    private void configureServer() {
        // Configurar Retrofit con la url de la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Crear la instancia del servicio
        service = retrofit.create(ServiceOMDB.class);
    }

    /**
     * Inicializa los widgets de la actividad.
     * Configura el RecyclerView y la lista de películas.
     */
    private void initializeWidgets() {
        editTextBuscar = findViewById(R.id.editTextBuscar);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        botonAtras = findViewById(R.id.buttonAtras);

        recyclerViewResultados = findViewById(R.id.recyclerViewPeliculas);

        // Inicializar la lista de resultados
        listaPeliculas = new ArrayList<>();

        // Configurar el RecyclerView con un layout vertical y un adaptador
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));
    }

}