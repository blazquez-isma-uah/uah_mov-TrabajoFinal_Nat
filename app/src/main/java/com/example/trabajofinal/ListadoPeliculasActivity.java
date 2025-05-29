package com.example.trabajofinal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajofinal.sqlite.AppDatabase;
import com.example.trabajofinal.sqlite.PeliculaGuardada;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ListadoPeliculasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button buttonEliminarTodas;

    Spinner spinnerOrdener;
    Button buttonOrdenar;
    TextView textOrden;

    SwitchCompat switchOrden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_peliculas);

        recyclerView = findViewById(R.id.recyclerViewPeliculas);
        buttonEliminarTodas = findViewById(R.id.buttonEliminarTodas);
        spinnerOrdener = findViewById(R.id.spinnerOrdenar);
        buttonOrdenar = findViewById(R.id.buttonOrdenar);
        textOrden = findViewById(R.id.textOrden);
        switchOrden = findViewById(R.id.switchOrden);

        // Adaptador de spinner con opciones de ordenación
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.campos_ordenacion, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdener.setAdapter(adapterSpinner);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarPeliculas("id", false); // Cargar películas por defecto ordenadas por ID ascendente

        // Actualizar el texto al cambiar el switch
        switchOrden.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textOrden.setText(getString(R.string.orden_descendente));
            } else {
                textOrden.setText(getString(R.string.orden_ascendente));
            }
        });

        buttonOrdenar.setOnClickListener(v -> {
            String campoSeleccionado = spinnerOrdener.getSelectedItem().toString();
            boolean descendente = switchOrden.isChecked();
            cargarPeliculas(campoSeleccionado, descendente);
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
        cargarPeliculas("id", false);

        // Mostrar mensaje de confirmación
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.peliculas_eliminadas))
                .setPositiveButton(getString(R.string.aceptar), null)
                .show();
    }

    private void cargarPeliculas(String ordenCampo, boolean descendente) {
        AppDatabase db = AppDatabase.getInstance(this);
        List<PeliculaGuardada> listaPeliculas;

        String titulo = getString(R.string.titulo_pelicula);
        String anio = getString(R.string.anio);
        String actor = getString(R.string.actor_principal);
        String fecha = getString(R.string.fecha_visionado);
        String ciudad = getString(R.string.ciudad_visionado);
        listaPeliculas = db.peliculaDao().obtenerTodas();

        Comparator<PeliculaGuardada> comparador = null;

        if(ordenCampo.equals(titulo)){
            comparador = Comparator.comparing(p -> p.titulo.toLowerCase());
        }
        else if(ordenCampo.equals(anio)){
            comparador = Comparator.comparing(p -> p.anio);
        }
        else if(ordenCampo.equals(actor)){
            comparador = Comparator.comparing(p -> p.actorPrincipal.toLowerCase());
        }
        else if(ordenCampo.equals(fecha)){
            comparador = Comparator.comparing(p -> {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return sdf.parse(p.fecha);
                } catch (Exception e) {
                    return null; // Si hay un error, no cambiar el orden
                }
            });
        }
        else if(ordenCampo.equals(ciudad)){
            comparador = Comparator.comparing(p -> p.ciudad.toLowerCase());
        }
        else {
            comparador = Comparator.comparing(p -> p.id);
        }

        if (descendente) {
            comparador = comparador.reversed();
        }

        listaPeliculas.sort(comparador);

        PeliculasGuardadasAdapter adapter = new PeliculasGuardadasAdapter(this, listaPeliculas, () -> {});
        recyclerView.setAdapter(adapter);
    }
}