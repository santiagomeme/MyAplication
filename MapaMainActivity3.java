package com.security.myapplication;
import static com.security.myapplication.MainActivity.NOTIFICACION_ID;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import androidx.core.app.NotificationManagerCompat;
import android.net.Uri;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

public class MapaMainActivity3 extends AppCompatActivity {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private boolean isLocationPermissionGranted = false;
    private LocationManager locationManager;
    private LocationListener locationListener;


    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_main3);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(NOTIFICACION_ID);


        abrirUbicacionEnMapa(latitude, longitude);


        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                // Llamar a la función para abrir la ubicación en el mapa aquí
                abrirUbicacionEnMapa(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            isLocationPermissionGranted = true;
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            isLocationPermissionGranted = true;
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de ubicación concedido, iniciar actualizaciones de ubicación
                isLocationPermissionGranted = true;
                startLocationUpdates();
            } else {
                // Permiso de ubicación denegado, puedes mostrar un mensaje o tomar alguna otra acción
                // por ejemplo, mostrar un mensaje al usuario indicando que la funcionalidad no estará disponible
            }
        }
    }


    private void startLocationUpdates() {
        if (isLocationPermissionGranted) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Iniciar actualizaciones de ubicación utilizando GPS_PROVIDER
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000, // Intervalo en milisegundos
                        0, // Distancia mínima en metros
                        locationListener
                );
            }
        }
    }


    public void onLocationChanged(Location location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            // Llamar a la función para abrir la ubicación en el mapa aquí
            abrirUbicacionEnMapa(latitude, longitude);
        }
    }


    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }


    private void abrirUbicacionEnMapa(double latitud, double longitud) {
        Uri locationUri = Uri.parse("geo:" + latitud + "," + longitud);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
        startActivity(mapIntent);
    }

    public void mapaAtras(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}