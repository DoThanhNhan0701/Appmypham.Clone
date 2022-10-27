package com.example.appbanhang.adapter.adapterAdmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Advertisement;

import java.util.List;

public class AdminAdvertiseAdapter extends RecyclerView.Adapter<AdminAdvertiseAdapter.MyViewHolder> {
    Context context;
    List<Advertisement> advertisementList;

    public AdminAdvertiseAdapter(Context context, List<Advertisement> advertisementList) {
        this.context = context;
        this.advertisementList = advertisementList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_admin_advertise, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Advertisement advertisement = advertisementList.get(position);
        holder.txtIdAvertise.setText("Quãng cáo: " + advertisement.getId());

        Glide.with(context).load(advertisement.getImages()).into(holder.imageViewQc);
    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdAvertise;
        ImageView imageViewQc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdAvertise = (TextView) itemView.findViewById(R.id.idAdvertise);
            imageViewQc = (ImageView) itemView.findViewById(R.id.imagesAdvertiseAdmin);
        }
    }

}
