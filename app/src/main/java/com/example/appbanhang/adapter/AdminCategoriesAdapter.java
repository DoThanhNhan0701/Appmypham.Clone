package com.example.appbanhang.adapter;

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
import com.example.appbanhang.model.Category;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdminCategoriesAdapter extends RecyclerView.Adapter<AdminCategoriesAdapter.MyViewHolder> {
    Context context;
    List<Category> categoryList;

    public AdminCategoriesAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_items_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtNameCategory.setText(category.getName());
        holder.txtDescripCategory.setText(category.getDecription());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateCategories = simpleDateFormat.format(category.getCreate_date());
        holder.txtDateCategory.setText(dateCategories);

        Glide.with(context).load(category.getImage()).into(holder.imageCategory);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameCategory;
        TextView txtDateCategory;
        TextView txtDescripCategory;
        ImageView imageCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = (ImageView) itemView.findViewById(R.id.imagesCategoryAdmin);
            txtNameCategory = (TextView) itemView.findViewById(R.id.textNameCategoryAdmin);
            txtDateCategory = (TextView) itemView.findViewById(R.id.date_category);
            txtDescripCategory = (TextView) itemView.findViewById(R.id.descripton_category);
        }
    }
}
