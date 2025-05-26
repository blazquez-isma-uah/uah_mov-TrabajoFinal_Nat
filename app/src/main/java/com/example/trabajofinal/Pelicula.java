package com.example.trabajofinal;

public class Pelicula {
    private String titulo;
    private String anio;

    public Pelicula() {
        // Constructor vacío
    }

    public Pelicula(String titulo, String anio) {
        this.titulo = titulo;
        this.anio = anio;
    }
    public String getTitulo() {
        return titulo;
    }

    public String getAnio() {
        return anio;
    }
}
