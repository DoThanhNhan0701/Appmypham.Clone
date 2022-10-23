package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.DetailMagazineActivity;
import com.example.appbanhang.model.Magazine;

import java.util.List;

public class MagazineAdapter extends RecyclerView.Adapter<MagazineAdapter.MyViewHolder> {
    Context context;
    List<Magazine> magazineList;

    public MagazineAdapter(Context context, List<Magazine> magazineList) {
        this.context = context;
        this.magazineList = magazineList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_magazine, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Magazine magazine = magazineList.get(position);
        String nameMagazine = magazine.getName_magazine();
        if(nameMagazine.length() <= 31){
            holder.txtNameMagazine.setText(nameMagazine);
        }
        else{
            holder.txtNameMagazine.setText(nameMagazine.substring(0, 32) +"...");
        }
        Glide.with(context).load(magazine.getImages_magazine()).into(holder.imageViewMagazine);

        holder.imageViewMagazine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetail = new Intent(context, DetailMagazineActivity.class);
                intentDetail.putExtra("titleMagazine", magazine);
                intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return magazineList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMagazine;
        TextView txtNameMagazine;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameMagazine = (TextView) itemView.findViewById(R.id.txtNameMagazine);
            imageViewMagazine = (ImageView) itemView.findViewById(R.id.imageMagazine);
        }
    }
}


