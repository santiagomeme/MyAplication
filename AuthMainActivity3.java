package com.security.myapplication;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.SignInButton;


public class AuthMainActivity3 extends AppCompatActivity {


    private FirebaseAnalytics analytics;
    private FirebaseAuth firebaseAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private static final String TAG = "AuthMainActivity3";


    public enum ProviderType {
        EMAIL, // Ejemplo: si el usuario se autentica mediante correo electrónico
        GOOGLE, // Ejemplo: si el usuario se autentica mediante Google
        FACEBOOK // Ejemplo: si el usuario se autentica mediante Facebook
    }


    private static final int RC_SIGN_IN = 123;
    private View authLayout;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main3);



        analytics = FirebaseAnalytics.getInstance(this);

        // Inicializa Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();


        Bundle bundle = new Bundle();
        bundle.putString("message", "Integración de Firebase completa");
        analytics.logEvent("initScreen", bundle);


        SignInButton googleSignInButton = findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        // En el método onCreate() o donde sea apropiado, inicializa y configura el cliente de inicio de sesión de Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id)) // Reemplaza con tu client ID
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Método para iniciar sesión con Google


        // Configura la interfaz de usuario
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.Acceder);
        authLayout = findViewById(R.id.authLayout);


        Button registrarseButton = findViewById(R.id.RegistrarseButton);
        Button accederButton = findViewById(R.id.Acceder);

        registrarseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para el botón "Registrarse"
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    // Aquí maneja el registro de usuarios
                    firebaseAuth.createUserWithEmailAndPassword(
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString()
                    ).addOnCompleteListener(AuthMainActivity3.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                showHome(user.getEmail(), ProviderType.EMAIL);
                            } else {
                                // Muestra un mensaje de error si ocurre un problema al registrar
                                AlertDialog.Builder builder = new AlertDialog.Builder(AuthMainActivity3.this);
                                builder.setTitle("Error al registrar usuario");
                                builder.setMessage("No se pudo registrar el usuario. Por favor, inténtelo de nuevo.");
                                builder.setPositiveButton("Aceptar", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                } else {
                    // Muestra un mensaje de error si faltan datos
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthMainActivity3.this);
                    builder.setTitle("Campos incompletos");
                    builder.setMessage("Por favor, complete todos los campos antes de registrar.");
                    builder.setPositiveButton("Aceptar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        accederButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para el botón "Acceder"
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    // Aquí maneja el inicio de sesión de usuarios
                    firebaseAuth.signInWithEmailAndPassword(
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString()
                    ).addOnCompleteListener(AuthMainActivity3.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                showHome(user.getEmail(), ProviderType.EMAIL);
                            } else {
                                // Muestra un mensaje de error si ocurre un problema al iniciar sesión
                                AlertDialog.Builder builder = new AlertDialog.Builder(AuthMainActivity3.this);
                                builder.setTitle("Error al iniciar sesión");
                                builder.setMessage("No se pudo iniciar sesión. Por favor, compruebe sus credenciales.");
                                builder.setPositiveButton("Aceptar", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                } else {
                    // Muestra un mensaje de error si faltan datos
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthMainActivity3.this);
                    builder.setTitle("Campos incompletos");
                    builder.setMessage("Por favor, complete todos los campos antes de acceder.");
                    builder.setPositiveButton("Aceptar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        Button authAtrasButton = findViewById(R.id.atrasAuth);

        authAtrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí coloca el código para ir a la actividad anterior (por ejemplo, MainActivity)
                Intent intent = new Intent(AuthMainActivity3.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });





    }

//registro con google

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In fue exitoso, ahora puedes autenticar en Firebase con las credenciales de Google
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign-In falló, maneja el error aquí
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Autenticación exitosa
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // Puedes redirigir al usuario a la actividad principal o realizar otras acciones
                            // como guardar la información del usuario en tu base de datos.
                            showHome(user.getEmail(), ProviderType.GOOGLE);
                        } else {
                            // La autenticación falló
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }



    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(String email, ProviderType proveedor) {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("proveedor", proveedor.toString()); // Convierte el enum a String
        editor.apply();

        Intent intent = new Intent(this, HomeMainActivity3.class);
        intent.putExtra("email", email);
        intent.putExtra("proveedor", proveedor.toString());
        startActivity(intent);
        finish();
    }



    protected void onStart(){
        super.onStart();
        authLayout.setVisibility(View.VISIBLE);
    }

    private void session() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String provider = prefs.getString("provider", null);

        if (email != null && provider != null) {
            authLayout.setVisibility(View.INVISIBLE);
            showHome(email, ProviderType.valueOf(provider));
        }
    }



}
