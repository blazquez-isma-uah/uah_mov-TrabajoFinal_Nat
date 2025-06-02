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

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class ListadoPeliculasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button buttonEliminarTodas;

    Spinner spinnerOrdener;
    Button buttonOrdenar, botonAtras;
    TextView textOrden;

    SwitchCompat switchOrden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_peliculas);

        initializeWidgets();
        configSpinner();

        // Cargar películas por defecto ordenadas por ID ascendente
        cargarPeliculas("id", false);

        switchOrden.setOnCheckedChangeListener((buttonView, isChecked) -> onCheckedChangeSwitch(isChecked));

        buttonOrdenar.setOnClickListener(v -> onClickOrdenar());

        buttonEliminarTodas.setOnClickListener(v -> onClickEliminarTodas());

        botonAtras.setOnClickListener(v -> finish());
    }

    /**
     * Maneja el evento de clic en el botón Eliminar Todas.
     * Muestra un diálogo de confirmación antes de eliminar todas las películas.
     */
    private void onClickEliminarTodas() {
        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.eliminar_todas_titulo))
            .setMessage(getString(R.string.eliminar_todas_mensaje))
            .setPositiveButton(getString(R.string.aceptar), (dialog, which) -> eliminarTodasPeliculas())
            .setNegativeButton(getString(R.string.cancelar), null)
            .show();
    }

    /**
     * Maneja el evento de clic en el botón Ordenar.
     * Obtiene el campo seleccionado del spinner y el estado del switch para ordenar las películas.
     */
    private void onClickOrdenar() {
        String campoSeleccionado = spinnerOrdener.getSelectedItem().toString();
        boolean descendente = switchOrden.isChecked();
        cargarPeliculas(campoSeleccionado, descendente);
    }

    /**
     * Actualiza el texto del TextView según el estado del switch.
     * Si el switch está activado, muestra "Orden descendente", de lo contrario "Orden ascendente".
     *
     * @param isChecked estado del switch
     */
    private void onCheckedChangeSwitch(boolean isChecked) {
        if (isChecked) {
            textOrden.setText(getString(R.string.orden_descendente));
        } else {
            textOrden.setText(getString(R.string.orden_ascendente));
        }
    }

    /**
     * Configura el spinner con las opciones de ordenación.
     */
    private void configSpinner() {
        // Adaptador de spinner con opciones de ordenación
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.campos_ordenacion, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdener.setAdapter(adapterSpinner);
    }

    /**
     * Inicializa los widgets y RecyclerView de la actividad.
     */
    private void initializeWidgets() {
        recyclerView = findViewById(R.id.recyclerViewPeliculas);
        buttonEliminarTodas = findViewById(R.id.buttonEliminarTodas);
        spinnerOrdener = findViewById(R.id.spinnerOrdenar);
        buttonOrdenar = findViewById(R.id.buttonOrdenar);
        textOrden = findViewById(R.id.textOrden);
        switchOrden = findViewById(R.id.switchOrden);
        botonAtras = findViewById(R.id.buttonAtras);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Elimina todas las películas de la base de datos y recarga la lista.
     */
    private void eliminarTodasPeliculas() {
        AppDatabase db = AppDatabase.getInstance(this);
        db.peliculaDao().eliminarTodas();
        // Recargar la lista de películas
        cargarPeliculas("id", false);

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.peliculas_eliminadas))
                .setPositiveButton(getString(R.string.aceptar), null)
                .show();
    }

    /**
     * Carga las películas desde la base de datos y las ordena según el campo y orden especificados.
     *
     * @param ordenCampo el campo por el que se ordenarán las películas (título, año, actor, fecha, ciudad o ID).
     * @param descendente indica si el orden es descendente (true) o ascendente (false).
     */
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
            // Usar SimpleDateFormat para comparar fechas
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

        // Configurar el adaptador con la lista de películas ordenadas
        PeliculasGuardadasAdapter adapter = new PeliculasGuardadasAdapter(this, listaPeliculas, () -> {});
        recyclerView.setAdapter(adapter);
    }
}