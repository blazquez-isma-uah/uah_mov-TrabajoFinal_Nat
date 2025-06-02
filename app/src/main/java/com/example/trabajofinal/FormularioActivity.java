package com.example.trabajofinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;

import com.example.trabajofinal.sqlite.AppDatabase;
import com.example.trabajofinal.sqlite.PeliculaGuardada;

import java.util.Calendar;

public class FormularioActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextAnio, editTextActor, editTextFecha, editTextCiudad;
    private Button buttonGuardar, botonAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario);

        initializeWidgets();
        getIntentData();

        editTextFecha.setOnClickListener(v -> onClickEditTextFecha());

        buttonGuardar.setOnClickListener(v -> onClickGuardar());

        botonAtras.setOnClickListener(v -> finish());

    }

    /**
     * Maneja el evento de clic en el botón Guardar.
     * Valida los campos del formulario y guarda la película en la base de datos.
     */
    private void onClickGuardar() {
        String fecha = editTextFecha.getText().toString();
        String ciudad = editTextCiudad.getText().toString();

        // Validar campos
        if (fecha.isEmpty() || ciudad.isEmpty()) {
            Toast.makeText(this, getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Pelicula con los datos del formulario
        PeliculaGuardada peliculaGuardada = new PeliculaGuardada(
                editTextTitulo.getText().toString(),
                editTextAnio.getText().toString(),
                editTextActor.getText().toString(),
                ciudad,
                fecha
        );

        // Guardar la película en la base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        db.peliculaDao().insertarPelicula(peliculaGuardada);

        Toast.makeText(this, getString(R.string.pelicula_guardada), Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Abre un DatePickerDialog para seleccionar una fecha.
     * Al seleccionar una fecha, se formatea y se muestra en el campo editTextFecha.
     */
    private void onClickEditTextFecha() {
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
    }

    /**
     * Obtiene los datos de la película desde el Intent y los muestra en los campos del formulario.
     */
    private void getIntentData() {
        String titulo = getIntent().getStringExtra("titulo");
        String anio = getIntent().getStringExtra("anio");
        String actor = getIntent().getStringExtra("actor");

        editTextTitulo.setText(titulo);
        editTextAnio.setText(anio);
        editTextActor.setText(actor);
    }

    /**
     * Inicializa los widgets del formulario.
     */
    private void initializeWidgets() {
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextAnio = findViewById(R.id.editTextAnio);
        editTextActor = findViewById(R.id.editTextActor);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        botonAtras = findViewById(R.id.buttonAtras);
    }
}