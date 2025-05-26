package com.example.trabajofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonAnadir, buttonListar, buttonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        buttonAnadir = findViewById(R.id.buttonAnadir);
        buttonListar = findViewById(R.id.buttonListar);
        buttonSalir = findViewById(R.id.buttonSalir);

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
            finish();
        });

    }
}