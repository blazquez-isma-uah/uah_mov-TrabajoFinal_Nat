package com.example.trabajofinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;

import java.util.Calendar;

public class FormularioActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextAnio, editTextActor, editTextFecha, editTextCiudad;
    private Button buttonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario);

        // Referencias a los widgets
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextAnio = findViewById(R.id.editTextAnio);
        editTextActor = findViewById(R.id.editTextActor);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        // Obtener datos de la pelicula a traves del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String anio = getIntent().getStringExtra("anio");
        String actor = getIntent().getStringExtra("actor");

        // Mostrar datos de los campos no editables
        editTextTitulo.setText(titulo);
        editTextAnio.setText(anio);
        editTextActor.setText(actor);

        // Al hacer clic en el campo Fecha, se abre un selector de calendario
        editTextFecha.setOnClickListener(v -> {
            // Obtiene la fecha actual como punto de partida
            final Calendar calendario = Calendar.getInstance();
            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            // Crea un DatePickerDialog para seleccionar la fecha
            new DatePickerDialog(FormularioActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                // Formatea la fecha seleccionada
                String fechaSeleccionada = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                editTextFecha.setText(fechaSeleccionada);
            }, year, month, day).show();

        });

        // Listener para el botón Guardar
        buttonGuardar.setOnClickListener(v -> {
            String fecha = editTextFecha.getText().toString();
            String ciudad = editTextCiudad.getText().toString();

            // Validar campos
            if (fecha.isEmpty() || ciudad.isEmpty()) {
                // Mostrar mensaje de error
                Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Película guardada", Toast.LENGTH_LONG).show();
            finish();
        });


    }
}