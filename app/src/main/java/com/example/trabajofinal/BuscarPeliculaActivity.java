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
    private Button buttonBuscar;
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

        // Configurar Retrofit con la url de la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Crear la instancia del servicio
        service = retrofit.create(ServiceOMDB.class);

        editTextBuscar = findViewById(R.id.editTextBuscar);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        recyclerViewResultados = findViewById(R.id.recyclerViewPeliculas);

        // Inicializar la lista de resultados
        listaPeliculas = new ArrayList<>();

        // Configurar el RecyclerView con un layout vertical y un adaptador
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));


        // Botón buscar
        buttonBuscar.setOnClickListener(v -> {
            String titulo = editTextBuscar.getText().toString().trim();
            if (titulo.isEmpty()) {
                // Mostrar un mensaje de error si el campo está vacío
                Toast.makeText(this, R.string.error_titulo, Toast.LENGTH_SHORT).show();
                return;
            }
            // Simular una búsqueda de películas (despues se hará una consulta a una API o base de datos)
            buscarPeliculasOMDB(titulo);
        });

        Button botonAtras = findViewById(R.id.buttonAtras);
        botonAtras.setOnClickListener(v -> finish());
    }

    public void buscarPeliculasOMDB(String titulo){
        listaPeliculas.clear();

        Call<SearchResponseOMDB> call = service.buscarPeliculas(apiKey, "movie", titulo);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<SearchResponseOMDB> call, Response<SearchResponseOMDB> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchResponseOMDB searchResponse = response.body();
                    if (searchResponse.getSearchResults() != null && !searchResponse.getSearchResults().isEmpty()) {
                        listaPeliculas.addAll(searchResponse.getSearchResults());
                        // Configurar el adaptador con la lista de películas
                        if (peliculaAdapter == null) {
                            peliculaAdapter = new PeliculasAdapter(listaPeliculas, pelicula -> {
                                // Llamada a la API para obtener los detalles de la película
                                obtenerDetallesPelicula(pelicula.getImdbID());
                            });
                            recyclerViewResultados.setAdapter(peliculaAdapter);
                        } else {
                            peliculaAdapter.notifyDataSetChanged();
                        }

                        Toast.makeText(BuscarPeliculaActivity.this,
                                getString(R.string.resultados_encontrados, listaPeliculas.size()), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(BuscarPeliculaActivity.this, R.string.sin_resultados, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BuscarPeliculaActivity.this, R.string.error_busqueda, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SearchResponseOMDB> call, Throwable t) {
                Toast.makeText(BuscarPeliculaActivity.this,
                        R.string.error_conexion + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Llamada a la API de OMDB para obtener los detalles de una pelicula por su imdbID.
     */
    public void obtenerDetallesPelicula(String imdbID) {
        Call<PeliculaOMDB> call = service.obtenerDetallePelicula(apiKey, imdbID);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PeliculaOMDB> call, Response<PeliculaOMDB> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PeliculaOMDB pelicula = response.body();

                    // Obtener los datos de la película y pasarlos a la actividad del Formulario
                    Intent intent = new Intent(BuscarPeliculaActivity.this, FormularioActivity.class);
                    intent.putExtra("titulo", pelicula.getTitulo());
                    intent.putExtra("anio", pelicula.getAnio());
                    intent.putExtra("actor", pelicula.getActorPrincipal());
                    startActivity(intent);

                } else {
                    Toast.makeText(BuscarPeliculaActivity.this, R.string.error_detalle, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PeliculaOMDB> call, Throwable t) {
                Toast.makeText(BuscarPeliculaActivity.this, R.string.error_conexion + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}