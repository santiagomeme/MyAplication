package com.security.myapplication;




import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import static com.security.myapplication.MainActivity.NOTIFICACION_ID;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MensajesMainActivity3 extends AppCompatActivity {


    private TextView textView2;
    private List<Mensaje> listaMensajes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes_main3);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICACION_ID);

        textView2 = findViewById(R.id.textView2);


        // Obtener el texto enviado desde MainActivity
        String textoRecibido = getIntent().getStringExtra("texto");

        // Mostrar el texto en el TextView
        textView2.setText(textoRecibido);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewMensajes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MensajesAdapter mensajesAdapter = new MensajesAdapter(listaMensajes);
        recyclerView.setAdapter(mensajesAdapter);

        // Recibir el mensaje enviado desde MainActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("mensaje")) {
            String mensajeContenido = intent.getStringExtra("mensaje");
            String remitente = "Usuario"; // Puedes cambiar esto según tu lógica

            Mensaje mensaje = new Mensaje(mensajeContenido, remitente);
            listaMensajes.add(mensaje);
            mensajesAdapter.notifyDataSetChanged(); // Notificar al adaptador que se ha agregado un nuevo mensaje
        }
    }



    public class Mensaje {
        private String contenido;
        private String remitente;

        public Mensaje(String contenido, String remitente) {
            this.contenido = contenido;
            this.remitente = remitente;
        }

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }

        public String getRemitente() {
            return remitente;
        }

        public void setRemitente(String remitente) {
            this.remitente = remitente;
        }
    }



    public void mensajesAtras(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


}