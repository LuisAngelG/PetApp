package com.lgastelu.petapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgastelu.petapp.DetailActivity;
import com.lgastelu.petapp.R;
import com.lgastelu.petapp.models.Mascota;
import com.lgastelu.petapp.service.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private List<Mascota> mascotas;

    private static final String TAG = MascotaAdapter.class.getSimpleName();

    public MascotaAdapter(){
        this.mascotas = new ArrayList<>();
    }

    public void setMascota(List<Mascota> mascotas){
        this.mascotas=mascotas;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoImage;
        TextView nombreText, razaText, edadText;

        ViewHolder(View itemView){
            super(itemView);
            fotoImage = itemView.findViewById(R.id.foto_image);
            nombreText = itemView.findViewById(R.id.nombre_text);
            razaText = itemView.findViewById(R.id.raza_text);
            edadText = itemView.findViewById(R.id.edad_text);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mascota, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int position) {

        final Context context = viewHolder.itemView.getContext();

        final Mascota mascota = this.mascotas.get(position);

        viewHolder.nombreText.setText(mascota.getNombres());
        viewHolder.razaText.setText(mascota.getRaza());
        viewHolder.edadText.setText(mascota.getEdad() + " años");

        String url = ApiService.API_BASE_URL + "/mascotas/images/" + mascota.getFoto();
        Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.fotoImage);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", mascota.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mascotas.size();
    }

}
