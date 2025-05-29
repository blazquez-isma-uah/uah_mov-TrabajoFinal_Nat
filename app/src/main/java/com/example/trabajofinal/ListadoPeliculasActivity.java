package com.example.trabajofinal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajofinal.sqlite.AppDatabase;
import com.example.trabajofinal.sqlite.PeliculaGuardada;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ListadoPeliculasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button buttonEliminarTodas;

    Spinner spinnerOrdener;
    Button buttonOrdenar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_peliculas);

        recyclerView = findViewById(R.id.recyclerViewPeliculas);
        buttonEliminarTodas = findViewById(R.id.buttonEliminarTodas);
        spinnerOrdener = findViewById(R.id.spinnerOrdenar);
        buttonOrdenar = findViewById(R.id.buttonOrdenar);

        // Adaptador de spinner con opciones de ordenación
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.campos_ordenacion, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdener.setAdapter(adapterSpinner);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarPeliculas("id"); // Cargar películas por ID inicialmente


        buttonOrdenar.setOnClickListener(v -> {
            String campoSeleccionado = spinnerOrdener.getSelectedItem().toString();
            cargarPeliculas(campoSeleccionado);
        });

        buttonEliminarTodas.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.eliminar_todas_titulo))
                .setMessage(getString(R.string.eliminar_todas_mensaje))
                .setPositiveButton(getString(R.string.aceptar), (dialog, which) -> eliminarTodasPeliculas())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
        });

        Button botonAtras = findViewById(R.id.buttonAtras);
        botonAtras.setOnClickListener(v -> finish());

    }

    private void eliminarTodasPeliculas() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.peliculaDao().eliminarTodas();
        // Recargar la lista de películas
        cargarPeliculas("id");

        // Mostrar mensaje de confirmación
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.peliculas_eliminadas))
                .setPositiveButton(getString(R.string.aceptar), null)
                .show();
    }

    private void cargarPeliculas(String ordenCampo) {
        System.out.println("Ordenando por: " + ordenCampo);
        AppDatabase db = AppDatabase.getInstance(this);
        List<PeliculaGuardada> listaPeliculas;

        String titulo = getString(R.string.titulo_pelicula);
        String anio = getString(R.string.anio);
        String actor = getString(R.string.actor_principal);
        String fecha = getString(R.string.fecha_visionado);
        String ciudad = getString(R.string.ciudad_visionado);
        listaPeliculas = db.peliculaDao().obtenerTodas();

        if(ordenCampo.equals(titulo)){
            listaPeliculas.sort(Comparator.comparing(p -> p.titulo));
        }
        else if(ordenCampo.equals(anio)){
            listaPeliculas.sort(Comparator.comparing(p -> p.anio));
        }
        else if(ordenCampo.equals(actor)){
            listaPeliculas.sort(Comparator.comparing(p -> p.actorPrincipal));
        }
        else if(ordenCampo.equals(fecha)){
            // Convertir la fecha de String a Date para ordenar correctamente
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            listaPeliculas.sort((p1, p2) -> {
                try {
                    Date date1 = new Date(Objects.requireNonNull(sdf.parse(p1.fecha)).getTime());
                    Date date2 = new Date(Objects.requireNonNull(sdf.parse(p2.fecha)).getTime());
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    return 0; // Si hay un error, no cambiar el orden
                }
            });
        }
        else if(ordenCampo.equals(ciudad)){
            listaPeliculas.sort(Comparator.comparing(p -> p.ciudad));
        }
        else {
            listaPeliculas.sort(Comparator.comparing(p -> p.id));
        }

        PeliculasGuardadasAdapter adapter = new PeliculasGuardadasAdapter(this, listaPeliculas, () -> {});
        recyclerView.setAdapter(adapter);
    }
}