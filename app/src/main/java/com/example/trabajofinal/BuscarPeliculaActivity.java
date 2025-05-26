package com.example.trabajofinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuscarPeliculaActivity extends AppCompatActivity {

    private EditText editTextBuscar;
    private Button buttonBuscar;
    private RecyclerView recyclerViewResultados;

    private List<Pelicula> listaPeliculas;
    private PeliculasAdapter peliculaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_pelicula);

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
                Toast.makeText(this, "Por favor, introduce un título", Toast.LENGTH_SHORT).show();
                return;
            }
            // Simular una búsqueda de películas (despues se hará una consulta a una API o base de datos)
            simularResultados(titulo);
        });

    }

    /**
     * Simula resultados para probar la interfaz.
     * Más adelante se sustituirá por una llamada a la API real.
     */
    private void simularResultados(String titulo) {
        listaPeliculas.clear(); // Limpiamos resultados anteriores

        // Generamos películas simuladas
        for (int i = 1; i <= 5; i++) {
            Pelicula pelicula = new Pelicula(titulo + " " + i, "20" + (10 + i));
            listaPeliculas.add(pelicula);
        }

        // Si es la primera vez que se carga el adapter, se crea
        if (peliculaAdapter == null) {
            peliculaAdapter = new PeliculasAdapter(listaPeliculas);
            recyclerViewResultados.setAdapter(peliculaAdapter);
        } else {
            // Si ya existe, solo se actualiza la lista
            peliculaAdapter.notifyDataSetChanged();
        }
    }

}