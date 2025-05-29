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
    private Button buttonEliminarTodas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_peliculas);

        recyclerView = findViewById(R.id.recyclerViewPeliculas);
        buttonEliminarTodas = findViewById(R.id.buttonEliminarTodas);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarPeliculas();

        buttonEliminarTodas.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle(R.string.eliminar_todas_titulo)
                .setMessage(R.string.eliminar_todas_mensaje)
                .setPositiveButton(R.string.aceptar, (dialog, which) -> eliminarTodasPeliculas())
                .setNegativeButton(R.string.cancelar, null)
                .show();
        });

        Button botonAtras = findViewById(R.id.buttonAtras);
        botonAtras.setOnClickListener(v -> finish());

    }

    private void eliminarTodasPeliculas() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.peliculaDao().eliminarTodas();
        // Recargar la lista de películas
        cargarPeliculas();

        // Mostrar mensaje de confirmación
        new AlertDialog.Builder(this)
                .setMessage(R.string.peliculas_eliminadas)
                .setPositiveButton(R.string.aceptar, null)
                .show();
    }

    private void cargarPeliculas() {
        AppDatabase db = AppDatabase.getInstance(this);
        List<PeliculaGuardada> listaPeliculas = db.peliculaDao().obtenerTodas();

        PeliculasGuardadasAdapter adapter = new PeliculasGuardadasAdapter(this, listaPeliculas, () -> {
        });
        recyclerView.setAdapter(adapter);
    }
}