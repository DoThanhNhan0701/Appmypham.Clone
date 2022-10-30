package com.example.appbanhang.adapter.adpterUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.screenUser.DetailProductActivity;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class TitileCategoryAdapter extends RecyclerView.Adapter<TitileCategoryAdapter.MyViewHolder> {
    Context context;
    List<Product> listTitleCategoryAdapter;

    public TitileCategoryAdapter(Context context, List<Product> listTitleCategoryAdapter) {
        this.context = context;
        this.listTitleCategoryAdapter = listTitleCategoryAdapter;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        TextView textViewPrice_new;
        TextView textViewPrice_old;
        ImageView imageViewProduct;
        private ItemClickListener itemClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewNameTitle);
            textViewPrice_new = (TextView) itemView.findViewById(R.id.textViewPrice_new);
            textViewPrice_old = (TextView) itemView.findViewById(R.id.textViewPrice_old);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProduct);
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_category_product, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = listTitleCategoryAdapter.get(position);

        String categoryProduct = product.getName();
        if(categoryProduct.length() <= 19){
            holder.textViewName.setText(categoryProduct);
        }else{
            holder.textViewName.setText(categoryProduct.substring(0, 20) +"...");
        }
        int priceNew = product.getPrice_old() * (100 - product.getDiscount()) / 100;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPrice_new.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(priceNew))) + "đ");
        holder.textViewPrice_old.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(product.getPrice_old()))) + "đ");
        holder.textViewPrice_old.setPaintFlags(holder.textViewPrice_old
                .getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(context).load(product.getImages()).into(holder.imageViewProduct);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    Intent intentTitle = new Intent(context, DetailProductActivity.class);
                    intentTitle.putExtra("title", product);
                    intentTitle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentTitle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTitleCategoryAdapter.size();
    }
}
