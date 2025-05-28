package com.example.trabajofinal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajofinal.sqlite.AppDatabase;
import com.example.trabajofinal.sqlite.PeliculaGuardada;

import java.util.List;

public class ListadoPeliculasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PeliculasGuardadasAdapter adapter;
    private Button buttonEliminarTodas, buttonVolver;
    private List<PeliculaGuardada> listaPeliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_peliculas);

        recyclerView = findViewById(R.id.recyclerViewPeliculas);
        buttonEliminarTodas = findViewById(R.id.buttonEliminarTodas);
        buttonVolver = findViewById(R.id.buttonVolver);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarPeliculas();

        buttonEliminarTodas.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Eliminar todas las películas")
                .setMessage("¿Estás seguro de que quieres eliminar todas las películas guardadas?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarTodasPeliculas())
                .setNegativeButton("Cancelar", null)
                .show();
        });

        buttonVolver.setOnClickListener(v -> {
            finish(); // Cierra la actividad y vuelve a la anterior
        });
    }

    private void eliminarTodasPeliculas() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.peliculaDao().eliminarTodas();
        // Recargar la lista de películas
        cargarPeliculas();

        // Mostrar mensaje de confirmación
        new AlertDialog.Builder(this)
                .setMessage("Todas las películas han sido eliminadas.")
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void cargarPeliculas() {
        AppDatabase db = AppDatabase.getInstance(this);
        listaPeliculas = db.peliculaDao().obtenerTodas();

        adapter = new PeliculasGuardadasAdapter(this, listaPeliculas, () -> {});
        recyclerView.setAdapter(adapter);
    }
}