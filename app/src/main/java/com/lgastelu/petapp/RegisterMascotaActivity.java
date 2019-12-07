package com.lgastelu.petapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lgastelu.petapp.models.Mascota;
import com.lgastelu.petapp.service.ApiService;
import com.lgastelu.petapp.service.ApiServiceGenerator;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterMascotaActivity extends AppCompatActivity {

    private static final String TAG = RegisterMascotaActivity.class.getSimpleName();

    private ImageView imagenPreview;

    private EditText nombreInput;
    private EditText razaInput;
    private EditText edadInput;
    private EditText usuarioInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mascota);

        imagenPreview = findViewById(R.id.imagen_preview);
        nombreInput = findViewById(R.id.txt_nombres_mascota);
        razaInput = findViewById(R.id.txt_raza_mascota);
        edadInput = findViewById(R.id.txt_edad_mascota);
        usuarioInput=findViewById(R.id.txt_usuario_mascota);

    }

    private static final int REQUEST_CAMERA = 100;

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap = scaleBitmapDown(bitmap, 800);  // Redimensionar
                imagenPreview.setImageBitmap(bitmap);
            }
        }
    }

    public void callRegister(View view){

        String nombre = nombreInput.getText().toString();
        String raza = razaInput.getText().toString();
        String edad = edadInput.getText().toString();
        String usuario = usuarioInput.getText().toString();

        if (nombre.isEmpty() || raza.isEmpty()) {
            Toast.makeText(this, "Nombre y Raza son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Mascota> call;

        if(bitmap == null){
            call = service.createMascota(nombre,raza, edad, usuario);
        } else {

            // De bitmap a ByteArray
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // ByteArray a MultiPart
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("foto", "photo.jpg", requestFile);

            // Paramestros a Part
            RequestBody nombrePart = RequestBody.create(MultipartBody.FORM, nombre);
            RequestBody razaPart = RequestBody.create(MultipartBody.FORM, raza);
            RequestBody edadPart = RequestBody.create(MultipartBody.FORM, edad);
            RequestBody usuarioPart = RequestBody.create(MultipartBody.FORM, usuario);

            call = service.createMascota(nombrePart, razaPart, edadPart, imagenPart,usuarioPart);
        }

        call.enqueue(new Callback<Mascota>() {
            @Override
            public void onResponse(@NonNull Call<Mascota> call, @NonNull Response<Mascota> response) {
                try {
                    if(response.isSuccessful()) {

                        Mascota mascotas = response.body();
                        Log.d(TAG, "mascotas: " + mascotas);

                        Toast.makeText(RegisterMascotaActivity.this, "Registro guardado satisfactoriamente", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        startActivity(new Intent(RegisterMascotaActivity.this, MainActivity.class));
                        finish();

                    }else{
                        throw new Exception(ApiServiceGenerator.parseError(response).getMessage());
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toast.makeText(RegisterMascotaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Mascota> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toast.makeText(RegisterMascotaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Redimensionar una imagen bitmap
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

}