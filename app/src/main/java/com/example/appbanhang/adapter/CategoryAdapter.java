package com.example.appbanhang.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.DuongdaActivity;
import com.example.appbanhang.activity.TrangdiemActivity;
import com.example.appbanhang.activity.TreatmentActivity;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.Category;


import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    List<Category> listItemsCategory;

    public CategoryAdapter(Context context, List<Category> listItemsCategory) {
        this.context = context;
        this.listItemsCategory = listItemsCategory;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        ImageView imageViewCategory;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewCategory);
            imageViewCategory = (ImageView) itemView.findViewById(R.id.imageViewCategory);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_category, parent, false);

        return new CategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        Category category = listItemsCategory.get(position);
        holder.textViewName.setText(category.getName());
        Glide.with(context).load(category.getImage()).into(holder.imageViewCategory);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                switch (position){
                    case 0:
                        Intent intentDuongda = new Intent(context, DuongdaActivity.class);
                        intentDuongda.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentDuongda);
                        break;
                    case 1:
                        Intent intentTrangdiem = new Intent(context, TrangdiemActivity.class);
                        intentTrangdiem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentTrangdiem);
                        break;
                    case 2:
                        Intent intentTreatment = new Intent(context, TreatmentActivity.class);
                        intentTreatment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentTreatment);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemsCategory.size();
    }
}