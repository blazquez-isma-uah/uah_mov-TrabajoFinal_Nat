package com.example.trabajofinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonAnadir, buttonListar, buttonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeWidgets();

        buttonAnadir.setOnClickListener(v -> onClickAnadir());

        buttonListar.setOnClickListener(v -> onClickListar());

        buttonSalir.setOnClickListener(v -> onClickSalir());
    }

    /**
     * Maneja el evento de clic en el botón Salir.
     * Muestra un diálogo de confirmación antes de cerrar la aplicación.
     */
    private void onClickSalir() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.confirmar_salida_titulo))
                .setMessage(getString(R.string.confirmar_salida_mensaje))
                .setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                    finishAffinity(); // Cierra completamente la app
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    /**
     * Maneja el evento de clic en el botón Listar.
     * Inicia la actividad ListadoPeliculasActivity para mostrar las películas guardadas.
     */
    private void onClickListar() {
        Intent intent = new Intent(this, ListadoPeliculasActivity.class);
        startActivity(intent);
    }

    /**
     * Maneja el evento de clic en el botón Añadir.
     * Inicia la actividad BuscarPeliculaActivity para buscar y añadir una nueva película.
     */
    private void onClickAnadir() {
        Intent intent = new Intent(this, BuscarPeliculaActivity.class);
        startActivity(intent);
    }

    /**
     * Inicializa los widgets de la actividad.
     */
    private void initializeWidgets() {
        buttonAnadir = findViewById(R.id.buttonAnadir);
        buttonListar = findViewById(R.id.buttonListar);
        buttonSalir = findViewById(R.id.buttonSalir);
    }
}