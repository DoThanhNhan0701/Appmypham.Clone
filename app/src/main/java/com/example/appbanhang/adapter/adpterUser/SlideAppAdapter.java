package com.example.appbanhang.adapter.adpterUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Advertisement;

import java.util.List;

public class SlideAppAdapter extends RecyclerView.Adapter<SlideAppAdapter.MyViewHolder> {
    Context context;
    List<Advertisement> advertisementList;

    public SlideAppAdapter(Context context, List<Advertisement> advertisementList) {
        this.context = context;
        this.advertisementList = advertisementList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_slide_app, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Advertisement advertisement = advertisementList.get(position);
        Glide.with(context).load(advertisement.getImages()).into(holder.imageViewPage2);
    }

    @Override
    public int getItemCount() {
        if(advertisementList != null){
            return advertisementList.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPage2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPage2 = (ImageView) itemView.findViewById(R.id.imageViewSlide_App);
        }
    }
}
