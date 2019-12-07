package com.lgastelu.petapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lgastelu.petapp.models.Mascota;
import com.lgastelu.petapp.service.ApiService;
import com.lgastelu.petapp.service.ApiServiceGenerator;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private Long id;

    private ImageView fotoImage;
    private TextView nombreText;
    private TextView edadText;
    private TextView razaText;
    private TextView dueñoText, correoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fotoImage = findViewById(R.id.foto_image);
        nombreText = findViewById(R.id.nombre_text);
        razaText = findViewById(R.id.raza_text);
        edadText = findViewById(R.id.edad_text);
        dueñoText = findViewById(R.id.dueño_text);
        correoText = findViewById(R.id.correo_text);

        id = getIntent().getExtras().getLong("ID");
        Log.e(TAG, "id:" + id);

        initialize();
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Mascota> call = service.showMascota(id);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final String usuarioNombre = sp.getString("usuarioNombre", null);
        final String usuarioCorreo = sp.getString("usuarioCorreo", null);

        call.enqueue(new Callback<Mascota>() {
            @Override
            public void onResponse(@NonNull Call<Mascota> call, @NonNull Response<Mascota> response) {
                try {

                    if (response.isSuccessful()) {

                        Mascota mascota = response.body();
                        Log.d(TAG, "mascota: " + mascota);

                        nombreText.setText(mascota.getNombres());
                        razaText.setText(mascota.getRaza());
                        edadText.setText(mascota.getEdad());
                        dueñoText.setText(usuarioNombre);
                        correoText.setText(usuarioCorreo);

                        String url = ApiService.API_BASE_URL + "/mascotas/images/" + mascota.getFoto();
                        Picasso.with(DetailActivity.this).load(url).into(fotoImage);

                    } else {
                        throw new Exception(ApiServiceGenerator.parseError(response).getMessage());
                    }

                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Mascota> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }

}

