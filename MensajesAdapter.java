package com.security.myapplication;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.MensajeViewHolder> {

    private List<MensajesMainActivity3.Mensaje> listaMensajes;

    public MensajesAdapter(List<MensajesMainActivity3.Mensaje> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        MensajesMainActivity3.Mensaje mensaje = listaMensajes.get(position);
        holder.bindMensaje(mensaje);
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MensajeViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewContenido;
        private TextView textViewRemitente;

        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContenido = itemView.findViewById(R.id.textViewContenido);
            textViewRemitente = itemView.findViewById(R.id.textViewRemitente);
        }

        public void bindMensaje(MensajesMainActivity3.Mensaje mensaje) {
            textViewContenido.setText(mensaje.getContenido());
            textViewRemitente.setText(mensaje.getRemitente());
        }
    }
}
