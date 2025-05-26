package com.example.trabajofinal.omdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajofinal.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Adapter para manejar la lista de películas.
 * Este adaptador se encargará de crear las vistas necesarias para mostrar cada película en la lista.
 */
public class PeliculasAdapter extends RecyclerView.Adapter<PeliculasAdapter.PeliculaViewHolder> {

    private List<PeliculaOMDB> listaPeliculas;

    public PeliculasAdapter(List<PeliculaOMDB> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    @NotNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelicula, parent, false);
        return new PeliculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull PeliculaViewHolder holder, int position) {
        // Obtener la película actual
        PeliculaOMDB pelicula = listaPeliculas.get(position);

        // Asignar los datos de la película a las vistas del ViewHolder
        holder.textViewTitulo.setText(pelicula.getTitulo());
        holder.textViewAnio.setText(pelicula.getAnio());
    }

    @Override
    public int getItemCount() {
        // Devolver el número total de películas en la lista
        return listaPeliculas.size();
    }

    /**
     * ViewHolder para representar cada elemento de la lista de películas.
     * Contiene las vistas que se mostrarán para cada película.
     */
    public static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewAnio;

        public PeliculaViewHolder(@NotNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewAnio = itemView.findViewById(R.id.textViewAnio);
        }
    }
}
