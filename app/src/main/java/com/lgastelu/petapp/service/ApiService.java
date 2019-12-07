package com.lgastelu.petapp.service;

import com.lgastelu.petapp.models.Mascota;
import com.lgastelu.petapp.models.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    String API_BASE_URL = "http://10.0.2.2:8080";

    @GET("/mascotas")
    Call<List<Mascota>> findAll();

    @GET("/usuarios/{id}")
    Call<Usuario> showUsuario(@Path("id") Long id);

    @POST("/api/usuarios")
    Call<Usuario> createUsuario(@Body Usuario usuario);

    @Multipart
    @POST("/mascotas")
    Call<Mascota> createMascota(@Part("nombres") RequestBody nombres,
                                  @Part("raza") RequestBody raza,
                                  @Part("edad") RequestBody edad,
                                  @Part MultipartBody.Part foto,
                                  @Part("usuario") RequestBody usuario);
    @FormUrlEncoded
    @POST("/mascotas")
    Call<Mascota> createMascota(@Field("nombres") String nombres,
                                  @Field("raza") String raza,
                                  @Field("usuario") String usuario,
                                  @Field("edad") String edad);

    @GET("/mascotas/{id}")
    Call<Mascota> showMascota(@Path("id") Long id);

    @FormUrlEncoded
    @POST("/auth/login")
    Call<Usuario> login(@Field("correo") String correo,
                         @Field("password") String password);
}
