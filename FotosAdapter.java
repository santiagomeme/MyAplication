package com.security.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FotosAdapter extends RecyclerView.Adapter<FotosAdapter.FotoViewHolder> {

    private List<MainActivity2.Foto> listaFotos;

    public FotosAdapter(List<MainActivity2.Foto> listaFotos) {
        this.listaFotos = listaFotos;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        MainActivity2.Foto foto = listaFotos.get(position);
        // Aquí puedes cargar la foto en el ImageView
        // Por ejemplo:
        // holder.imageView.setImageBitmap(foto.getBitmap());
    }

    @Override
    public int getItemCount() {
        return listaFotos.size();
    }

    public class FotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewFoto);
        }
    }
}
