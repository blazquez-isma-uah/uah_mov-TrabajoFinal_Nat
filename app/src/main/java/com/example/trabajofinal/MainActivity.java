package com.example.trabajofinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button buttonAnadir = findViewById(R.id.buttonAnadir);
        Button buttonListar = findViewById(R.id.buttonListar);
        Button buttonSalir = findViewById(R.id.buttonSalir);

        // Botón para añadir una película, abre la actividad BuscarPeliculaActivity
        buttonAnadir.setOnClickListener(v -> {
            Intent intent = new Intent(this, BuscarPeliculaActivity.class);
            startActivity(intent);
        });

        // Botón para listar las películas, abre la actividad ListadoPeliculasActivity
        buttonListar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListadoPeliculasActivity.class);
            startActivity(intent);
        });

        // Botón para salir de la aplicación
        buttonSalir.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getString(R.string.confirmar_salida_titulo))
                    .setMessage(getString(R.string.confirmar_salida_mensaje))
                    .setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                        finishAffinity(); // Cierra completamente la app
                    })
                    .setNegativeButton(getString(R.string.cancelar), null)
                    .show();
        });

    }
}