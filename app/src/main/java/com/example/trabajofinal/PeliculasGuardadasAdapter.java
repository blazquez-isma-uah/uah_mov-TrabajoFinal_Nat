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

/**
 * Adaptador para mostrar una lista de películas guardadas en un RecyclerView.
 */
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

        String titulo = contexto.getString(R.string.titulo_pelicula) +
                ": " + peliculaGuardada.titulo;

        // anio, actorPrincipal, fecha y ciudad
        String detalles = contexto.getString(R.string.anio) + ": " + peliculaGuardada.anio + "\n" +
                contexto.getString(R.string.actor_principal) + ": " + peliculaGuardada.actorPrincipal + "\n" +
                contexto.getString(R.string.fecha_visionado) + ": " + peliculaGuardada.fecha + "\n" +
                contexto.getString(R.string.ciudad_visionado) + ": " + peliculaGuardada.ciudad;

        holder.textTitulo.setText(titulo);
        holder.textDetalles.setText(detalles);

        // Al hacer long click, preguntar si se quiere borrar
        holder.itemView.setOnLongClickListener(v -> onLongClickItem(position, peliculaGuardada));
    }

    /**
     * Maneja el evento de long click en un item del RecyclerView.
     * Muestra un diálogo de confirmación para eliminar la película guardada.
     *
     * @param position posición del item en la lista
     * @param peliculaGuardada película guardada que se desea eliminar
     * @return
     */
    private boolean onLongClickItem(int position, PeliculaGuardada peliculaGuardada) {
        new AlertDialog.Builder(contexto)
                .setTitle(contexto.getString(R.string.titulo_alerta_eliminar))
                .setMessage(contexto.getString(R.string.mensaje_alerta_eliminar))
                .setPositiveButton(contexto.getString(R.string.aceptar), (dialog, which) -> {
                    // Eliminar la película de la base de datos y actualizar la lista
                    AppDatabase db = AppDatabase.getInstance(contexto);
                    db.peliculaDao().eliminarPorId(peliculaGuardada.id);
                    lista.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(contexto, contexto.getString(R.string.pelicula_eliminada), Toast.LENGTH_SHORT).show();
                    listener.onItemDeleted();
                })
                .setNegativeButton(contexto.getString(R.string.cancelar), null)
                .show();
        return true;
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