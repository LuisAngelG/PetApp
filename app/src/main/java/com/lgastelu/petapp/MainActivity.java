package com.lgastelu.petapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgastelu.petapp.adapters.MascotaAdapter;
import com.lgastelu.petapp.models.ApiError;
import com.lgastelu.petapp.models.Mascota;
import com.lgastelu.petapp.service.ApiService;
import com.lgastelu.petapp.service.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mascotasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mascotasList = findViewById(R.id.listview_mascotas);

        mascotasList.setLayoutManager(new LinearLayoutManager(this));

        mascotasList.setAdapter(new MascotaAdapter());

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().remove("islogged").commit();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    public void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        service.findAll().enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful()) {

                    List<Mascota> mascotas = response.body();
                    Log.d(TAG, "mascotas: " + mascotas);

                    MascotaAdapter adapter = (MascotaAdapter) mascotasList.getAdapter();
                    adapter.setMascota(mascotas);
                    adapter.notifyDataSetChanged();

                } else {
                    ApiError error = ApiServiceGenerator.parseError(response);
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void callMascotaRegister(View view) {
        startActivityForResult(new Intent(getApplicationContext(), RegisterMascotaActivity.class), 100);
    }
}