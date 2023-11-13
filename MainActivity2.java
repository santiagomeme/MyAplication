package com.security.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager; // Agrega esta importación

import java.util.ArrayList;
import java.util.List;




import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {
    private List<Foto> listaFotos = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private FotosAdapter fotosAdapter;

    private ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        imagen = findViewById(R.id.imagen);

        // Obtén la imagen enviada desde MainActivity
        Bitmap imagenRecibida = getIntent().getParcelableExtra("imagen");

        // Muestra la imagen en el ImageView
        imagen.setImageBitmap(imagenRecibida);


        //recicler veaw  instancia

        RecyclerView recyclerViewFotos = findViewById(R.id.recyclerViewFotos);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerViewFotos.setLayoutManager(layoutManager);

        fotosAdapter = new FotosAdapter(listaFotos);

        recyclerViewFotos.setAdapter(fotosAdapter);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar si el resultado corresponde a la acción de tomar foto
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Obtener la ruta de la foto (puedes obtenerla desde el Intent o cualquier otra fuente)
            Bitmap imagenCapturada = (Bitmap) data.getExtras().get("data");
            // Agregar la foto a la lista de fotos

            String rutaFoto = null;
            Foto foto = new Foto(rutaFoto);
            listaFotos.add(foto);


            // Notificar al adaptador que se ha agregado una nueva foto
            fotosAdapter.notifyDataSetChanged();
        }
    }

    public class Foto {
        private String rutaFoto;

        public Foto(String rutaFoto) {
            this.rutaFoto = rutaFoto;
        }

        public String getRutaFoto() {
            return rutaFoto;
        }
    }



    public void fotosAtras(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





}