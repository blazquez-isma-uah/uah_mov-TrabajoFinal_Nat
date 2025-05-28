package com.example.trabajofinal;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajofinal.sqlite.AppDatabase;
import com.example.trabajofinal.sqlite.PeliculaGuardada;

import java.util.List;

// Adapter para manejar la lista de películas guardadas
public class PeliculasGuardadasAdapter extends RecyclerView.Adapter<PeliculasGuardadasAdapter.PeliculaViewHolder> {

    private List<PeliculaGuardada> lista;
    private Context contexto;
    private OnItemDeletedListener listener;

    public interface OnItemDeletedListener {
        void onItemDeleted();
    }

    public PeliculasGuardadasAdapter(Context contexto, List<PeliculaGuardada> lista, OnItemDeletedListener listener) {
        this.contexto = contexto;
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(contexto).inflate(R.layout.item_pelicula_guardada, parent, false);
        return new PeliculaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        PeliculaGuardada peliculaGuardada = lista.get(position);

        String titulo = "Título: " + peliculaGuardada.titulo;
        String detalles = "Año: " + peliculaGuardada.anio + "\nActor: " + peliculaGuardada.actorPrincipal +
                "\nFecha: " + peliculaGuardada.fecha + "\nCiudad: " + peliculaGuardada.ciudad;

        holder.textTitulo.setText(titulo);
        holder.textDetalles.setText(detalles);

        // Al hacer long click, preguntar si se quiere borrar
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(contexto)
                    .setTitle("Eliminar película")
                    .setMessage("¿Estás seguro de eliminar esta película?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        AppDatabase db = AppDatabase.getInstance(contexto);
                        db.peliculaDao().eliminarPorId(peliculaGuardada.id);
                        lista.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(contexto, "Película eliminada", Toast.LENGTH_SHORT).show();
                        listener.onItemDeleted();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textDetalles;

        public PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textDetalles = itemView.findViewById(R.id.textDetalles);
        }
    }
}