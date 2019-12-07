package com.lgastelu.petapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lgastelu.petapp.models.Usuario;
import com.lgastelu.petapp.service.ApiService;
import com.lgastelu.petapp.service.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUsuarioActivity extends AppCompatActivity {

    private static final String TAG = RegisterUsuarioActivity.class.getSimpleName();

    private EditText txt_nombres,txt_correo, txt_contraseña, txt_re_contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_usuario);

        txt_nombres=findViewById(R.id.txt_nombres);
        txt_correo=findViewById(R.id.txt_correo);
        txt_contraseña=findViewById(R.id.txt_contraseña);
        txt_re_contraseña=findViewById(R.id.txt_re_contraseña);

    }
    public void callRegister (View view){

        String nombre = txt_nombres.getText().toString();
        String correo = txt_correo.getText().toString();
        String contraseña = txt_contraseña.getText().toString();
        String re_contraseña = txt_re_contraseña.getText().toString();

        if (correo.isEmpty() || contraseña.isEmpty() || re_contraseña.isEmpty()) {
            Toast.makeText(this, "correo y contraseña son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(re_contraseña)){
            Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String user_id = String.valueOf(sp.getLong("user_id", 1));

        ApiService service = ApiServiceGenerator.createService(ApiService.class);


        Call<Usuario> call;
        final Usuario usuario = new Usuario(nombre,correo,contraseña);
        call = service.createUsuario(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                try {
                    if (response.isSuccessful()) {

                        Usuario usuarios = response.body();
                        Log.d(TAG, "usuario: " + usuarios);

                        Toast.makeText(RegisterUsuarioActivity.this, "Registro guardado satisfactoriamente", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        startActivity(new Intent(RegisterUsuarioActivity.this, MainActivity.class));
                        finish();

                    } else {
                        throw new Exception(ApiServiceGenerator.parseError(response).getMessage());
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toast.makeText(RegisterUsuarioActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(RegisterUsuarioActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}