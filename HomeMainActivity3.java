package com.security.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;

import android.content.SharedPreferences;
import android.content.Context;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;



public class HomeMainActivity3 extends AppCompatActivity {

    public enum ProviderType {
        BASIC,
        GOOGLE,
        FACEBOOK
    }

    private TextView emailTextView;
    private TextView proveedorTextView;
    private Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main3);



        emailTextView = findViewById(R.id.emailTextView);
        proveedorTextView = findViewById(R.id.proveedorTextView);
        logOutButton = findViewById(R.id.CerrarSessionButton); // Agregar esta línea




//setup
        Bundle bundle = getIntent().getExtras();
        String email = bundle != null ? bundle.getString("email") : null;
        String proveedor = bundle != null ? bundle.getString("proveedor") : null;
        setup(email != null ? email : "", proveedor != null ? proveedor : "");
//guardar datosi}
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", emailTextView.getText().toString());
        editor.putString("proveedor", proveedor);
        editor.apply();

    }
    private void setup(String email, String proveedor) {
        setTitle("inicio");
        emailTextView.setText(email);
        proveedorTextView.setText(proveedor);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });
    }


}