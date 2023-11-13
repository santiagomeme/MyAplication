package com.security.myapplication;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.pm.PackageManager;
import android.content.Context;
import android.widget.Toast;


import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.animation.ArgbEvaluatorCompat;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_VIDEO_CAPTURE = 2;

    private Bitmap imageBitmap;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText editText;
    private Button btnCapturarFoto;
    private Button btnAlert;
    private PendingIntent pendingIntent;

    private PendingIntent siPendingIntent;
    private PendingIntent noPendingIntent;
    private static final String CHANNEL_ID = "CANAL";
    final static int NOTIFICACION_ID = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private LocationManager locationManager;
    Button btnSiguiente;
  Button irAAuthMainActivity3;
    private FirebaseAnalytics mFirebaseAnalytics;

    // Nuevo mÃ©todo para manejar el clic del botÃ³n de grabar video
    @Override 
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        irAAuthMainActivity3 = findViewById(R.id.irAAuthMainActivity3);

        // Configurar el listener del botón si es necesario
        irAAuthMainActivity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código para manejar el clic en el botón irAAuthMainActivity3
                Intent intent = new Intent(MainActivity.this, AuthMainActivity3.class);
                startActivity(intent);
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Crea un Bundle para enviar datos al evento
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integracion de Firebase completa");

        // Registra un evento personalizado en Firebase Analytics
        mFirebaseAnalytics.logEvent("InitScreen", bundle);
        btnCapturarFoto = findViewById(R.id.btnCapturarFoto);
        btnCapturarFoto.setOnClickListener(this);
        editText = findViewById(R.id.editText);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(this);
        btnCapturarFoto = findViewById(R.id.btnCapturarFoto);
        Button btnGrabarVideo = findViewById(R.id.btnGrabarVideo);
        btnGrabarVideo.setOnClickListener(this);
        btnAlert = findViewById(R.id.btnAlert);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPendingIntent();
                setSiPendingIntent();
                setNoPendingIntent();
                createNotification();
                createNotificationChannel();
                getLocation();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }



        // Verifica si la aplicaciÃ³n se estÃ¡ ejecutando en un dispositivo con cÃ¡mara frontal.
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)) {
            // ContinÃºa con el flujo de trabajo de tu aplicaciÃ³n que requiere una cÃ¡mara frontal.
            btnCapturarFoto.setVisibility(View.VISIBLE); // Hace visible el botÃ³n de capturar foto
        } else {
            // Realiza una degradaciÃ³n grÃ¡cil de la experiencia de tu aplicaciÃ³n.
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int numberOfCameras = Camera.getNumberOfCameras();
            int cameraId = -1;

            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i;
                    break;
                }
            }

            if (cameraId != -1) {
                // Habilita el uso de la cÃ¡mara trasera

                btnCapturarFoto.setVisibility(View.VISIBLE); // Hace visible el botÃ³n de capturar foto
            } else {
                // No hay cÃ¡mara disponible en el dispositivo
                btnCapturarFoto.setVisibility(View.GONE); // Oculta el botÃ³n de capturar foto
            }
        }

        //boton de alertas

    }



    public void irAAuthMainActivity3(View view) {
        // Código para iniciar AuthMainActivity3
        Intent intent = new Intent(this, AuthMainActivity3.class);
        startActivity(intent);
    }
    public void btnGrabarVideo(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }




    // MÃ©todo para obtener y enviar la ubicaciÃ³n
    private void enviarUbicacion() {
        // Verificar si se tiene el permiso de ubicaciÃ³n
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Si se tiene el permiso, obtener y enviar la ubicaciÃ³n
            obtenerUbicacionYEnviarTexto();
        } else {
            // Si no se tiene el permiso, solicitarlo al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // MÃ©todo para obtener la ubicaciÃ³n actual del dispositivo (Este es solo un ejemplo, la implementaciÃ³n real puede variar segÃºn las necesidades)
    private Location getLocation() {
        // Implementar lÃ³gica para obtener la ubicaciÃ³n actual del dispositivo aquÃ­
        // Puedes usar la API de ubicaciÃ³n del sistema o algÃºn servicio de ubicaciÃ³n, como GPS o red
        // En este ejemplo, simplemente devolvemos una ubicaciÃ³n ficticia
        Location location = new Location("Fictitious Provider");
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);
        return location;
    }

    // MÃ©todo para obtener y enviar la ubicaciÃ³n por texto
    private void obtenerUbicacionYEnviarTexto() {
        // Obtener la ubicaciÃ³n actual del dispositivo
        Location currentLocation = getLocation();

        if (currentLocation != null) {
            // Obtener la latitud y longitud
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();

            // Crear un Intent para iniciar la actividad de enviar ubicaciÃ³n por texto
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:")); // Este URI indica que serÃ¡ un SMS
            intent.putExtra("sms_body", "Mi ubicaciÃ³n actual es: " + latitude + ", " + longitude);
            startActivity(intent);
        } else {
            // No se pudo obtener la ubicaciÃ³n actual
            Toast.makeText(this, "No se pudo obtener la ubicaciÃ³n actual", Toast.LENGTH_SHORT).show();
        }
    }

    // MÃ©todo para manejar la respuesta de la solicitud de permiso de ubicaciÃ³n
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Agrega esta lÃ­nea
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de ubicaciÃ³n concedido, obtener y enviar la ubicaciÃ³n
                enviarUbicacion();
                getLocation();
            } else {
                // Permiso de ubicaciÃ³n denegado, mostrar un mensaje o realizar alguna acciÃ³n
                Toast.makeText(this, "Permiso de ubicaciÃ³n denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Permiso de ubicaciÃ³n conced



    public void VerMapa(View view) {
        // AquÃ­ implemento el cÃ³digo para el boton de abrir la actividad MapaMainActivity4
        Intent intent = new Intent(this, MapaMainActivity3.class);
        startActivity(intent);
    }

    public void irAActividad2(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }



    private void setPendingIntent() {
        Intent intent = new Intent(this, MainActivity2.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity2.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private void setSiPendingIntent(){
        Intent intent = new Intent(this, MainActivity2.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity2.class);
        stackBuilder.addNextIntent(intent);
        siPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private void setNoPendingIntent(){
        Intent intent = new Intent(this, VideoMainActivity3.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(VideoMainActivity3.class);
        stackBuilder.addNextIntent(intent);
        noPendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }





    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NOTIFICACION";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @SuppressLint("MissingPermission")
    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.baseline_add_alert_24);
        builder.setContentTitle("Alerta de Robo");
        builder.setContentText("Hay un Robo en este Momento");
        builder.setColor(Color.RED);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setLights(Color.RED, 5000, 5000);
        builder.setVibrate(new long[]{2000, 2000, 2000, 2000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.baseline_add_alert_24, "Ver mensajes", siPendingIntent);
        builder.addAction(R.drawable.baseline_add_alert_24, "Ver Fotos", noPendingIntent);




        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCapturarFoto) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        } else if (v.getId() == R.id.btnSiguiente) {
            String texto = editText.getText().toString(); // ObtÃ©n el texto del EditText
            Intent intent = new Intent(MainActivity.this, MensajesMainActivity3.class);
            intent.putExtra("texto", texto); // Pasa el texto como un extra con el intent
            intent.putExtra("imagen", imageBitmap); // Pasa la imagen capturada como un extra con el intent
            startActivity(intent);
        }
        else if (v.getId() == R.id.btnGrabarVideo) {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();

            // Lanzar la actividad para mostrar el video
            Intent intent = new Intent(MainActivity.this, VideoMainActivity3 .class);
            intent.putExtra("videoUri", videoUri.toString());
            startActivity(intent);
        }


        // Abre MainActivity2 y pasa la imagen capturada como un extra
        String texto = editText.getText().toString(); // ObtÃ©n el texto del EditText
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("texto", texto); // Pasa el texto como un extra con el intent
        intent.putExtra("imagen", imageBitmap); // Pasa la imagen capturada como un extra con el intent
        startActivity(intent);



    }


    public void onCallButtonClick(View view) {
        // NÃºmero que deseas llamar
        String phoneNumber = "123"; // Reemplaza esto con el nÃºmero deseado

        // Crear un intent para realizar la llamada
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        // Verificar si la app de llamadas estÃ¡ disponible en el dispositivo
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Iniciar la actividad para realizar la llamada
            startActivity(intent);
        } else {
            // La app de llamadas no estÃ¡ disponible en el dispositivo
            // AquÃ­ puedes manejar esta situaciÃ³n, por ejemplo, mostrar un mensaje de error.
        }
    }



}


// FirebaseApp.initializeApp(this);